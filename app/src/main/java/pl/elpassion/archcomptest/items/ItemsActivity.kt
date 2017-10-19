package pl.elpassion.archcomptest.items

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.items_activity.*
import pl.elpassion.archcomptest.R

class ItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.items_activity)
        val itemsViewModel = ViewModelProviders.of(this).get(ItemsViewModel::class.java)
        itemsViewModel.state.observe(this, Observer {
            updateViews(it)
        })
        itemsViewModel.event.accept(Items.Event.GetItems)
    }

    private fun updateViews(state: Items.State?) {
        state?.let { itemsLoader.visibility = if (it.isLoading) View.VISIBLE else View.GONE }
        state?.items?.forEach { itemsLayout.addView(TextView(this).apply { text = it.name }) }
        state?.exception?.let(this::showError)
    }

    private fun showError(exception: Throwable) {
        Snackbar.make(itemsLayout, exception.message.toString(), Snackbar.LENGTH_SHORT)
                .setAction("Refresh", { })
                .show()
    }
}