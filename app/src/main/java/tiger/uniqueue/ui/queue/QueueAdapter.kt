package tiger.uniqueue.ui.queue

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import tiger.uniqueue.data.LoginType
import tiger.uniqueue.data.model.Queue
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class QueueAdapter(private val type: LoginType) :
    BaseQuickAdapter<Queue, BaseViewHolder>(
        R.layout.q_info,
        LinkedList()
    ) {
    override fun convert(helper: BaseViewHolder, item: Queue) {
        with(helper) {
            setText(R.id.title, item.queueName)
            val timeStr =
                LocalDateTime.ofEpochSecond(item.startTime, 0, OffsetDateTime.now().offset)
                    .truncatedTo(ChronoUnit.MINUTES).toString()
            setText(R.id.locationAndTime, "${item.locationName} (Started in $timeStr)")
//            TODO: remove hardcode
            setText(R.id.position, item.questionIds.size.toString())
            setText(R.id.waitingTime, (item.questionIds.size * 3).toString())
            setGone(R.id.menu_header, type.shouldShowQueueMenu)
        }
    }
}