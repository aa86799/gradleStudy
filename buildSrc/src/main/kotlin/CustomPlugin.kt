import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

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
    }

}