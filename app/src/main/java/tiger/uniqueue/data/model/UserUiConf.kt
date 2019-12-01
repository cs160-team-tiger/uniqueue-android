package tiger.uniqueue.data.model

import androidx.annotation.LayoutRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tiger.uniqueue.R
import tiger.uniqueue.data.LoginType

enum class UserUiConf(@LayoutRes val questionLayoutInd: Int) {
    STUDENT(R.layout.posi_info) {
        override val shouldShowQueueMenu: Boolean = false


    },
    INSTRUCTOR(R.layout.swipable_question_info) {
        override val questionSwipable: Boolean = true
        override fun processAddQuestionFab(fab: FloatingActionButton) {
            fab.hide()
        }
    };

    open val shouldShowQueueMenu: Boolean = true
    open fun processAddQuestionFab(fab: FloatingActionButton) {}
    open val questionSwipable: Boolean = false

    companion object {
        fun valueOf(type: LoginType) =
            type.uiConf
    }
}