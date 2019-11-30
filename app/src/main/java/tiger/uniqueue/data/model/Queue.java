package tiger.uniqueue.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Queue extends BaseModel {
    @SerializedName("_id")
    public Long id;

    @SerializedName("instructor_id")
    public Long instructorId;

    @SerializedName("is_open")
    public Boolean isOpen;

    @SerializedName("location")
    public String location;

    @SerializedName("location_longitude")
    public Double locationLongitude;

    @SerializedName("location_latitude")
    public Double locationLatitude;

    @SerializedName("motd")
    public String messageOfTheDay;

    @SerializedName("question_ids")
    public List<Long> questionIds;

    @SerializedName("start_time")
    public Long startTime;

    @SerializedName("queue_name")
    public String queueName;
}
