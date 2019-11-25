package tiger.uniqueue

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.material.snackbar.Snackbar


inline fun <reified T : Activity> Context.startActivity(noinline intentInitializer: ((Intent) -> Unit)? = null) {
    startActivity(T::class.java, intentInitializer)
}

fun <T : Activity> Context.startActivity(
    clazz: Class<T>,
    intentInitializer: ((Intent) -> Unit)? = null
) {
    val intent = Intent(this, clazz)
    intentInitializer?.invoke(intent)
    startActivity(intent)
}

fun Activity.onError(msg: CharSequence?, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(window.decorView, msg ?: "Some error occurred", length).show()
}