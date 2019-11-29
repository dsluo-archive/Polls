package dev.dsluo.polls.data.models;

import com.google.firebase.firestore.DocumentId;
import java.util.List;

/**
 * Model for Groups
 */
public class Group {
    @DocumentId
    public String groupId;
    public String name;
    public List<User> owners;
}
