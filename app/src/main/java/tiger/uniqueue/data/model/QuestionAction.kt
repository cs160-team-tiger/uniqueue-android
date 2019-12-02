package tiger.uniqueue.data.model

import retrofit2.Call
import tiger.uniqueue.data.Network

enum class QuestionAction {
    ASSIGN {
        override fun markToBackend(qId: Long, instructorId: Long): Call<Question> =
            Network.uniqueueService.assign(qId, instructorId)

    },
    HELPING {
        override fun markToBackend(qId: Long, instructorId: Long): Call<Question> =
            Network.uniqueueService.markHelping(qId, instructorId)
    },
    RESOLVED {
        override fun markToBackend(qId: Long, instructorId: Long): Call<Question> =
            Network.uniqueueService.markResolved(qId, instructorId)
    };

    abstract fun markToBackend(qId: Long, instructorId: Long): Call<Question>
}