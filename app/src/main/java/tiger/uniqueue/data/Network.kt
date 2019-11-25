package tiger.uniqueue.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    val retrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl("https://cs160-uniqueue.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }
    val uniqueueService: UniqueueService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        retrofit.create(UniqueueService::class.java)
    }
}