package tiger.uniqueue.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    val retrofit: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl("https://cs160-uniqueue.herokuapp.com/")
            .client(
                OkHttpClient.Builder()
                    .cache(null)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(tiger.uniqueue.gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }
    val uniqueueService: UniqueueService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        retrofit.create(UniqueueService::class.java)
    }
}