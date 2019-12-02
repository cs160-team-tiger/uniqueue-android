package tiger.uniqueue.ui.queue

import android.widget.LinearLayout
import androidx.core.view.children
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout
import retrofit2.Callback
import tiger.uniqueue.R
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.QuestionAction
import tiger.uniqueue.data.model.UserUiConf
import java.util.*

class QuestionAdapter(
    private val uiConf: UserUiConf,
    private val uuid: Long,
    private val questionActionCallback: Callback<Question>
) :
    BaseQuickAdapter<Question, BaseViewHolder>(uiConf.questionLayoutInd, LinkedList()) {

    override fun convert(helper: BaseViewHolder, item: Question?) {
        item ?: return
        helper
            .setText(R.id.position, item.index.toString())
            .setText(R.id.text_inProgress, item.status)
            .setText(R.id.text_question, item.questionText)
            .setText(R.id.student_title, "Student id: ${item.askerUuid}")
        if (uiConf.questionSwipable) {
            val menu = helper.getView<LinearLayout>(R.id.menu_swipe)
            val menuLayout = helper.getView<EasySwipeMenuLayout>(R.id.swipe_menu_layout)
            menu.children.iterator().forEach {
                it.setOnClickListener {
                    QuestionAction.valueOf(it.tag as String).markToBackend(item.id, uuid)
                        .enqueue(questionActionCallback)
                    menuLayout.resetStatus()
                }
            }
        }
    }
}