package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.User;
import dev.dsluo.polls.data.repository.FirebaseRepository;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

public class GroupRepository extends FirebaseRepository {
    private MutableLiveData<List<Group>> groups;

    /**
     * Get {@link Group}s subscribed to by the currently authenticated {@link User}.
     *
     * @return The {@link User}'s {@link Group}s
     */
    public LiveData<List<Group>> getGroups() {
        if (this.groups == null) {
            this.groups = new MutableLiveData<>();
            ListenerRegistration groupsListenerRegistration =
                    firestore
                            .collection(USER_COLLECTION)
                            .document(auth.getUid())
                            .addSnapshotListener(
                                    (documentSnapshot, e) -> {
                                        if (documentSnapshot == null) {
                                            return;
                                        }
                                        User user = documentSnapshot.toObject(User.class);

                                        List<Task<DocumentSnapshot>> getTasks = new ArrayList<>();
                                        for (DocumentReference groupRef : user.groups)
                                            getTasks.add(groupRef.get());
                                        Tasks.whenAllSuccess(getTasks).addOnSuccessListener(
                                                objects -> {
                                                    List<Group> groups = new ArrayList<>();
                                                    for (Object groupDoc : objects) {
                                                        Group group = ((DocumentSnapshot) groupDoc).toObject(Group.class);
                                                        groups.add(group);
                                                    }
                                                    this.groups.postValue(groups);
                                                }
                                        );
                                    }
                            );
            registerListenerRegistration(groupsListenerRegistration);
        }
        return groups;
    }

    public interface OnGroupCreatedListener {
        void onGroupCreated(boolean isSuccessful);
    }

    public void createNewGroup(String name, String description, OnGroupCreatedListener onGroupCreatedListener) {

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
        }).addOnCompleteListener(task -> onGroupCreatedListener.onGroupCreated(task.isSuccessful()));
    }
}
