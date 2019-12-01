package tiger.uniqueue.data.model

import tiger.uniqueue.data.LoginType

enum class UserUiConf {
    STUDENT {
        override val shouldShowFab: Boolean
            get() = true
        override val shouldShowQueueMenu: Boolean
            get() = false
    },
    INSTRUCTOR {
        override val shouldShowFab: Boolean
            get() = false
        override val shouldShowQueueMenu: Boolean
            get() = true
    };

    abstract val shouldShowQueueMenu: Boolean
    abstract val shouldShowFab: Boolean

    companion object {
        fun valueOf(type: LoginType) =
            when (type) {
                LoginType.INSTRUCTOR -> INSTRUCTOR
                LoginType.STUDENT -> STUDENT
            }
    }
}