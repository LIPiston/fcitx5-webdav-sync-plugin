plugins {
    id("com.android.application") version "8.13.1"
    id("org.jetbrains.kotlin.android") version "2.2.21"
}

android {
    namespace = "org.fcitx.fcitx5.android.plugin.webdav_sync"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.fcitx.fcitx5.android.plugin.webdav_sync"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        // 始终指向正式版 Fcitx5-Android 包名
        buildConfigField(
            "String",
            "MAIN_APPLICATION_ID",
            "\"org.fcitx.fcitx5.android\""
        )
        manifestPlaceholders["mainApplicationId"] = "org.fcitx.fcitx5.android"
    }

    buildFeatures {
        buildConfig = true
        aidl = true
    }

    signingConfigs {
        create("release") {
            val signKeyFile = rootProject.findProperty("signKeyFile")?.toString()
                ?: System.getenv("SIGN_KEY_FILE")
            val signKeyPwd = rootProject.findProperty("signKeyPwd")?.toString()
                ?: System.getenv("SIGN_KEY_PWD")
            val signKeyAlias = rootProject.findProperty("signKeyAlias")?.toString()
                ?: System.getenv("SIGN_KEY_ALIAS")

            if (!signKeyFile.isNullOrEmpty() && !signKeyPwd.isNullOrEmpty() && !signKeyAlias.isNullOrEmpty()) {
                val keyFile = file(signKeyFile)
                if (keyFile.exists()) {
                    storeFile = keyFile
                    storePassword = signKeyPwd
                    keyAlias = signKeyAlias
                    keyPassword = signKeyPwd
                }
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 与主应用匹配，用于显示发布版名称
            resValue("string", "app_name", "@string/app_name_release")
            // 继承主程序的签名配置（仅当存在 storeFile 时）
            val releaseSigning = signingConfigs.findByName("release")
            if (releaseSigning?.storeFile != null) {
                signingConfig = releaseSigning
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue("string", "app_name", "@string/app_name_debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")

    // WebDAV 客户端库（使用 OkHttp 的 Sardine 实现）
    implementation("com.github.thegrizzlylabs:sardine-android:0.9")
}
