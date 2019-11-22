package tiger.uniqueue.ui.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tiger.uniqueue.data.LoginDataSource
import tiger.uniqueue.data.LoginRepository
import tiger.uniqueue.data.QueueRepository
import tiger.uniqueue.ui.login.LoginViewModel

class QueueListViewModelFactory: ViewModelProvider.Factory  {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(
                QueueListViewModel::class.java)) {
            return QueueListViewModel(
                QueueRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}