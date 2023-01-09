package gitversion

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import java.io.File
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.tasks.TaskProvider
import com.android.build.api.artifact.SingleArtifact

class GitVersionPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        // ./gradlew :app:gitVersionProvider
        val gitVersionProvider = project.tasks.register<GitVersionTask>("gitVersionProvider") {
            // 对输出文件属性赋值一个文件对象 app/build/intermediates/gitVersionProvider/output
            gitVersionOutputFile.set(File(project.buildDir, "intermediates/gitVersionProvider/output"))
            outputs.upToDateWhen { false } // 将 upToDateWhen 设置为 false，这样此 Task 前一次执行的输出就不会被复用
        }

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            // staging/debug/release ManifestUpdater,   $ ./gradlew :app:stagingManifestUpdater
            // 输出文件位置：app/build/intermediates/merged_manifest/staging/[build variant]ManifestUpdater/AndroidManifest.xml
            val manifestUpdater: TaskProvider<ManifestTransformerTask> = project.tasks.register<ManifestTransformerTask>(variant.name + "ManifestUpdater") {
                // 设置输入(对 ManifestTransformerTask来说，是输入)，输入的是 GitVersionTask 的输出 gitVersionOutputFile
                gitInfoFile.set(gitVersionProvider.get().gitVersionOutputFile)
                // gitInfoFile.set(gitVersionProvider.flatMap {
                //     it.gitVersionOutputFile
                // })
            }
            variant.artifacts.use(manifestUpdater)
                .wiredWithFiles(ManifestTransformerTask::mergedManifest, ManifestTransformerTask::updatedManifest)
                .toTransform(SingleArtifact.MERGED_MANIFEST)

        }

        /*
         * GitVersionPlugin， 注册 []ManifestUpdater 任务，其依赖了 ManifestTransformerTask。
         *
         * 当执行 ./gradlew :app:stagingManifestUpdater;
         * 发现 其依赖了 gitVersionProvider 的 GitVersionTask;
         * GitVersionTask 输出文本，并创建文件到  app/build/intermediates/gitVersionProvider/output；
         * manifestUpdater 的 ManifestTransformerTask， 读取 gitVersion 文本；
         * 在 manifest 文件中替换文本，得到了 app/build/intermediates/merged_manifest/staging/[build variant]ManifestUpdater/AndroidManifest.xml
         */
    }
}