package tiger.uniqueue.data

enum class LoginType {
    STUDENT {
        override val shouldShowQueueMenu: Boolean
            get() = false
    },
    INSTRUCTOR {
        override val shouldShowQueueMenu: Boolean
            get() = true
    };

    abstract val shouldShowQueueMenu: Boolean
}