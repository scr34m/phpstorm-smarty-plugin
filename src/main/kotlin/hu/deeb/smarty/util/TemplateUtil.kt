package hu.deeb.smarty.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import hu.deeb.smarty.settings.SmartySettings
import hu.deeb.smarty.cache.Cache
import java.nio.file.Paths

object TemplateUtil {

    fun getTemplateDirectory(project: Project, path: String): String {
        val settings = SmartySettings.getInstance(project)
        val projectBasePath = project.basePath?.replace("\\", "/") ?: return "/templates"

        val normalizedPath = path.replace("\\", "/")

        return settings.templatePaths
            .map { base ->
                val normalizedBase = base.replace("\\", "/").trimEnd('/')
                if (normalizedBase.startsWith("/")) {
                    Paths.get(projectBasePath, normalizedBase.removePrefix("/"))
                        .normalize()
                        .toString()
                        .replace("\\", "/")
                } else {
                    Paths.get(projectBasePath, normalizedBase)
                        .normalize()
                        .toString()
                        .replace("\\", "/")
                }
            }
            .filter { absoluteBase ->
                normalizedPath == absoluteBase ||
                        normalizedPath.startsWith("$absoluteBase/")
            }
            .maxByOrNull { it.length }
            ?: "/templates"
    }

    fun collectFiles(project: Project, dir: String, visitor: SmartyTemplateVisitor) {
        val tplFiles = Cache.getInstance(project).getTplFiles()
        val normalizedDir = normalize(dir).trimEnd('/')

        for (file in tplFiles) {
            val path = normalize(file.path)

            if (!path.startsWith("$normalizedDir/")) {
                continue
            }

            val relative = path.substring(normalizedDir.length + 1)
            visitor.visitFile(file, relative)
        }
    }

    private fun normalize(path: String): String {
        return path.replace("\\", "/")
    }

    fun interface SmartyTemplateVisitor {
        fun visitFile(file: VirtualFile, fileName: String)
    }
}