package tiger.uniqueue.data.model

import com.google.android.material.floatingactionbutton.FloatingActionButton
import tiger.uniqueue.data.LoginType

enum class UserUiConf {
    STUDENT {
        override val shouldShowQueueMenu: Boolean = false

    },
    INSTRUCTOR {
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