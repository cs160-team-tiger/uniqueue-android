package tiger.uniqueue

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import tiger.uniqueue.data.UniqueueService


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

val gson = Gson()
inline fun <reified T> unserialize(str: String?): T {
    return gson.fromJson<T>(str, T::class.java)
}

fun AppCompatActivity.openDialogFragment(newFragment: DialogFragment) {
    val ft = supportFragmentManager.beginTransaction()
    val prev = supportFragmentManager.findFragmentByTag("dialog")
    if (prev != null) {
        ft.remove(prev)
    }
    ft.addToBackStack(null)
    newFragment.show(ft, "dialog")
}

fun UniqueueService.createQueue(
    queueName: String,
    instrId: Long,
    locationName: String
) =
    createQueue(
        queueName, instrId, locationName, true, "", 0.0, 0.0
    )