package tiger.uniqueue.ui.queue

import androidx.lifecycle.LiveData
import tiger.uniqueue.data.Resource

interface IAddStatus {
    val addStatus: LiveData<Resource<String>>
    fun updateStatus(status: Resource<String>)
}