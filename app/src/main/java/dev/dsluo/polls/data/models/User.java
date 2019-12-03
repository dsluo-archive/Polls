package dev.dsluo.polls.data.models;

import android.net.Uri;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

/**
 * Model for Users
 *
 * @author David Luo
 */
@SuppressWarnings("unused")
public class User {
    @DocumentId
    public String userId;
    public boolean emailVerified;
    public String email;
    public String displayName;
    public Uri photoURL;
    public String phoneNumber;
    public boolean disabled;

    public List<DocumentReference> groups;
}
