package tiger.uniqueue.ui.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tiger.uniqueue.data.QueueRepository
import tiger.uniqueue.data.model.QueueInfo

class QueueListViewModel(private val repo: QueueRepository) : ViewModel() {

    private val _activeQueues = MutableLiveData<List<QueueInfo>>()
    val activeQueues: LiveData<List<QueueInfo>> = _activeQueues

    private val _selectedQueue = MutableLiveData<QueueInfo?>()
    var selectedQueue: LiveData<QueueInfo?> = _selectedQueue

    fun fetchQueueInfo() {
        repo.refresh()
        _activeQueues.value = repo.queues
    }

    fun selectInfo(info: QueueInfo?) {
        _selectedQueue.value = info
    }
}