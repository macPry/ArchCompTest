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
        itemsViewModel.event.accept(Items.Event.Create)
    }

    private fun updateViews(state: Items.State?) {
        itemsLoader.visibility = if (state is Items.State.Loading) View.VISIBLE else View.GONE
        when (state) {
            is Items.State.Items -> state.items.forEach {
                itemsLayout.addView(TextView(this).apply { text = it.name })
            }
            is Items.State.Error -> showError(state)
        }
    }

    private fun showError(error: Items.State.Error) {
        Snackbar.make(itemsLayout, error.exception.message.toString(), Snackbar.LENGTH_SHORT)
                .setAction("Refresh", { })
                .show()
    }
}