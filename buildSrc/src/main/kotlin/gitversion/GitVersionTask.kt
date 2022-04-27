package gitversion

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.BufferedReader
import java.io.InputStreamReader

abstract class GitVersionTask : DefaultTask() {

    // RegularFileProperty, 表示某个可配置的常规文件位置，其值是可变的。
    @get:OutputFile
    abstract val gitVersionOutputFile: RegularFileProperty

    @TaskAction
    fun taskAction() {
        // git head 前7位的 commit-id 的 sha-1 hash值
        val process = ProcessBuilder("git", "rev-parse --short HEAD").start()
        if (process.isAlive) {
            val exit = process.waitFor()
            // if (exit != 0) { // 正常执行命令为 0
            //     throw AssertionError(String.format("runCommand returned %d", exit));
            // }
        }
        val error = process.errorStream.readBytes().toString()
        if (error.isNotBlank()) {
            println("Git error : $error ")
        }

        val gitVersionReader = BufferedReader(InputStreamReader(process.inputStream))
        val gitVersion = gitVersionReader.readLine()
        println(gitVersion)
        gitVersionReader.close()

        // 向文件内写入字符串
        gitVersionOutputFile.get().asFile.writeText("3234")
        // 在 m1电脑没有执行成功，回头尝试家里的电脑试试。 在家里的电脑也试过了，还是不行。看了下官网示例，也是注释掉的。
        // gitVersionOutputFile.get().asFile.writeText(gitVersion)
    }
}