package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.UniqueueService
import tiger.uniqueue.data.model.OfferResponse
import tiger.uniqueue.ui.login.LoginViewModel

class AddQuestionDialogFragment(
    statusUpdater: IAddStatus,
    networkService: UniqueueService
) :
    AddOperationDialogFragment(statusUpdater, networkService) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val queueId = arguments?.getLong(QueueDetailActivity.QUEUE_ID_EXTRA)
            with(builder) {
                val view = inflater.inflate(R.layout.enter_question, null)
                val editText = view.findViewById<TextInputEditText>(
                    R.id.et_question
                )
                // TODO modify text
//                setText(R.id.join_queue_position, )
//                setText(R.id.time, )
                setView(view)
                    .setPositiveButton(
                        R.string.action_confirm
                    ) { dialog, _ ->
                        dialog.dismiss()
                        val userId: Long? =
                            InMemCache.INSTANCE[LoginViewModel.USER_ID_KEY]
                        if (queueId == null || userId == null) {
                            statusUpdater.updateStatus(
                                Resource.Error(
                                    "Wrong queueId or userId"
                                )
                            )
                            return@setPositiveButton
                        }
                        statusUpdater.updateStatus(
                            Resource.Loading()
                        )
                        networkService
                            .offerQueue(queueId, userId, editText.text.toString())
                            .enqueue(object :
                                Callback<OfferResponse> {
                                override fun onFailure(
                                    call: Call<OfferResponse>,
                                    t: Throwable
                                ) {
                                    statusUpdater.updateStatus(
                                        Resource.Error(
                                            t.message ?: "Network error"
                                        )
                                    )
                                }

                                override fun onResponse(
                                    call: Call<OfferResponse>,
                                    response: Response<OfferResponse>
                                ) {
                                    if (!response.isSuccessful) {
                                        onFailure(
                                            call,
                                            java.lang.Exception("Network error with code: ${response.code()}")
                                        )
                                        return
                                    }

                                    val status = response.body()?.status ?: false
                                    statusUpdater.updateStatus(
                                        if (status) {
                                            Resource.Success(
                                                "Body omitted"
                                            )
                                        } else {
                                            Resource.Error(
                                                "Operation failed."
                                            )
                                        }

                                    )
                                }
                            })
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
            queueId: Long,
            viewModel: IAddStatus,
            networkService: UniqueueService
        ): AddQuestionDialogFragment {
            val dialog =
                AddQuestionDialogFragment(viewModel, networkService)
            dialog.arguments = Bundle().also {
                it.putLong(QueueDetailActivity.QUEUE_ID_EXTRA, queueId)
            }
            return dialog
        }
    }
}