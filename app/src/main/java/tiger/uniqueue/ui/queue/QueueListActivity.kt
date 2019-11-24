package tiger.uniqueue.ui.queue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class QueueListActivity : AppCompatActivity() {

    @BindView(R.id.rv_queue_list)
    lateinit var queueList: RecyclerView

    private lateinit var viewModel: QueueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_queue)
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this, QueueListViewModelFactory())
            .get(QueueListViewModel::class.java)

        val queueAdapter = QueueAdapter()
        queueList.layoutManager = LinearLayoutManager(this)
        queueList.adapter = queueAdapter
        queueAdapter.setOnItemClickListener { _, view, position ->
            viewModel.selectInfo(
                viewModel.activeQueues.value?.data?.get(
                    position
                )
            )
        }
        viewModel.activeQueues.observe(
            this,
            androidx.lifecycle.Observer { t -> queueAdapter.setNewData(t.data) })
        viewModel.selectedQueue.observe(this, androidx.lifecycle.Observer {
            val selected = it ?: return@Observer
            // TODO open detailed
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchQueueInfo()
    }

    class QueueAdapter :
        BaseQuickAdapter<tiger.uniqueue.data.model.Queue, BaseViewHolder>(
            R.layout.q_info,
            LinkedList()
        ) {
        override fun convert(helper: BaseViewHolder, item: tiger.uniqueue.data.model.Queue) {
            with(helper) {
                setText(R.id.title, "Queue at ${item.location}")
                val timeStr =
                    LocalDateTime.ofEpochSecond(item.startTime, 0, OffsetDateTime.now().offset)
                        .truncatedTo(ChronoUnit.MINUTES).toString()
                setText(R.id.locationAndTime, "${item.location} (Started in $timeStr)")
                setText(R.id.position, "3")
                setText(R.id.waitingTime, "5")
            }
        }
    }
}
