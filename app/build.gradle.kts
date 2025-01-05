import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.campus_news"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.campus_news"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs/mysql-connector-java-5.1.49.jar"))
    /*implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")*/
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.youth.banner:banner:1.4.10")
    implementation ("com.github.bumptech.glide:glide:4.11.0")

    implementation  ("com.scwang.smart:refresh-layout-kernel:2.0.1")    //核心必须依赖
    implementation  ("com.scwang.smart:refresh-header-classics:2.0.1")    //经典刷新头
    implementation  ("com.scwang.smart:refresh-header-radar:2.0.1")       //雷达刷新头
    implementation  ("com.scwang.smart:refresh-header-falsify:2.0.1")     //虚拟刷新头
    implementation  ("com.scwang.smart:refresh-header-material:2.0.1")    //谷歌刷新头
    implementation  ("com.scwang.smart:refresh-header-two-level:2.0.1")   //二级刷新头
    implementation  ("com.scwang.smart:refresh-footer-ball:2.0.1")       //球脉冲加载
    implementation  ("com.scwang.smart:refresh-footer-classics:2.0.1")    //经典加载

    //implementation ("com.carson_ho:SearchLayout:1.0.1")
    implementation  ("com.github.tangguna:SearchBox:1.0.1")

    implementation ("com.github.li-xiaojun:XPopup:2.0.0")
}