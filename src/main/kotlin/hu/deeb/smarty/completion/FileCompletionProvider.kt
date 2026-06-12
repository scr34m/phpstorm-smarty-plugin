package hu.deeb.smarty.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.util.ProcessingContext
import hu.deeb.smarty.util.Pattern
import hu.deeb.smarty.util.TemplateUtil

class FileCompletionProvider : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            Pattern.filePattern,
            object : CompletionProvider<CompletionParameters>() {

                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    val currentFile = position.containingFile.originalFile.virtualFile ?: return
                    val templateDir = TemplateUtil.getTemplateDirectory(position.project, currentFile.path).trimEnd('/')
                    val includePathBeforeCaret = getIncludePathBeforeCaret(parameters)
                    val dir = getDirectoryPart(includePathBeforeCaret)
                    var fullDir = templateDir
                    if (!dir.isEmpty()) {
                        fullDir = "${templateDir.trimEnd('/')}/${dir.trimStart('/')}"
                    }

                    result.addAllElements(
                        getTemplateCompletion(position.project, fullDir)
                    )
                }
            }
        )
    }

    companion object {

        private fun getIncludePathBeforeCaret(parameters: CompletionParameters): String {
            val document = parameters.editor.document
            val offset = parameters.offset.coerceAtMost(document.textLength)
            val textBeforeCaret = document.text.substring(0, offset)
            val doubleQuoteIndex = textBeforeCaret.lastIndexOf('"')
            val singleQuoteIndex = textBeforeCaret.lastIndexOf('\'')
            val quoteIndex = maxOf(doubleQuoteIndex, singleQuoteIndex)
            if (quoteIndex < 0) {
                return ""
            }
            return textBeforeCaret.substring(quoteIndex + 1).replace("\\", "/")
        }

        private fun getDirectoryPart(path: String): String {
            val slashIndex = path.lastIndexOf('/')
            if (slashIndex >= 0) {
                return path.substring(0, slashIndex + 1)
            } else {
                return ""
            }
        }

        fun getTemplateCompletion(
            project: Project,
            dir: String
        ): List<LookupElement> {
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