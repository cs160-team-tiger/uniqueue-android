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

class QueueDetailViewModel() : ViewModel(), IRefreshable, IAddStatus {
    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>> = _questions

    private val _queue = MutableLiveData<Resource<Queue>>()
    val queue: LiveData<Resource<Queue>> = _queue

    private val _addStatus = MutableLiveData<Resource<String>>()
    override val addStatus: LiveData<Resource<String>> = _addStatus

    var queueId: Long = Long.MIN_VALUE

    override fun updateStatus(status: Resource<String>) {
        _addStatus.postValue(status)
    }

    override fun refresh() {
        _queue.value = Resource.Loading()
        if (queueId == Long.MIN_VALUE) {
            _queue.value = Resource.Error("queueId not init")
            return
        }
        val disposable = Network.uniqueueService
            .getQueueByIdRx(queueId)
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
        val questionIds =
            queue.questionIds

        var index = 1L
        val disposable = Flowable.fromIterable(questionIds)
            .concatMap {
                Network.uniqueueService.getQuestionByIdRx(it).toFlowable()
            }
            .doOnNext { t ->
                t.index = index++
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