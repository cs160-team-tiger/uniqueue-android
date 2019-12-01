package tiger.uniqueue.ui.queue

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.UserUiConf
import java.util.*

class QuestionAdapter(private val uiConf: UserUiConf) :
    BaseQuickAdapter<Question, BaseViewHolder>(uiConf.questionLayoutInd, LinkedList()) {

    override fun convert(helper: BaseViewHolder, item: Question?) {
        item ?: return
        helper
            .setText(R.id.position, item.index.toString())
            .setText(R.id.text_inProgress, item.status)
            .setText(R.id.text_question, item.questionText)
            .setText(R.id.student_title, "Student id: ${item.askerUuid}")
    }
}