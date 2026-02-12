package org.fcitx.fcitx5.android.plugin.webdav_sync

import android.util.Log
import org.fcitx.fcitx5.android.common.FcitxPluginService
import org.fcitx.fcitx5.android.common.ipc.FcitxRemoteConnection
import org.fcitx.fcitx5.android.common.ipc.bindFcitxRemoteService

/**
 * 与 fcitx5-android 主应用通过 IPC 连接，用于触发 WebDAV 同步。
 *
 * 当前实现的策略很简单：
 * - 当服务被主应用绑定时，从配置中读取 WebDAV 信息
 * - 如果配置完整，则拉取远端文件到本地
 * - 同步成功后调用 remote.reloadQuickPhrase() 通知主应用重新加载快捷短语
 *
 * 你可以根据需要扩展为：
 * - 定时同步 / 手动触发同步
 * - 同步多种类型的数据（拼音词库等）
 */
class WebDavSyncService : FcitxPluginService() {

    private var connection: FcitxRemoteConnection? = null

    override fun start() {
        val config = WebDavConfig.load(this)
        if (!config.isValid) {
            Log.w(TAG, "WebDAV config is not valid, skip sync")
            return
        }

        // 绑定到主应用的远程服务
        connection = bindFcitxRemoteService(BuildConfig.MAIN_APPLICATION_ID,
            onDisconnect = {
                Log.d(TAG, "Disconnected from fcitx remote")
            }
        ) { remote ->
            Log.d(TAG, "Connected to fcitx remote, start syncing")
            runCatching {
                val targetFile = getFileStreamPath(config.localFile)
                targetFile.parentFile?.mkdirs()
                SimpleWebDavClient.downloadToFile(
                    remoteUrl = config.url,
                    username = config.username,
                    password = config.password,
                    targetFile = targetFile,
                )
                // 同步完成后，让主应用重新加载快捷短语
                remote.reloadQuickPhrase()
                Log.i(TAG, "WebDAV sync success, quick phrase reloaded")
            }.onFailure {
                Log.w(TAG, "WebDAV sync failed", it)
            }
        }
    }

    override fun stop() {
        connection?.let {
            try {
                unbindService(it)
            } catch (_: Throwable) {
            }
        }
        connection = null
    }

    companion object {
        private const val TAG = "WebDavSyncService"
    }
}
