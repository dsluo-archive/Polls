package dev.dsluo.polls.data.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

/**
 * Model for Groups
 */
public class Group {
    @DocumentId
    public String groupId;
    public String name;
    public String description;
    public List<DocumentReference> owners;

    public Group(String name, String description, List<DocumentReference> owners) {
        this.name = name;
        this.description = description;
        this.owners = owners;
    }

    public Group() {

    }
}
