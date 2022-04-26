import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import  com.android.build.api.variant.ApplicationAndroidComponentsExtension

class CustomPlugin : Plugin<Project> {

    // 在哪里 apply plugin，那里所属的项目或模块就是这个 project 对象
    override fun apply(project: Project) {
        project.tasks.register("hello1") {
            doLast {
                println("hello 1")
            }
        }

        project.tasks.register<HelloTask>("hello2") {
            // 依赖一些任务
//            dependsOn("assemble")
            doLast {
                println("hello 2")
            }
        }

        project.task("hello3") {
            doLast {
                println("Hello from the GreetingPlugin")
            }

            doFirst {
                println("Hello from the GreetingPlugin. First")
            }
        }

        addVariant(project, "staging")
    }

    // 添加构建变体
    private fun addVariant(project: Project, variantName: String) {
        val extension = project.extensions.getByName(
            "androidComponents"
        ) as ApplicationAndroidComponentsExtension

        // finalizeDsl, 在 DSL 对象应用于 Variant 创建前对它们进行修改
        // 在阶段结束时，AGP 将会锁定 DSL 对象，这样它们就无法再被更改
        extension.finalizeDsl { ext->
            println("finalizeDsl")
            ext.buildTypes.create(variantName).let { buildType ->
                buildType.initWith(ext.buildTypes.getByName("debug"))
                buildType.manifestPlaceholders["hostName"] = "example.com"
                buildType.applicationIdSuffix = ".debug$variantName"
                buildType.isDebuggable = true
                buildType.isShrinkResources = false // 是否压缩资源
                buildType.isMinifyEnabled = false // 是否混淆
            }
        }

        extension.beforeVariants { variantBuilder ->
            println("beforeVariants ${variantBuilder.name}")
            if (variantBuilder.name == variantName) {
                variantBuilder.enableUnitTest = false
                variantBuilder.minSdk = 23
            }
        }

    }
}