package tiger.uniqueue.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question extends BaseModel {
    @SerializedName("_id")
    public Long id;

    @SerializedName("answered_uuid")
    public Long answeredUuid;

    @SerializedName("answered_name")
    public String answeredName;

    @SerializedName("asker_uuid")
    public Long askerUuid;

    @SerializedName("asker_name")
    public String askerName;

    @SerializedName("assigned_uuid")
    public Long assignedUuid;

    @SerializedName("assigned_name")
    public String assignedName;

    @SerializedName("creation_time")
    public Long creationTime;

    @SerializedName("question_text")
    public String questionText;

    @SerializedName("queue_id")
    public Long queueId;

    // one of "incomplete", "assigned", "helping", and "resolved"
    @SerializedName("status")
    public String status;

    @SerializedName("question_attachments")
    public List<String> questionAttachment;

    public transient Long index = -1L;
}
