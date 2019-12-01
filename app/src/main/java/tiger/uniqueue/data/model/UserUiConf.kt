package tiger.uniqueue.data.model

import androidx.annotation.LayoutRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tiger.uniqueue.R
import tiger.uniqueue.data.LoginType

enum class UserUiConf(@LayoutRes val questionLayoutInd: Int) {
    STUDENT(R.layout.posi_info) {
    },
    INSTRUCTOR(R.layout.swipable_question_info) {
        override val questionSwipable: Boolean = true
        override val showAddQueueFab: Boolean = true
        override val showQueueMenu: Boolean = true
        override fun processAddQuestionFab(fab: FloatingActionButton) {
            fab.hide()
        }
    };

    open val showQueueMenu: Boolean = false
    open fun processAddQuestionFab(fab: FloatingActionButton) {}
    open val questionSwipable: Boolean = false
    open val showAddQueueFab: Boolean = false

    companion object {
        fun valueOf(type: LoginType) =
            type.uiConf
    }
}