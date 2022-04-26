
// all buildscript {} blocks must appear before any plugins {} blocks in the script
// buildscript { // gradle 构建脚本 自身，所需的
//     repositories {
//         google()
//         mavenCentral()
//     }
//
//     dependencies {
//         classpath("com.android.tools.build:gradle:7.1.3")
//         classpath(kotlin("gradle-plugin", version = "1.6.20"))
//     }
// }

plugins {
    `kotlin-dsl` // 使用这个后  buildSrc/src/main/kotlin 才成为源码目录
    kotlin("jvm") version("1.6.20")
}

repositories { // project 所需的 repos
    google()
    mavenCentral()
}

dependencies { // project 所需的 depends
    implementation("com.android.tools.build:gradle-api:7.1.3")
    implementation(kotlin("stdlib", version = "1.6.20"))
    val ga = gradleApi() // 暂没看出来有什么作用
    // print: " gradle 2api: null unspecified null "
    println("gradle api: ${ga.group} ${ga.name} ${ga.version}")
}