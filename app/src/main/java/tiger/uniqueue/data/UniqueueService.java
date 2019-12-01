package tiger.uniqueue.data;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import tiger.uniqueue.data.model.OfferResponse;
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

    @GET("questions/fetchbyid")
    Single<Question> getQuestionByIdRx(@Query("id") long id);

    @GET("queue/fetchall")
    Call<List<Queue>> getAllQueues();

    @GET("queue/fetchbyid")
    Call<Queue> getQueueById(@Query("id") long uuid);

    @GET("queue/fetchbyid")
    Single<Queue> getQueueByIdRx(@Query("id") long uuid);

    @GET("queue/peek")
    Call<Long> peekQueue(@Query("id") long uuid);

    @Multipart
    @POST("queue/offer")
    Call<OfferResponse> offerQueue(@Part("queue_id") long queueId, @Part("asker_uuid") long uuid,
                                   @Part("question_text") String questionText);

    // TODO @POST("") create queue
//    Call<ResponseBody> createQueue(@Part("queue_id") long queueId, @Part("queue_name") String queueName,
//                                  @Part("instructor_id") long instrId, @Part("location_name") String locationName,
//                                   @Part("start_time") String startTime);
}
