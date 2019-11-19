package tiger.uniqueue

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import tiger.uniqueue.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val handler = Handler()
//        wait for 500ms
        handler.postDelayed({
            navigateToNext()
        }, 500)
    }

    private fun navigateToNext() {
        lateinit var activityClazz: Class<out Activity>
        if (checkLogin()) {
            activityClazz = QueueListActivity::class.java
        } else {
            activityClazz = LoginActivity::class.java
        }
        val intent = Intent(this, activityClazz)
        startActivity(intent)
    }

    fun checkLogin(): Boolean {
        return false
    }
}
