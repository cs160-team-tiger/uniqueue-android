package tiger.uniqueue.data.model

import java.time.LocalDateTime
import kotlin.time.Duration

data class QueueInfo(
    val id: Long,
    val title: String,
    val location: String,
    val time: LocalDateTime,
    val inQueueNum: Int,
    val waitTime: java.time.Duration
) {
}