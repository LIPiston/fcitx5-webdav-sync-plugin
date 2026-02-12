package org.fcitx.fcitx5.android.plugin.webdav_sync

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 负责读写 WebDAV 配置（URL、用户名、密码、本地文件路径）。
 */
data class WebDavConfig(
    val url: String,
    val username: String?,
    val password: String?,
    val localFile: String,
) {
    val isValid: Boolean
        get() = url.isNotBlank() && localFile.isNotBlank()

    companion object {
        private const val PREF_NAME = "webdav_sync"
        private const val KEY_URL = "url"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_LOCAL_FILE = "local_file"

        fun load(context: Context): WebDavConfig {
            val sp = prefs(context)
            return WebDavConfig(
                url = sp.getString(KEY_URL, "") ?: "",
                username = sp.getString(KEY_USERNAME, null),
                password = sp.getString(KEY_PASSWORD, null),
                localFile = sp.getString(KEY_LOCAL_FILE, "") ?: "",
            )
        }

        fun save(context: Context, config: WebDavConfig) {
            prefs(context).edit {
                putString(KEY_URL, config.url)
                putString(KEY_USERNAME, config.username)
                putString(KEY_PASSWORD, config.password)
                putString(KEY_LOCAL_FILE, config.localFile)
            }
        }

        private fun prefs(context: Context): SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
