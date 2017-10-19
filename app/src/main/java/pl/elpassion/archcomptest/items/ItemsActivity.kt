package pl.elpassion.archcomptest.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.items_activity.*
import pl.elpassion.archcomptest.R
import pl.elpassion.archcomptest.app.App

class ItemsActivity : AppCompatActivity() {

    private val items = mutableListOf<App.Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_activity)
        itemsRecycler.adapter = ItemsAdapter(items)
        val itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
        itemsViewModel.state.observe(this, Observer {
            updateViews(it)
        })
        itemsViewModel.event.accept(Items.Event.GetItems)
    }

    private fun updateViews(state: Items.State?) {
        state?.let { itemsLoader.visibility = if (it.isLoading) View.VISIBLE else View.GONE }
        state?.items?.let { items.clear(); items.addAll(it); itemsRecycler.adapter.notifyDataSetChanged() }
        state?.exception?.let(this::showError)
    }

    private fun showError(exception: Throwable) {
        Snackbar.make(itemsLayout, "${exception.message}", Snackbar.LENGTH_SHORT)
                .setAction("Refresh", { })
                .show()
    }
}