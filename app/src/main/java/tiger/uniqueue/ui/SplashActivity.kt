package tiger.uniqueue.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import tiger.uniqueue.R
import tiger.uniqueue.data.InMemCache
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.startActivity
import tiger.uniqueue.ui.login.LoginActivity
import tiger.uniqueue.ui.login.LoginViewModel
import tiger.uniqueue.ui.queue.QueueListActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler()
//        wait for 500ms
        handler.postDelayed({
            navigateToNext()
            finish()
        }, 1000)
    }

    private fun navigateToNext() {
        val activityClazz: Class<out Activity> = when (checkLogin()) {
            LoginType.STUDENT -> QueueListActivity::class.java
            LoginType.INSTRUCTOR -> QueueListActivity::class.java
            null -> LoginActivity::class.java
        }
        startActivity(activityClazz)
    }

    private fun checkLogin(): LoginType? =
        InMemCache.INSTANCE[LoginViewModel.USER_TYPE_KEY]

}
