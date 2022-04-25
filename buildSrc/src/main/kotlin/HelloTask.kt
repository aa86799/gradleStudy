import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

// 是 abstract 或 open 才能被 register
abstract class HelloTask: DefaultTask() {

    @TaskAction
    fun taskAction() {
        println("Hello \"${project.parent?.name}\" from task!")
    }
}