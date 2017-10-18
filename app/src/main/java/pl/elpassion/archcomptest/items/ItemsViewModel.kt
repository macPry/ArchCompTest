package pl.elpassion.archcomptest.items

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import pl.elpassion.archcomptest.app.App
import pl.elpassion.archcomptest.app.DI
import pl.elpassion.archcomptest.items.Items.*

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    val state: MutableLiveData<State> = MutableLiveData()
    val event: PublishRelay<Event> = PublishRelay.create()

    private val clear = PublishRelay.create<Unit>()

    private val appModel: App.Model by lazy { DI.provideAppModel() }

    init {
        appModel.states
                .takeUntil(clear)
                .subscribe {
                    when (it) {
                        is App.States.Items -> state.postValue(State.Items(it.items))
                        is App.States.Error -> state.postValue(State.Error(it.exception))
                    }
                }
        event.map {
            when (it) {
                Event.Create -> App.Events.GetItems
            }
        }.subscribe(appModel.events)
    }

    override fun onCleared() = clear.accept(Unit)
}