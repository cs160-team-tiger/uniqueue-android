package tiger.uniqueue

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(T::class.java)
}

fun <T : Activity> Context.startActivity(clazz: Class<T>) {
    startActivity(Intent(this, clazz))
}