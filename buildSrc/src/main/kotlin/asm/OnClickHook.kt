package asm

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.internal.impldep.org.bouncycastle.asn1.x500.style.RFC4519Style.name
import org.gradle.internal.impldep.org.objectweb.asm.Opcodes.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.Opcodes
import java.util.*

//class Axx: TransformAction {
abstract class OnClickHook : AsmClassVisitorFactory<InstrumentationParameters.None> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        val classVisitor = object: ClassVisitor(Opcodes.ASM9, nextClassVisitor) {
            private var mInterfaces: Array<out String>? = null
            override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
                super.visit(version, access, name, signature, superName, interfaces)
                mInterfaces = interfaces
            }

            override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
                val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
//                println("visitMethod : $name $descriptor ${Arrays.toString(mInterfaces)}")
                // 被注入的是非  lambda 代码中使用;
//                if (name != "onClick" || descriptor != "(Landroid/view/View;)V" || mInterfaces?.contains("android/view/View${'$'}OnClickListener") != true) {
                // 使用kotlin lambda后，onClick 方法名 变成了 onCreate$lambda$0; 且 mInterfaces 也不包含OnClickListener
                if (descriptor != "(Landroid/view/View;)V") {
                    return mv
                }
                println("visitMethod: OnClickListener#onClick ")
                val newMv = object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
                    override fun onMethodEnter() {
                        println("visitMethod: onMethodEnter")
//                        mv.visitLdcInsn("TAG")
//                        mv.visitLdcInsn("onCreate: stone----")
//                        mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i",
//                            "(Ljava/lang/StringLjava/lang/String)I", false)
//                        mv.visitInsn(POP)
////                        mv.visitVarInsn(ALOAD, 0)
////                        mv.visitMethodInsn(INVOKESTATIC, "com/stone/gradlestudy/HookClick", "click", "()V", false)
////                        mv.visitInsn(RETURN)
//                        mv.visitMaxs(2, 1)
//                        mv.visitEnd()
                        super.onMethodEnter()
                    }

                    override fun onMethodExit(opcode: Int) {
                        super.onMethodExit(opcode)
                    }

                }
                return newMv
            }
        }

        return classVisitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className.startsWith("com.stone")
    }

}