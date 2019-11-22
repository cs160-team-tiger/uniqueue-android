package tiger.uniqueue

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(T::class.java)
}

fun <T : Activity> Context.startActivity(clazz: Class<T>) {
    startActivity(clazz, null)
}

fun <T : Activity> Context.startActivity(
    clazz: Class<T>,
    intentInitializer: ((Intent) -> Unit)? = null
) {
    val intent = Intent(this, clazz)
    intentInitializer?.invoke(intent)
    startActivity(intent)
}