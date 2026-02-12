package org.fcitx.fcitx5.android.plugin.webdav_sync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 简单的配置界面，用于填写 WebDAV 参数并手动触发一次同步。
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val urlEdit = findViewById<EditText>(R.id.edit_webdav_url)
        val userEdit = findViewById<EditText>(R.id.edit_username)
        val passEdit = findViewById<EditText>(R.id.edit_password)
        val localFileEdit = findViewById<EditText>(R.id.edit_local_file)
        val saveButton = findViewById<Button>(R.id.button_save)
        val syncButton = findViewById<Button>(R.id.button_sync_now)

        // 载入已有配置
        val current = WebDavConfig.load(this)
        urlEdit.setText(current.url)
        userEdit.setText(current.username.orEmpty())
        passEdit.setText(current.password.orEmpty())
        localFileEdit.setText(current.localFile)

        saveButton.setOnClickListener {
            val config = WebDavConfig(
                url = urlEdit.text.toString().trim(),
                username = userEdit.text.toString().trim().ifEmpty { null },
                password = passEdit.text.toString(),
                localFile = localFileEdit.text.toString().trim(),
            )
            if (!config.isValid) {
                Toast.makeText(this, getString(R.string.msg_missing_config), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            WebDavConfig.save(this, config)
            Toast.makeText(this, getString(R.string.msg_config_saved), Toast.LENGTH_SHORT).show()
        }

        syncButton.setOnClickListener {
            // 直接启动 Service，让它在 onStartCommand / onBind 时执行一次同步。
            Toast.makeText(this, getString(R.string.msg_sync_started), Toast.LENGTH_SHORT).show()
            val serviceIntent = Intent(this, WebDavSyncService::class.java)
            startService(serviceIntent)
        }
    }
}
