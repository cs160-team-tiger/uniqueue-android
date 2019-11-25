package tiger.uniqueue.ui.queue

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import tiger.uniqueue.R
import tiger.uniqueue.data.model.Queue
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class QueueAdapter :
    BaseQuickAdapter<Queue, BaseViewHolder>(
        R.layout.q_info,
        LinkedList()
    ) {
    override fun convert(helper: BaseViewHolder, item: Queue) {
        with(helper) {
            setText(R.id.title, "Queue at ${item.location}")
            val timeStr =
                LocalDateTime.ofEpochSecond(item.startTime, 0, OffsetDateTime.now().offset)
                    .truncatedTo(ChronoUnit.MINUTES).toString()
            setText(R.id.locationAndTime, "${item.location} (Started in $timeStr)")
//            TODO: remove hardcode
            setText(R.id.position, "3")
            setText(R.id.waitingTime, "5")
        }
    }
}