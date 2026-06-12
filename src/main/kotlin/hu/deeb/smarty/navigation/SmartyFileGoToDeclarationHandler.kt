package hu.deeb.smarty.navigation

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import hu.deeb.smarty.util.Pattern
import hu.deeb.smarty.util.TemplateUtil

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

        if (Pattern.filePattern.accepts(sourceElement)) {
            attachExtendsFileGoto(sourceElement, targets)
        }

        return targets.toTypedArray<PsiElement?>()
    }

    private fun attachExtendsFileGoto(sourceElement: PsiElement, psiElements: MutableList<PsiElement?>) {
        val project = sourceElement.getProject()
        val currentFile = sourceElement.getContainingFile().getVirtualFile()

        val searchedFileName = normalizeFilename(sourceElement.getText())

        val dir = TemplateUtil.getTemplateDirectory(sourceElement.project, currentFile.getPath())

        TemplateUtil.collectFiles(project, dir, object : TemplateUtil.SmartyTemplateVisitor {
            override fun visitFile(file: VirtualFile, fileName: String) {
                if (fileName != searchedFileName) {
                    return
                }

                val psiFile = PsiManager.getInstance(project).findFile(file)
                if (psiFile != null) {
                    psiElements.add(psiFile)
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

}