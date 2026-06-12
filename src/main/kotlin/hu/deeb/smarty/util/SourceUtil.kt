package hu.deeb.smarty.util

import com.intellij.openapi.project.Project
import com.jetbrains.php.PhpIndex
import com.jetbrains.php.lang.psi.elements.Function;

object SourceUtil {

    fun collectGlobalFunctions(project: Project, visitor: SmartyFunctionVisitor) {
        val phpIndex = PhpIndex.getInstance(project)

        for (functionName in phpIndex.getAllFunctionNames(null)) {
            val functions: MutableCollection<Function> =
                phpIndex.getFunctionsByName(functionName)

            for (function in functions) {
                visitor.visitFunction(function, functionName)
            }
        }
    }

    fun interface SmartyFunctionVisitor {
        fun visitFunction(function: Function, functionName: String)
    }
}