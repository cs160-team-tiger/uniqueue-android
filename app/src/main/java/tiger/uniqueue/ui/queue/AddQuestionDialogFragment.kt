package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.UniqueueService
import tiger.uniqueue.data.model.OfferResponse
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.ui.login.LoginViewModel
import tiger.uniqueue.uploadImage


class AddQuestionDialogFragment(
    statusUpdater: IAddStatus,
    networkService: UniqueueService
) :
    AddOperationDialogFragment(statusUpdater, networkService) {
    private var imgUri: Uri? = null
    @BindView(R.id.et_question)
    lateinit var editText: TextInputEditText
    @BindView(R.id.image_button)
    lateinit var pickImgButton: Button
    @BindView(R.id.iv_preview)
    lateinit var ivPreview: ImageView

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_PICK_IMG -> {
                onImgPicked(data?.data)
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun onImgPicked(uri: Uri?) {
        imgUri = uri
        Glide.with(ivPreview)
            .load(imgUri)
            .centerInside()
            .into(ivPreview)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val queueId = arguments?.getLong(QueueDetailActivity.QUEUE_ID_EXTRA)
            with(builder) {
                val view = inflater.inflate(R.layout.enter_question, null)
                ButterKnife.bind(this@AddQuestionDialogFragment, view)
                pickImgButton.setOnClickListener {
                    var intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    intent = Intent.createChooser(intent, "Pick a image")
                    val activities: List<ResolveInfo> =
                        activity.packageManager.queryIntentActivities(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    val isIntentSafe: Boolean = activities.isNotEmpty()
                    if (!isIntentSafe) {
                        Toast.makeText(activity, "No img picker available.", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    startActivityForResult(intent, REQ_PICK_IMG)
                }

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
                        val callback = AddQuestionCallback(activity, statusUpdater, networkService)

                        networkService
                            .offerQueue(queueId, userId, editText.text.toString())
                            .enqueue(callback)
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

    inner class AddQuestionCallback(
        private val context: Context,
        private val statusUpdater: IAddStatus,
        private val networkService: UniqueueService
    ) : Callback<OfferResponse> {
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
                    Exception("Network error with code: ${response.code()}")
                )
                return
            }

            val status = response.body()?.status ?: false
            val uri = imgUri
            if (!status) {
//                fail to add question
                statusUpdater.updateStatus(
                    Resource.Error(
                        response.body()?.error ?: "Error raised when adding question"
                    )
                )
            } else if (uri == null) {
                // question added and no img, good to go
                statusUpdater.updateStatus(
                    Resource.Success(
                        "Body omitted"
                    )
                )
            } else {
                // question added and img to upload
                val questionId = response.body()?.question?.id!!
                networkService
                    .uploadImage(context, questionId, uri)
                    .enqueue(object : Callback<Question> {
                        //                        dummy
                        override fun onFailure(call: Call<Question>, t: Throwable) {
                            statusUpdater.updateStatus(
                                Resource.Error(
                                    "Failed to upload img"
                                )
                            )
                        }

                        override fun onResponse(
                            call: Call<Question>,
                            response: Response<Question>
                        ) {
                            statusUpdater.updateStatus(
                                Resource.Success(
                                    "Image uploaded"
                                )
                            )
                        }
                    })
            }
        }
    }

    companion object {

        private const val REQ_PICK_IMG = 100

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