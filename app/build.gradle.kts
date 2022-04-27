import kotlin.io.println
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

//tasks.register("hello") {
//    println("hello  ${project.name}") // 当前 module
//    println("hello  ${project.parent?.name}") // 父级
//}

apply<CustomPlugin>()
apply<gitversion.GitVersionPlugin>()

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.stone.gradlestudy"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        // 非 debug、release的配置，需要使用 create()
        // 构建变体，方式1，直接配置
        // create("staging") {
        //     initWith(getByName("debug"))
        //     manifestPlaceholders["hostName"] = "internal.example.com"
        //     applicationIdSuffix = ".debugStaging"
        // }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // 构建变体，方式2
    // // androidComponents: ApplicationAndroidComponentsExtension
    // val extension = project.extensions.getByName(
    //     "androidComponents"
    // ) as com.android.build.api.variant.ApplicationAndroidComponentsExtension
    //
    // // finalizeDsl, 在 DSL 对象应用于 Variant 创建前对它们进行修改
    // // 在阶段结束时，AGP 将会锁定 DSL 对象，这样它们就无法再被更改
    // extension.finalizeDsl { ext->
    //     println("finalizeDsl")
    //     ext.buildTypes.create("staging").let { buildType ->
    //         buildType.initWith(ext.buildTypes.getByName("debug"))
    //         buildType.manifestPlaceholders["hostName"] = "example.com"
    //         buildType.applicationIdSuffix = ".debugStaging"
    //     }
    // }
    //
    // extension.beforeVariants { variantBuilder ->
    //     println("beforeVariants ${variantBuilder.name}")
    //     if (variantBuilder.name == "staging") {
    //         variantBuilder.enableUnitTest = false
    //         variantBuilder.minSdk = 23
    //     }
    // }

    // 构建变体，方式3
    // androidComponents {
    //     finalizeDsl { ext ->
    //         ext.buildTypes.create("")
    //     }
    //     beforeVariants { variantBuilder ->
    //
    //     }
    // }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}