package org.fcitx.fcitx5.android.common

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger

/**
 * 与 fcitx5-android 主应用约定的插件 Service 基类。
 * 主应用只关心它是否能成功绑定并保持连接，插件侧在 [start] / [stop] 中做自己的事情即可。
 */
abstract class FcitxPluginService : Service() {

    private lateinit var messenger: Messenger

    open val handler: Handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent): IBinder {
        messenger = Messenger(handler)
        start()
        return messenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 允许通过 startService 方式主动触发一次同步
        start()
        // 不需要长期驻留，交给系统回收
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stop()
        return false
    }

    abstract fun start()

    abstract fun stop()
}
