package pl.elpassion.archcomptest.items

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    val states: MutableLiveData<Items.State> = MutableLiveData()
    val events: PublishRelay<Items.Event> = PublishRelay.create()

    private val clear = PublishRelay.create<Unit>()

    //private val model: Items.Model by lazy { DI.provideModel() }

}