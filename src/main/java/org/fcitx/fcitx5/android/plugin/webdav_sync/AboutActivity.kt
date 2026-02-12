package org.fcitx.fcitx5.android.plugin.webdav_sync

import android.app.Activity
import android.os.Bundle

/**
 * 供 Fcitx5-Android 用于发现插件的占位 Activity。
 *
 * Fcitx 主程序只会通过 Intent action `${mainApplicationId}.plugin.MANIFEST`
 * 来枚举插件包名，不会真正启动这个 Activity 的 UI，因此这里可以是一个空实现。
 */
class AboutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 不需要展示界面，立即结束即可
        finish()
    }
}
