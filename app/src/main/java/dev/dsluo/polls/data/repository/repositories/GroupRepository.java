package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

/**
 * Handles database operations regarding {@link Group}s
 *
 * @author David Luo
 */
public class GroupRepository extends FirebaseRepository {
    private MutableLiveData<List<Group>> groups;
    private MutableLiveData<List<Group>> ownedGroups;

    /**
     * Get {@link Group}s subscribed to by the currently authenticated {@link User}.
     *
     * @return The {@link User}'s {@link Group}s
     */
    public LiveData<List<Group>> getGroups() {
        if (this.groups == null) {
            this.groups = new MutableLiveData<>();
            ListenerRegistration groupsListenerRegistration =
                    USER_DOC
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

    /**
     * Get {@link Group}s owned by the currently authenticated {@link User}.
     *
     * @return The {@link User}'s owned {@link Group}s
     */
    public LiveData<List<Group>> getOwnedGroups() {
        if (this.ownedGroups == null) {
            this.ownedGroups = new MutableLiveData<>();
            ListenerRegistration ownedGroupsListenerReigstration =
                    firestore.collection(GROUP_COLLECTION)
                            .whereArrayContains("owners", USER_DOC)
                            .addSnapshotListener(
                                    (queryDocumentSnapshots, e) -> {
                                        if (queryDocumentSnapshots == null)
                                            return;
                                        List<Group> ownedGroups = queryDocumentSnapshots.toObjects(Group.class);
                                        this.ownedGroups.postValue(ownedGroups);
                                    }
                            );
            registerListenerRegistration(ownedGroupsListenerReigstration);
        }
        return this.ownedGroups;
    }

    /**
     * Handles result of group creation.
     */
    public interface OnGroupCreatedListener {
        void onGroupCreated(boolean isSuccessful);
    }

    /**
     * Create a new poll group and join the current user to it.
     *
     * @param name                   the name of the group.
     * @param description            the description of the group.
     * @param onGroupCreatedListener what to do after it is created.
     */
    public Group createNewGroup(String name, String description, OnGroupCreatedListener onGroupCreatedListener) {

        Group newGroup = new Group(
                name,
                description,
                Collections.singletonList(USER_DOC)
        );
        DocumentReference newGroupDoc = firestore.collection(GROUP_COLLECTION).document();

        firestore.runTransaction(transaction -> {
            transaction.set(newGroupDoc, newGroup);
            transaction.update(USER_DOC, GROUP_COLLECTION, FieldValue.arrayUnion(newGroupDoc));
            transaction.update(newGroupDoc, "owners", FieldValue.arrayUnion(USER_DOC));
            return null;
        }).addOnCompleteListener(task -> onGroupCreatedListener.onGroupCreated(task.isSuccessful()));

        return newGroup;
    }

    /**
     * Handles result of group joining.
     */
    public interface OnGroupJoinedListener {
        void onGroupJoined(boolean isSuccessful);
    }

    /**
     * Join a group.
     *
     * @param groupId               The group ID.
     * @param onGroupJoinedListener handle what to do after group is joined.
     */
    public void joinGroup(String groupId, OnGroupJoinedListener onGroupJoinedListener) {
        firestore.collection(GROUP_COLLECTION)
                .document(groupId)
                .get()
                .continueWithTask(
                        task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                if (snapshot.exists())
                                    return USER_DOC.update(
                                            "groups",
                                            FieldValue.arrayUnion(snapshot.getReference())
                                    );
                            }
                            return null;
                        }
                )
                .addOnCompleteListener(task -> onGroupJoinedListener.onGroupJoined(task.isSuccessful()));
    }
}
