package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textfield.TextInputEditText
import tiger.uniqueue.R

import kotlinx.android.synthetic.main.activity_instr_queue_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.Network
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.BaseModel
import tiger.uniqueue.onError
import tiger.uniqueue.startActivity
import tiger.uniqueue.ui.login.LoginViewModel
import tiger.uniqueue.unserialize

class InstrQueueListActivity : AppCompatActivity() {

    @BindView(R.id.rv_queue_list)
    lateinit var queueList: RecyclerView
    @BindView(R.id.swiperefresh)
    lateinit var swipeLayout: SwipeRefreshLayout
    @BindView(R.id.create_queue)
    lateinit var createQueueButton: Button

    private lateinit var viewModel: QueueListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instr_queue_list)
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

        swipeLayout.setOnRefreshListener {
            refreshData()
        }

        createQueueButton.setOnClickListener {
            openCreateQueueDialog()
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

    private fun openCreateQueueDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        val newFragment =
            CreateQueueDialogFragment.newInstance()
        newFragment.show(ft, "dialog")
    }

    class CreateQueueDialogFragment :
        DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                val inflater = requireActivity().layoutInflater
                with(builder) {
                    val view = inflater.inflate(R.layout.create_queue, null)
                    val editTextName = view.findViewById<EditText>(R.id.queueName)
                    val editTextLocation = view.findViewById<EditText>(R.id.location)
                    setView(view)
                        .setPositiveButton(
                            R.string.action_confirm
                        ) { dialog, _ ->
                            dialog.dismiss()
                        // TODO
//                            Network.uniqueueService
//                                .createQueue(queueId, editTextName.text.toString(), instructor_id, editTextLocation.text.toString(), start_time)

                        }
                        .setNegativeButton(
                            R.string.action_cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }

                }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }

        companion object {

            fun newInstance(): CreateQueueDialogFragment {
                val dialog = CreateQueueDialogFragment()
                dialog.arguments = Bundle().also {
                }
                return dialog
            }
        }
    }
}
