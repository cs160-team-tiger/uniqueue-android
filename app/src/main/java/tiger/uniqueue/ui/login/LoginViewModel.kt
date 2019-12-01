package tiger.uniqueue.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.LoginRepository
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String, type: LoginType) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password, type)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        displayName = result.data.displayName,
                        type = result.data.type
                    )
                )
            InMemCache.INSTANCE[USER_ID_KEY] = username.toLongOrNull()
            InMemCache.INSTANCE[USER_TYPE_KEY] = type
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
            InMemCache.INSTANCE[USER_ID_KEY] = null
            InMemCache.INSTANCE[USER_TYPE_KEY] = null
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank() && username.toLongOrNull() != null
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    companion object {
        val USER_ID_KEY = "${LoginViewModel::class.java.canonicalName}.userId"
        val USER_TYPE_KEY = "${LoginViewModel::class.java.canonicalName}.useType"
    }
}
