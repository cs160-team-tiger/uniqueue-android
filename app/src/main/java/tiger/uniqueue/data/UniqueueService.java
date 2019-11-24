package tiger.uniqueue.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tiger.uniqueue.data.model.Question;
import tiger.uniqueue.data.model.Queue;
import tiger.uniqueue.data.model.User;

public interface UniqueueService {
    @GET("users/fetchall")
    Call<List<User>> getAllUsers();

    @GET("users/fetchbyuuid")
    Call<User> getUserByUUID(@Query("uuid") long uuid);

    @GET("users/fetchbyemail")
    Call<User> getUserByEmail(@Query("email") String email);

    @GET("questions/fetchall")
    Call<List<Question>> getAllQuestions();

    @GET("questions/fetchallids")
    Call<List<Long>> getAllQuestionIds();

    @GET("questions/fetchbyid")
    Call<Question> getQuestionById(@Query("id") long id);

    @GET("queue/fetchall")
    Call<List<Queue>> getAllQueues();

    @GET("queue/fetchbyid")
    Call<Queue> getQueueById(@Query("id") long uuid);

    @GET("queue/peek")
    Call<Long> peekQueue(@Query("id") long uuid);
}
