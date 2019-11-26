package tiger.uniqueue.ui.queue

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import tiger.uniqueue.data.model.Question
import java.util.*

class QuestionAdapter :
    BaseQuickAdapter<Question, BaseViewHolder>(R.layout.posi_info, LinkedList()) {

    override fun convert(helper: BaseViewHolder, item: Question?) {
        item ?: return
        helper
            .setText(R.id.position, item.index.toString())
            //TODO: remove hard code?
            .setVisible(R.id.text_inProgress, true)
            .setText(R.id.text_question, item.questionText)
            .setText(R.id.student_title, "Student id: ${item.askerUuid}")
    }
}