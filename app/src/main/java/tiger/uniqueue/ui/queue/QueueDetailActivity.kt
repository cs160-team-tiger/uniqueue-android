package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tiger.uniqueue.R
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.Queue
import tiger.uniqueue.onError
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_detail)
        ButterKnife.bind(this)

        viewModel = ViewModelProviders.of(this)
            .get(QueueDetailViewModel::class.java)

        queueId = intent.getLongExtra(QUEUE_ID_EXTRA, Long.MIN_VALUE)
        fetchQueue()

        headerAdapter = QueueAdapter()
        queueHeaderList.adapter = headerAdapter
        queueHeaderList.layoutManager = LinearLayoutManager(this)

        questionAdapter = QuestionAdapter()
        questionListView.adapter = questionAdapter
        questionListView.layoutManager = LinearLayoutManager(this)

        swipeRefresh.setOnRefreshListener(this::fetchQueue)

        addQuestionButon.setOnClickListener {
            openAddQuestionDialog()
        }

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
                    addQuestionButon.isVisible = true
                    updateQuestionList(it.data!!)
                }
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                    addQuestionButon.isVisible = false
                }
                is Resource.Error -> {
                    swipeRefresh.isRefreshing = false
                    onError(it.message)
                }
            }
        })
    }

    private fun openAddQuestionDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val newFragment =
            AddQuestionDialogFragment.newInstance(queueId)
        newFragment.show(ft, "dialog")
    }

    private fun fetchQueue() {
        if (queueId == Long.MAX_VALUE) {
            onError("Wrong Queue ID")
        } else {
            viewModel.getQueue(queueId)
        }
    }

    private fun updateQueueView(q: Queue) {
        headerAdapter.setNewData(Collections.singletonList(q))
    }

    private fun updateQuestionList(questions: List<Question>) {
        questionAdapter.setNewData(questions)
    }

    class AddQuestionDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater
                with(builder) {
                    setView(inflater.inflate(R.layout.enter_question, null))
                        .setPositiveButton(
                            R.string.action_confirm
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setNegativeButton(
                            R.string.action_cancel
                        ) { dialog, _ ->
                            dialog.cancel()
                        }
                    setOnDismissListener {

                    }
                    setOnCancelListener {

                    }
                }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        companion object {

            fun newInstance(queueId: Long): AddQuestionDialogFragment {
                val dialog = AddQuestionDialogFragment()
                dialog.arguments = Bundle().also {
                    it.putLong(QUEUE_ID_EXTRA, queueId)
                }
                return dialog
            }
        }
    }

    companion object {
        val QUEUE_ID_EXTRA = "${QueueDetailActivity::class.java.canonicalName}.QUEUE_ID"
    }
}
