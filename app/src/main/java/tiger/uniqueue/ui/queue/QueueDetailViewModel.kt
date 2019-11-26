package tiger.uniqueue.ui.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import tiger.uniqueue.data.Network
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Question
import tiger.uniqueue.data.model.Queue
import java.util.*

class QueueDetailViewModel : ViewModel() {
    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>> = _questions

    private val _queue = MutableLiveData<Resource<Queue>>()
    val queue: LiveData<Resource<Queue>> = _queue

    internal val _questionStatus = MutableLiveData<Resource<String>>()
    val questionStatus: LiveData<Resource<String>> = _questionStatus

    fun getQueue(id: Long) {
        _queue.value = Resource.Loading()
        val disposable = Network.uniqueueService
            .getQueueByIdRx(id)
            .doOnSuccess(this::loadQuestion)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _queue.postValue(Resource.Success(it))
            },
                { _queue.postValue(Resource.Error(it?.message ?: "Network Error")) }
            )
    }

    private fun loadQuestion(queue: Queue) {
        _questions.postValue(Resource.Loading())
        //        TODO: remove this debug
        val questionIds = if (queue.questionIds.isEmpty()) {
            Collections.singletonList(101L)
        } else {
            queue.questionIds
        }
        var index = 0L
        val disposable = Flowable.fromIterable(questionIds)
            .concatMap {
                Network.uniqueueService.getQuestionByIdRx(it).toFlowable()
            }
            .doOnEach { t ->
                t.value?.index = index++
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _questions.postValue(Resource.Success(it))
            }, {
                _questions.postValue(Resource.Error(it?.message ?: "Network Error"))
            })
    }
}