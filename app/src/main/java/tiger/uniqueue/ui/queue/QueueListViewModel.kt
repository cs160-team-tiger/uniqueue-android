package tiger.uniqueue.ui.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tiger.uniqueue.data.QueueRepository
import tiger.uniqueue.data.model.Queue

class QueueListViewModel(private val repo: QueueRepository) : ViewModel() {

    val activeQueues = repo.queueLiveData

    private val _selectedQueue = MutableLiveData<Queue>()
    var selectedQueue: LiveData<Queue> = _selectedQueue

    fun fetchQueueInfo() {
        repo.refresh()
    }

    fun selectInfo(info: Queue?) {
        _selectedQueue.value = info
    }
}