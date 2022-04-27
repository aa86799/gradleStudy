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
        // 代码运行这个命令，会提示 is not a git command 。 而单独放到命令行运行，是可以的。真晕
//        val processBuilder = ProcessBuilder("git", "rev-parse --short HEAD")
        // 这个可以正常运行
        val processBuilder = ProcessBuilder("git", "--version")
        // 如果此属性为true，则由该对象的start()方法随后启动的子进程生成的任何错误输出将与标准输出合并，
        // 以便可以使用Process.getInputStream()方法读取它们。初始值为false
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()
        if (process.isAlive) {
            val exit = process.waitFor()
             if (exit != 0) { // 正常执行命令为 0
                 throw AssertionError(String.format("runCommand returned %d", exit))
             }
        }
        val error = process.errorStream.readBytes().toString(Charsets.UTF_8)
        if (error.isNotBlank()) {
            println("Git error : $error ")
        }

        val gitVersionReader = BufferedReader(InputStreamReader(process.inputStream))
        val gitVersion = gitVersionReader.readLine()
        println(gitVersion)
        gitVersionReader.close()

        // 向文件内写入字符串
//        gitVersionOutputFile.get().asFile.writeText("3234")
         gitVersionOutputFile.get().asFile.writeText(gitVersion)
    }
}