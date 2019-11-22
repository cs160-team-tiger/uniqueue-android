package tiger.uniqueue.data

import tiger.uniqueue.data.model.QueueInfo
import java.time.Duration
import java.time.LocalDateTime

class QueueRepository {
    private var _queues = mutableListOf<QueueInfo>()
    val queues: List<QueueInfo>
        get() = _queues

    fun refresh() {
        _queues = mutableListOf(
            QueueInfo(
                1,
                "CS61A - AAA's OH",
                "Cory Hall 241",
                LocalDateTime.of(2019, 11, 21, 11, 58),
                11,
                Duration.ofMinutes(4)
            ), QueueInfo(
                1,
                "CS61A - BBB's OH",
                "Cory Hall 241",
                LocalDateTime.of(2019, 11, 21, 17, 58),
                5,
                Duration.ofMinutes(12)
            ), QueueInfo(
                1,
                "CS61A - CCC's OH",
                "Cory Hall 241",
                LocalDateTime.of(2019, 11, 21, 14, 58),
                34,
                Duration.ofMinutes(76)
            ), QueueInfo(
                1,
                "CS61A - DDD's OH",
                "Cory Hall 241",
                LocalDateTime.of(2019, 11, 21, 10, 58),
                3,
                Duration.ofMinutes(2)
            )
        )
    }

    fun add(info: QueueInfo) {
        _queues.add(info)
    }

    fun remove(info: QueueInfo) {
        _queues.removeIf { it.id == info.id }
    }
}