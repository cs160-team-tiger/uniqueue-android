package tiger.uniqueue.data.model

import tiger.uniqueue.data.LoginType

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val type: LoginType
)
