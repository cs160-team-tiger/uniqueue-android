package tiger.uniqueue.ui.queue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.data.Network
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.Queue
import tiger.uniqueue.data.model.UserUiConf
import tiger.uniqueue.onError
import tiger.uniqueue.openDialogFragment
import tiger.uniqueue.ui.login.LoginViewModel
import java.util.*


class QueueDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: QueueDetailViewModel

    @BindView(R.id.swiperefresh)
    lateinit var swipeRefresh: SwipeRefreshLayout
    @BindView(R.id.rv_queue_header)
    lateinit var queueHeaderList: RecyclerView
    @BindView(R.id.rv_question_list)
    lateinit var questionListView: RecyclerView
    @BindView(R.id.fab_add)
    lateinit var addQuestionButon: FloatingActionButton

    private lateinit var headerAdapter: QueueAdapter
    private lateinit var questionAdapter: QuestionAdapter

    private var queueId: Long = Long.MIN_VALUE
    private lateinit var uiConf: UserUiConf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_detail)
        initVars()
        setupListeners()
        setupObservers()
        refreshQueue()
    }

    private fun initVars() {
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this)
            .get(QueueDetailViewModel::class.java)
        queueId = intent.getLongExtra(QUEUE_ID_EXTRA, Long.MIN_VALUE)
        val uuid: Long = InMemCache.INSTANCE[LoginViewModel.USER_ID_KEY]!!

        viewModel.queueId = queueId

        val type: LoginType =
            InMemCache.INSTANCE[LoginViewModel.USER_TYPE_KEY] ?: LoginType.STUDENT
        uiConf = UserUiConf.valueOf(type)
        headerAdapter = QueueAdapter(uiConf)
        queueHeaderList.adapter = headerAdapter
        queueHeaderList.layoutManager = LinearLayoutManager(this)

        questionAdapter = QuestionAdapter(uiConf, uuid, object : Callback<Question> {
            override fun onFailure(call: Call<Question>, t: Throwable) {
                onError(t.message)
            }

            override fun onResponse(call: Call<Question>, response: Response<Question>) {
                val error = response.body()?.error
                if (error != null) {
                    onFailure(call, Exception(error))
                } else {
                    refreshQueue()
                }
            }
        })
        questionListView.adapter = questionAdapter
        questionListView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupListeners() {

        swipeRefresh.setOnRefreshListener(this::refreshQueue)

        if (uiConf.showAddQuestionFab) {
            addQuestionButon.show()
            addQuestionButon.setOnClickListener {
                val newFragment =
                    uiConf.showAddQuestionDialog(queueId, viewModel, Network.uniqueueService)
                        ?: return@setOnClickListener
                openDialogFragment(newFragment)
            }
        }
    }

    private fun setupObservers() {
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
                    updateQuestionList(it.data)
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

        viewModel.addStatus.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    refreshQueue()
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    onError(it.message)
                }
            }
        })
    }

    private fun refreshQueue() {
        viewModel.refresh()
    }

    private fun updateQueueView(q: Queue) {
        headerAdapter.setNewData(Collections.singletonList(q))
    }

    private fun updateQuestionList(questions: List<Question>?) {
        questionAdapter.setNewData(questions?.filter { it.status != "resolved" })
    }

    companion object {
        val QUEUE_ID_EXTRA = "${QueueDetailActivity::class.java.canonicalName}.QUEUE_ID"
    }
}
