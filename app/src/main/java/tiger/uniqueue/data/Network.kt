package tiger.uniqueue.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    val retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl("https://cs160-uniqueue.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val uniqueueService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        retrofit.create(UniqueueService::class.java)
    }
}