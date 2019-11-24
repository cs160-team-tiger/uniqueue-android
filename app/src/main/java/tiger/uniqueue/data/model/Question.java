package tiger.uniqueue.data.model;

import com.google.gson.annotations.SerializedName;

public class Question extends BaseModel {
    @SerializedName("_id")
    public Long id;
    @SerializedName("answered_uuid")
    public Long answeredUuid;
    @SerializedName("asker_uuid")
    public Long askerUuid;
    @SerializedName("assigned_uuid")
    public Long assignedUuid;
    @SerializedName("creation_time")
    public Long creationTime;
    @SerializedName("question_text")
    public String questionText;
    @SerializedName("queue_id")
    public Long queueId;
}
