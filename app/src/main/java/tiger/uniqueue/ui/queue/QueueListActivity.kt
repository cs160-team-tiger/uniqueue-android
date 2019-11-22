package tiger.uniqueue.ui.queue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import tiger.uniqueue.data.model.QueueInfo
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
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
                viewModel.activeQueues.value?.get(
                    position
                )
            )
        }
        viewModel.activeQueues.observe(
            this,
            androidx.lifecycle.Observer { t -> queueAdapter.setNewData(t) })
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
        BaseQuickAdapter<QueueInfo, BaseViewHolder>(R.layout.q_info, LinkedList()) {
        override fun convert(helper: BaseViewHolder, item: QueueInfo) {
            with(helper) {
                setText(R.id.title, item.title)
                val timeStr = item.time.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString()
                setText(R.id.locationAndTime, "${item.location} (Started in $timeStr)")
                setText(R.id.position, item.inQueueNum.toString())
                setText(R.id.waitingTime, item.waitTime.toMinutes().toString())
            }
        }
    }
}
