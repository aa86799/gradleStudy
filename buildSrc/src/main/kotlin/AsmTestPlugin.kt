import org.gradle.api.Plugin
import org.gradle.api.Project
//import  com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.AndroidComponentsExtension
import asm.OnClickHook
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope

class AsmTestPlugin : Plugin<Project> {

    // 在哪里 apply plugin，那里所属的项目或模块就是这个 project 对象
    override fun apply(project: Project) {
        if (project.plugins.hasPlugin("com.android.application")) {
            println("this is app module")

            val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                // InstrumentationScope.PROJECT  当前 project 范围，不含依赖的 library
                variant.instrumentation.transformClassesWith(OnClickHook::class.java, InstrumentationScope.ALL) {}
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
//                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES) // 不重新计算栈帧，而是复制

            }
        }

    }

}