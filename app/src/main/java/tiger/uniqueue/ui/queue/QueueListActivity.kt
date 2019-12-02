package tiger.uniqueue.ui.queue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.UserUiConf
import tiger.uniqueue.onError
import tiger.uniqueue.startActivity
import tiger.uniqueue.ui.login.LoginViewModel

class QueueListActivity : AppCompatActivity() {

    @BindView(R.id.rv_queue_list)
    lateinit var queueList: RecyclerView
    @BindView(R.id.swiperefresh)
    lateinit var swipeLayout: SwipeRefreshLayout
    @BindView(R.id.fab_add)
    lateinit var addQueueFab: FloatingActionButton

    private lateinit var viewModel: QueueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_list)
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this, QueueListViewModelFactory())
            .get(QueueListViewModel::class.java)

        val type = InMemCache.INSTANCE[LoginViewModel.USER_TYPE_KEY] ?: LoginType.STUDENT
        val uiConf = UserUiConf.valueOf(type)
        val queueAdapter = QueueAdapter(uiConf)
        queueList.layoutManager = LinearLayoutManager(this)
        queueList.adapter = queueAdapter
        queueAdapter.setOnItemClickListener { _, view, position ->
            viewModel.selectInfo(
                viewModel.activeQueues.value?.data?.get(
                    position
                )
            )
        }

        if (uiConf.showAddQueueFab) {
            addQueueFab.show()
        }

        swipeLayout.setOnRefreshListener {
            refreshData()
        }

        viewModel.activeQueues.observe(
            this,
            androidx.lifecycle.Observer { t ->
                when (t) {
                    is Resource.Success -> {
                        swipeLayout.isRefreshing = false
                        queueAdapter.setNewData(t.data)
                    }
                    is Resource.Loading -> {
                        swipeLayout.isRefreshing = true
                    }
                    is Resource.Error -> {
                        swipeLayout.isRefreshing = false
                        onError(t.message)
                    }
                }
            })
        viewModel.selectedQueue.observe(this, androidx.lifecycle.Observer { q ->
            val selected = q ?: return@Observer
            startActivity<QueueDetailActivity> {
                it.putExtra(
                    QueueDetailActivity.QUEUE_ID_EXTRA,
                    selected.id
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        viewModel.fetchQueueInfo()
    }
}
