package tiger.uniqueue.data;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
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

    @POST("questions/assigninstructor")
    Call<Question> assign(@Query("id") long id, @Query("instructor_uuid") long instUuid);

    @POST("questions/markhelping")
    Call<Question> markHelping(@Query("id") long id, @Query("instructor_uuid") long instUuid);

    @POST("questions/markresolved")
    Call<Question> markResolved(@Query("id") long id, @Query("instructor_uuid") long instUuid);

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

    @POST("queue/offer")
    @Multipart
    Call<OfferResponse> offerQueue(@Part("queue_id") long queueId, @Part("asker_uuid") long uuid,
                                   @Part("question_text") String questionText);

    @POST("queue/create")
    @Multipart
    Call<Queue> createQueue(
            @Part("queue_name") String queueName,
            @Part("instructor_id") long instrId,
            @Part("location_name") String locationName,
            @Part("is_open") boolean isOpen,
            @Part("motd") String motd,
            @Part("location_latitude") double lat,
            @Part("location_longitude") double lng
    );

    @POST("questions/uploadimage")
    @Multipart
    Call<Question> uploadImage(
            @Query("id") long id,
            @Part MultipartBody.Part file
    );
}
