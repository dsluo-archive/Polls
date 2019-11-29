package dev.dsluo.polls.ui.newgroup;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

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

        String userId = auth.getUid();

        final DocumentReference owner = firestore.collection(USER_COLLECTION)
                .document(userId);
        Group newGroup = new Group(
                name,
                description,
                Collections.singletonList(owner)
        );

        Task<DocumentReference> createGroup = firestore.collection(GROUP_COLLECTION).add(newGroup);

        Task<Void> joinGroup = createGroup.continueWithTask(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentReference newGroupRef = task.getResult();
//                        String newGroupDocPath = newGroupRef.getPath();

                        WriteBatch writeBatch = firestore.batch();
                        writeBatch.update(owner, GROUP_COLLECTION, FieldValue.arrayUnion(newGroupRef));
                        writeBatch.update(newGroupRef, "owners", FieldValue.arrayUnion(owner));

                        return writeBatch.commit();

                    }
                    throw new RuntimeException();
                }
        );

        joinGroup.addOnCompleteListener(task -> onSuccess.onGroupCreated(task.isSuccessful()));
    }
}
