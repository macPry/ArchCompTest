package pl.elpassion.archcomptest

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import pl.elpassion.archcomptest.common.TreeSpec

class MainListModelTest : TreeSpec() {

    private val model = MainListModel()
    private val state = model.state.test()

    init {
        nest("When start") {
            assert("should be idle") {
                state.assertValue(MainList.State.Idle)
            }
        }
    }
}

interface MainList {
    sealed class State {
        object Idle : State()
    }
}

class MainListModel {
    val state: Relay<MainList.State> = BehaviorRelay.createDefault<MainList.State>(MainList.State.Idle)
}