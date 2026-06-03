package hu.deeb.smarty.cache

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager

@Service(Service.Level.PROJECT)
class Cache(private val project: Project) {

    private val tplFilesCache: CachedValue<List<VirtualFile>> =
        CachedValuesManager.getManager(project).createCachedValue {

            CachedValueProvider.Result.create(
                FilenameIndex.getAllFilesByExt(project, "tpl").toList(),
                ProjectRootManager.getInstance(project)
            )
        }

    fun getTplFiles(): List<VirtualFile> =
        tplFilesCache.value

    companion object {
        fun getInstance(project: Project): Cache =
            project.service()
    }
}