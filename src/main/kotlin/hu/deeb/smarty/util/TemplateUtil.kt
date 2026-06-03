package hu.deeb.smarty.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import hu.deeb.smarty.settings.SmartySettings
import hu.deeb.smarty.cache.Cache

object TemplateUtil {

    fun getTemplateDirectory(project: Project, path: String): String {
        val settings = SmartySettings.getInstance(project)

        for (base in settings.templatePaths) {
            if (path.contains(base)) {
                return base
            }
        }

        return "/templates/"
    }

    fun collectFiles(project: Project, dir: String, visitor: SmartyTemplateVisitor) {
        val tplFiles =
            Cache.getInstance(project).getTplFiles()

        for (file in tplFiles) {
            val path = file.path
            val i = path.lastIndexOf(dir)
            if (i >= 0) {
                val relative = path.substring(i + dir.length)
                visitor.visitFile(file, relative)
            }
        }
    }

    fun interface SmartyTemplateVisitor {
        fun visitFile(file: VirtualFile, fileName: String)
    }
}