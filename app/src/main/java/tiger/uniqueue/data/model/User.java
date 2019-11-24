package tiger.uniqueue.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User extends BaseModel {
    @SerializedName("_uuid")
    public Long uuid;
    @SerializedName("email")
    public String Email;
    @SerializedName("name")
    public String name;
    @SerializedName("asked_question_ids")
    public List<Long> questionIds;
}
