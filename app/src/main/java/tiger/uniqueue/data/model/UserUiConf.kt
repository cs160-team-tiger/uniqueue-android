package tiger.uniqueue.data.model

import androidx.annotation.LayoutRes
import tiger.uniqueue.R
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.data.UniqueueService
import tiger.uniqueue.ui.queue.AddOperationDialogFragment
import tiger.uniqueue.ui.queue.AddQuestionDialogFragment
import tiger.uniqueue.ui.queue.CreateQueueDialogFragment
import tiger.uniqueue.ui.queue.IAddStatus

enum class UserUiConf(@LayoutRes val questionLayoutInd: Int) {
    STUDENT(R.layout.question_item) {
        override val showAddQuestionFab: Boolean = true
        override fun showAddQuestionDialog(
            queueId: Long,
            statusUpdater: IAddStatus,
            networkService: UniqueueService
        ): AddOperationDialogFragment? =
            AddQuestionDialogFragment.newInstance(queueId, statusUpdater, networkService)
    },
    INSTRUCTOR(R.layout.swipable_question_info) {
        override val questionSwipable: Boolean = true
        override val showAddQueueFab: Boolean = true
        override val showQueueMenu: Boolean = true
        override fun showAddQueueDialog(
            statusUpdater: IAddStatus,
            networkService: UniqueueService
        ): AddOperationDialogFragment? =
            CreateQueueDialogFragment.newInstance(statusUpdater, networkService)

    };

    open val showQueueMenu: Boolean = false
    open val showAddQuestionFab: Boolean = false
    open val questionSwipable: Boolean = false
    open val showAddQueueFab: Boolean = false
    open fun showAddQueueDialog(
        statusUpdater: IAddStatus,
        networkService: UniqueueService
    ): AddOperationDialogFragment? = null

    open fun showAddQuestionDialog(
        queueId: Long,
        statusUpdater: IAddStatus,
        networkService: UniqueueService
    ): AddOperationDialogFragment? = null

    companion object {
        fun valueOf(type: LoginType) =
            type.uiConf
    }
}