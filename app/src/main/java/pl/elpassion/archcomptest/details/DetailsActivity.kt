package pl.elpassion.archcomptest.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.elpassion.archcomptest.R
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        private val SELECTED_ITEM_ID_KEY = "selectedItemIdKey"

        fun start(context: Context, selectedItemId: Long) = context.startActivity(intent(context, selectedItemId))

        private fun intent(context: Context, selectedItemId: Long) =
                Intent(context, DetailsActivity::class.java).apply {
                    putExtra(SELECTED_ITEM_ID_KEY, selectedItemId)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
    }
}