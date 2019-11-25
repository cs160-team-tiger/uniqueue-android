package tiger.uniqueue.ui.queue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tiger.uniqueue.R
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Queue

class QueueDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: QueueDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_detail)

        viewModel = ViewModelProviders.of(this)
            .get(QueueDetailViewModel::class.java)


        viewModel.queue.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    updateQueueView(it.data!!)
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        })
    }

    private fun updateQueueView(q: Queue) {
        TODO()
    }
}
