package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.User;
import dev.dsluo.polls.data.repository.FirebaseRepository;

import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

public class GroupRepository extends FirebaseRepository {
    private MutableLiveData<List<Group>> groups;

    private String userId = FirebaseAuth.getInstance().getUid();

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
                            .document(userId)
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
}
