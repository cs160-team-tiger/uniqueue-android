package tiger.uniqueue.ui.queue

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import tiger.uniqueue.R
import tiger.uniqueue.data.UniqueueService

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
                val editTextName = view.findViewById<EditText>(
                    R.id.queueName
                )
                val editTextLocation = view.findViewById<EditText>(
                    R.id.location
                )
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