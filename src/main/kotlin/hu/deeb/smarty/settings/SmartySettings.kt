package hu.deeb.smarty.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "SmartySettings",
    storages = [Storage("smarty.xml")]
)
@Service(Service.Level.PROJECT)
class SmartySettings : PersistentStateComponent<SmartySettings> {

    var templatePaths: MutableList<String> = mutableListOf(
        "/templates"
    )

    override fun getState(): SmartySettings = this

    override fun loadState(state: SmartySettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): SmartySettings {
            return project.service()
        }
    }
}