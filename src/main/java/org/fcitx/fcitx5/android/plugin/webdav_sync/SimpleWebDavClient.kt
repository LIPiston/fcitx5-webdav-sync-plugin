package org.fcitx.fcitx5.android.plugin.webdav_sync

import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import java.io.File

/**
 * 基于 [sardine-android](https://github.com/thegrizzlylabs/sardine-android) 的简单封装，
 * 目前只用到「从远端 WebDAV 下载到本地文件」这一能力。
 */
object SimpleWebDavClient {

    @Throws(Exception::class)
    fun downloadToFile(
        remoteUrl: String,
        username: String?,
        password: String?,
        targetFile: File,
    ) {
        val sardine = OkHttpSardine()
        if (!username.isNullOrEmpty() || !password.isNullOrEmpty()) {
            sardine.setCredentials(username.orEmpty(), password.orEmpty())
        }

        sardine.get(remoteUrl).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
