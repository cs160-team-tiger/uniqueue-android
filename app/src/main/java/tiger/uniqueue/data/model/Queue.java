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
    @SerializedName("motd")
    public String motd;
    @SerializedName("question_ids")
    public List<Long> questionIds;
    @SerializedName("start_time")
    public Long startTime;
}
