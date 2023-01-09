package gitversion

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ManifestTransformerTask: DefaultTask() {

   @get:InputFile
   abstract val gitInfoFile: RegularFileProperty

   @get:InputFile
   abstract val mergedManifest: RegularFileProperty

   @get:OutputFile
   abstract val updatedManifest: RegularFileProperty

   @TaskAction
   fun taskAction() {
      val gitVersion = gitInfoFile.get().asFile.readText()
      var manifest = mergedManifest.asFile.get().readText()
      manifest = manifest.replace(
         "android:versionName=\"1.0\"",
         "android:versionName=\"${gitVersion}\""
      )

      // 先 get或先 asFile，都可以的，最终都是 File 对象
      // updatedManifest.get().asFile.writeText(manifest)
      updatedManifest.asFile.get().writeText(manifest)
   }
}