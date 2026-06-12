package hu.deeb.smarty.navigation

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.jetbrains.php.lang.psi.elements.Function
import hu.deeb.smarty.util.Pattern
import hu.deeb.smarty.util.SourceUtil
import hu.deeb.smarty.util.TemplateUtil
import java.util.*
import javax.xml.transform.Source


class SmartyFileGoToDeclarationHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(
        sourceElement: PsiElement?,
        offset: Int,
        editor: Editor?
    ): Array<out PsiElement?>? {
        if (sourceElement == null) {
            return arrayOfNulls<PsiElement>(0)
        }

        val targets: MutableList<PsiElement?> = ArrayList<PsiElement?>()

        // <{extends file="frontend/register/index.tpl"}>
        if (Pattern.filePattern.accepts(sourceElement)) {
            attachExtendsFileGoto(sourceElement, targets)
        }

        // <{function_to_call('arg', [1, 'test'])}>
        if (Pattern.functionPattern.accepts(sourceElement)) {
            attachFunctionGoto(sourceElement, targets)
        }

        return targets.toTypedArray<PsiElement?>()
    }

    private fun attachExtendsFileGoto(sourceElement: PsiElement, psiElements: MutableList<PsiElement?>) {
        val project = sourceElement.getProject()
        val currentFile = sourceElement.getContainingFile().getVirtualFile()

        val searched = normalizeFilename(sourceElement.getText())

        val dir = TemplateUtil.getTemplateDirectory(sourceElement.project, currentFile.getPath())

        TemplateUtil.collectFiles(project, dir, object : TemplateUtil.SmartyTemplateVisitor {
            override fun visitFile(file: VirtualFile, fileName: String) {
                if (fileName == searched) {
                    val psiFile = PsiManager.getInstance(project).findFile(file)
                    if (psiFile != null) {
                        psiElements.add(psiFile)
                    }
                }
            }
        })
    }

    private fun normalizeFilename(text: String): String {
        var text = text
        if (text.startsWith("./")) {
            text = text.substring(2)
        }
        return text
    }

    private fun attachFunctionGoto(sourceElement: PsiElement, psiElements: MutableList<PsiElement?>) {
        val project = sourceElement.getProject()
        val searched = sourceElement.getText()

        SourceUtil.collectGlobalFunctions(project, object : SourceUtil.SmartyFunctionVisitor {
            override fun visitFunction(function: Function, functionName: String) {
                if (functionName == searched) {
                    psiElements.add(function)
                }
            }
        })
    }
}