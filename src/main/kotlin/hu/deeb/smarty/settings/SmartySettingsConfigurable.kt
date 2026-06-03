package hu.deeb.smarty.settings

import com.intellij.ui.dsl.builder.*
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

class SmartySettingsConfigurable : Configurable {

    private val textArea = JBTextArea()

    override fun getDisplayName() = "Smarty"

    override fun createComponent(): JComponent {
        val settings = settings()
        textArea.text = settings.templatePaths.joinToString("\n")
        return panel {
            row {
                label("Template folders (one per line)")
            }
            row {
                cell(JBScrollPane(textArea))
                    .resizableColumn()
                    .align(com.intellij.ui.dsl.builder.Align.FILL)
            }.resizableRow()
            row {
                comment("Each line is treated as a template root path")
            }
        }
    }

    override fun isModified(): Boolean {
        val settings = settings()
        return textArea.text != settings.templatePaths.joinToString("\n")
    }

    override fun apply() {
        val settings = settings()
        settings.templatePaths = textArea.text.lines().filter { it.isNotBlank() }.toMutableList()
    }

    private fun settings(): SmartySettings {
        val project = com.intellij.openapi.project.ProjectManager.getInstance().defaultProject
        return SmartySettings.getInstance(project)
    }
}