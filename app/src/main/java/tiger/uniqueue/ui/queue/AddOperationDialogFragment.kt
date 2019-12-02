package tiger.uniqueue.ui.queue

import androidx.fragment.app.DialogFragment
import tiger.uniqueue.data.UniqueueService

abstract class AddOperationDialogFragment(
    protected val statusUpdater: IAddStatus,
    protected val networkService: UniqueueService
) : DialogFragment()