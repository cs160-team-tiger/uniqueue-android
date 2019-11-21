package tiger.uniqueue.ui.login

import tiger.uniqueue.data.LoginType

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val type: LoginType
    //... other data fields that may be accessible to the UI
)
