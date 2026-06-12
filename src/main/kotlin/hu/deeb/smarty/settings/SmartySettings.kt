package hu.deeb.smarty.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "SmartySettings",
    storages = [Storage("smarty.xml")]
)
@Service(Service.Level.PROJECT)
class SmartySettings : PersistentStateComponent<SmartySettings.State> {

    data class State(
        var templatePaths: MutableList<String> = mutableListOf()
    )

    private var state = State()

    var templatePaths: MutableList<String>
        get() = state.templatePaths
        set(value) {
            state.templatePaths = value
        }

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(project: Project): SmartySettings =
            project.service()
    }
}