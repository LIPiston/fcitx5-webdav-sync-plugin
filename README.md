# fcitx5-webdav-sync-plugin

这是从 [fcitx5-android](https://github.com/fcitx5-android/fcitx5-android) 项目中提取出来的独立 WebDAV 同步插件项目。

## 项目说明

WebDAV 同步插件是一个 Fcitx5-Android 的插件，用于与远程 WebDAV 目录同步 Fcitx5 相关数据（例如词库、快捷短语等）。

## 主要功能

- 从 WebDAV 服务器下载文件到本地
- 支持用户名和密码认证
- 与 Fcitx5-Android 主应用通过 IPC 通信
- 提供简单的设置界面

## 项目结构

```
.
├── src/
│   └── main/
│       ├── java/                          # Kotlin 源代码
│       │   └── org/fcitx/fcitx5/android/
│       │       ├── plugin/webdav_sync/   # 主插件代码
│       │       └── common/                # 通用代码（IPC 等）
│       ├── aidl/                          # AIDL 接口定义
│       ├── res/                           # 资源文件
│       │   ├── values/                    # 字符串资源
│       │   ├── layout/                    # UI 布局
│       │   └── xml/                       # 插件描述
│       └── AndroidManifest.xml
├── build.gradle.kts                       # 构建配置
├── settings.gradle.kts
├── gradle.properties
├── gradle/                                # Gradle wrapper 和版本目录
└── README.md
```

## 构建说明

### 前置要求

- Android SDK (API Level 35+)
- Gradle 8.9+
- Kotlin 2.2.21+

### 构建命令

```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease
```

### 签名配置

Release 版本的签名可以通过以下方式配置：

1. **环境变量**（推荐）：
   ```bash
   export SIGN_KEY_FILE=/path/to/keystore.jks
   export SIGN_KEY_PWD=your_keystore_password
   export SIGN_KEY_ALIAS=your_key_alias
   ```

2. **gradle.properties**：
   ```properties
   signKeyFile=/path/to/keystore.jks
   signKeyPwd=your_keystore_password
   signKeyAlias=your_key_alias
   ```

## 依赖

主要依赖：
- `androidx.appcompat:appcompat:1.7.1` - Android AppCompat
- `androidx.core:core-ktx:1.16.0` - Android Core KTX
- `com.google.android.material:material:1.13.0` - Material Design
- `com.github.thegrizzlylabs:sardine-android:0.9` - WebDAV 客户端

## 用法

### 作为插件使用

1. 在 Fcitx5-Android 的 `settings.gradle.kts` 中包含此项目：
   ```kotlin
   include(":webdav-sync")
   project(":webdav-sync").projectDir = file("../fcitx5-webdav-sync-plugin")
   ```

2. 在主应用的 `build.gradle.kts` 中添加依赖（可选）

3. 构建和安装主应用，插件会自动被识别

### 独立开发

此项目可以独立构建和测试：
```bash
./gradlew assembleDebug
```

构建的 APK 可以直接安装到 Android 设备上进行测试。

## 开发指南

### 添加新功能

1. **同步多种数据类型**：在 `WebDavSyncService` 中扩展同步逻辑
2. **定时同步**：使用 `WorkManager` 或 `AlarmManager` 实现周期性同步
3. **UI 增强**：在 `SettingsActivity` 中添加更多配置选项

### 修改 IPC 接口

如果需要修改与 Fcitx5-Android 的通信接口：
1. 修改 `IFcitxRemoteService.aidl`
2. 同步修改主应用的对应接口

## 许可证

本项目遵循 fcitx5-android 的许可证。

## 相关链接

- [Fcitx5-Android 项目](https://github.com/fcitx5-android/fcitx5-android)
- [Sardine Android](https://github.com/thegrizzlylabs/sardine-android)
