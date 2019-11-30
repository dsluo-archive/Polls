package dev.dsluo.polls.ui.newgroup;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

import dev.dsluo.polls.data.models.Group;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

public class NewGroupViewModel extends ViewModel {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public interface OnGroupCreatedListener {
        void onGroupCreated(boolean isSuccessful);
    }

    public void createNewGroup(String name, String description, OnGroupCreatedListener onSuccess) {

        FirebaseUser owner = auth.getCurrentUser();
        DocumentReference ownerDoc = firestore.collection(USER_COLLECTION).document(owner.getUid());

        Group newGroup = new Group(
                name,
                description,
                Collections.singletonList(ownerDoc)
        );
        DocumentReference newGroupDoc = firestore.collection(GROUP_COLLECTION).document();

        firestore.runTransaction(transaction -> {
            transaction.set(newGroupDoc, newGroup);
            transaction.update(ownerDoc, GROUP_COLLECTION, FieldValue.arrayUnion(newGroupDoc));
            transaction.update(newGroupDoc, "owners", FieldValue.arrayUnion(ownerDoc));
            return null;
        }).addOnCompleteListener(task -> onSuccess.onGroupCreated(task.isSuccessful()));
    }
}
