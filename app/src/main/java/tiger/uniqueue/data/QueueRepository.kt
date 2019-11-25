package tiger.uniqueue.data

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tiger.uniqueue.data.model.Queue

class QueueRepository {

    private val _queueLiveData = MutableLiveData<Resource<List<Queue>>>()
    val queueLiveData = _queueLiveData
    fun refresh() {
        Network.uniqueueService.allQueues.enqueue(
            object : Callback<List<Queue>> {
                override fun onFailure(call: Call<List<Queue>>, t: Throwable) {
                    queueLiveData.postValue(Resource.Error(t.message ?: "Network Error"))
                }

                override fun onResponse(call: Call<List<Queue>>, response: Response<List<Queue>>) {
                    queueLiveData.postValue(Resource.Success(response.body()!!))
                }
            }
        )
        queueLiveData.postValue(Resource.Loading())
    }
}