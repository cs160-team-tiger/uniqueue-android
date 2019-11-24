package tiger.uniqueue.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Queue extends BaseModel {
    @SerializedName("_id")
    Long id;
    @SerializedName("instructor_id")
    Long instructorId;
    @SerializedName("is_open")
    Boolean isOpen;
    @SerializedName("location")
    String location;
    @SerializedName("motd")
    String motd;
    @SerializedName("question_ids")
    List<Long> questionIds;
    @SerializedName("start_time")
    Long startTime;
}
