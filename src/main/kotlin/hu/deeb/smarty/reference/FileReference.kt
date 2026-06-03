package hu.deeb.smarty.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import hu.deeb.smarty.util.TemplateUtil

class FileReference(
    element: PsiElement
) : PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {

    override fun resolve(): PsiElement? {
        val project = element.project
        val currentFile = element.containingFile.virtualFile
        val templateRoot = TemplateUtil.getTemplateDirectory(project, currentFile.path)
        val targetFile = element.text.trim('"')
        var result: PsiElement? = null
        TemplateUtil.collectFiles(project, templateRoot) { virtualFile, fileName ->
            if (fileName == targetFile) {
                result = PsiManager.getInstance(project).findFile(virtualFile)
            }
        }
        return result
    }

    override fun getVariants(): Array<Any> = emptyArray()

}