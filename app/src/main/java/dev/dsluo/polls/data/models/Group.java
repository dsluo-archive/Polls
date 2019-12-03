package dev.dsluo.polls.data.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

/**
 * Model for Groups
 *
 * @author David Luo
 */
@SuppressWarnings("unused")
public class Group {
    @DocumentId
    public String groupId;
    public String name;
    public String description;
    public List<DocumentReference> owners;

    /**
     * Constructor for manual construction
     *
     * @param name        Name of the group.
     * @param description Description of the group.
     * @param owners      List of people who can post to the group.
     */
    public Group(String name, String description, List<DocumentReference> owners) {
        this.name = name;
        this.description = description;
        this.owners = owners;
    }

    /**
     * Constructor for {@link com.google.firebase.firestore.DocumentSnapshot#toObject(Class)}.
     */
    public Group() {
    }

    /**
     * toString for {@link android.widget.ArrayAdapter}s.
     *
     * @return The group's name.
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
