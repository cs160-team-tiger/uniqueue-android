package tiger.uniqueue.ui.queue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import tiger.uniqueue.R
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.Queue
import tiger.uniqueue.onError

class QueueDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: QueueDetailViewModel

    @BindView(R.id.swiperefresh)
    lateinit var swipeRefresh: SwipeRefreshLayout

    private var queueId: Long = Long.MIN_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_detail)
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this)
            .get(QueueDetailViewModel::class.java)

        val intent = getIntent()
        queueId = intent.getLongExtra(QUEUE_ID_EXTRA, Long.MIN_VALUE)
        fetchQueue()

        swipeRefresh.setOnRefreshListener(this::fetchQueue)

        viewModel.queue.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    updateQueueView(it.data!!)
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    onError(it.message)
                }
            }
        })

        viewModel.questions.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    swipeRefresh.isRefreshing = false
                    updateQuestionList(it.data!!)
                }
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    onError(it.message)
                }
            }
        })
    }

    private fun fetchQueue() {
        if (queueId == Long.MAX_VALUE) {
            onError("Wrong Queue ID")
        } else {
            viewModel.getQueue(queueId)
        }
    }

    private fun updateQueueView(q: Queue) {

    }

    private fun updateQuestionList(questions: List<Question>) {

    }

    companion object {
        val QUEUE_ID_EXTRA = "${QueueDetailActivity::class.java.canonicalName}.QUEUE_ID"
    }
}
