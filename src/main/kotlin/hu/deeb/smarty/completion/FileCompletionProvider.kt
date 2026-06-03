package hu.deeb.smarty.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.util.ProcessingContext
import com.jetbrains.smarty.lang.SmartyTokenTypes
import hu.deeb.smarty.util.Pattern
import hu.deeb.smarty.util.TemplateUtil

class FileCompletionProvider : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            Pattern.filePattern,
            object : CompletionProvider<CompletionParameters>() {

                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {

                    val position = parameters.position

                    var dir = ""

                    val node = position.node
                    if (node?.elementType == SmartyTokenTypes.STRING_LITERAL) {

                        val text = position.text
                        val i = text.lastIndexOf("/")

                        if (i > 0) {
                            dir = text.substring(0, i + 1)
                        }
                    }

                    val filePath = position.containingFile.originalFile.virtualFile.path
                    val templateDir = TemplateUtil.getTemplateDirectory(position.project, filePath)
                    val fullDir = templateDir + dir
                    result.addAllElements(
                        getTemplateCompletion(position.project, fullDir)
                    )
                }
            }
        )
    }

    companion object {
        fun getTemplateCompletion(project: Project, dir: String): List<LookupElement> {

            val lookupElements = mutableListOf<LookupElement>()
            val uniqueList = mutableSetOf<String>()

            TemplateUtil.collectFiles(project, dir) { _, fileName ->

                if (uniqueList.add(fileName)) {
                    lookupElements.add(
                        LookupElementBuilder.create(fileName)
                    )
                }
            }

            return lookupElements
        }
    }
}