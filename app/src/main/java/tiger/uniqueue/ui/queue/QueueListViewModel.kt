package tiger.uniqueue.ui.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tiger.uniqueue.data.QueueRepository
import tiger.uniqueue.data.Resource
import tiger.uniqueue.data.model.Queue

class QueueListViewModel(private val repo: QueueRepository) : ViewModel(), IRefreshable,
    IAddStatus {

    val activeQueues = repo.queueLiveData

    private val _selectedQueue = MutableLiveData<Queue>()
    var selectedQueue: LiveData<Queue> = _selectedQueue

    private val _addStatus = MutableLiveData<Resource<String>>()
    override val addStatus: LiveData<Resource<String>> = _addStatus
    override fun updateStatus(status: Resource<String>) {
        _addStatus.postValue(status)
    }

    override fun refresh() {
        repo.refresh()
    }

    fun selectInfo(info: Queue?) {
        _selectedQueue.value = info
    }
}