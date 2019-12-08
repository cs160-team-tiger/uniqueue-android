package tiger.uniqueue

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Call
import tiger.uniqueue.data.UniqueueService
import tiger.uniqueue.data.model.Question
import java.io.IOException
import java.io.InputStream


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

// ref: https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server
fun UniqueueService.uploadImage(ctx: Context, id: Long, uri: Uri): Call<Question> {
    val inputStream = ctx.contentResolver.openInputStream(uri)
        ?: throw Exception("Failed to open input stream from uri: $uri")
    val fileReq =
        createRequestBodyFromInputStream(inputStream, MediaType.parse("multipart/form-dataitem")!!)
    val filePart = MultipartBody.Part.createFormData("file", uri.lastPathSegment, fileReq)
    return uploadImage(id, filePart)
}

// ref: https://stackoverflow.com/questions/25367888/upload-binary-file-with-okhttp-from-resources
fun createRequestBodyFromInputStream(inputStream: InputStream, mediaType: MediaType): RequestBody {
    return object : RequestBody() {
        override fun contentType(): MediaType? {
            return mediaType
        }

        override fun contentLength(): Long {
            return try {
                inputStream.available().toLong()
            } catch (e: IOException) {
                super.contentLength()
            }
        }

        override fun writeTo(sink: BufferedSink) {
            Okio.source(inputStream).use {
                sink.writeAll(it)
            }
        }
    }
}