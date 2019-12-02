package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.R
import tiger.uniqueue.createQueue
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.UniqueueService
import tiger.uniqueue.data.model.Queue
import tiger.uniqueue.ui.login.LoginViewModel

class CreateQueueDialogFragment(
    statusUpdater: IAddStatus,
    networkService: UniqueueService
) :
    AddOperationDialogFragment(statusUpdater, networkService) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            with(builder) {
                val view = inflater.inflate(R.layout.create_queue, null)
                val etQueueName = view.findViewById<EditText>(
                    R.id.queueName
                )
                val etLocation = view.findViewById<EditText>(
                    R.id.location
                )
                val instrId: Long = InMemCache.INSTANCE[LoginViewModel.USER_ID_KEY]!!
                setView(view)
                    .setPositiveButton(
                        R.string.action_confirm
                    ) { dialog, _ ->
                        dialog.dismiss()
                        networkService.createQueue(
                            etQueueName.text.toString(),
                            instrId,
                            etLocation.text.toString()
                        ).enqueue(object : Callback<Queue> {
                            override fun onFailure(call: Call<Queue>, t: Throwable) {
                                statusUpdater.updateStatus(
                                    Resource.Error(
                                        t.message ?: "Network error"
                                    )
                                )
                            }

                            override fun onResponse(call: Call<Queue>, response: Response<Queue>) {
                                val error = response.body()?.error
                                if (error != null) {
                                    onFailure(call, Exception(error))
                                    return
                                }
                                statusUpdater.updateStatus(Resource.Success("Sent"))
                            }
                        })
                        statusUpdater.updateStatus(Resource.Loading())
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

        fun newInstance(
            queueListViewModel: IAddStatus,
            networkService: UniqueueService
        ): CreateQueueDialogFragment {
            val dialog =
                CreateQueueDialogFragment(queueListViewModel, networkService)
            dialog.arguments = Bundle().also {
            }
            return dialog
        }
    }
}