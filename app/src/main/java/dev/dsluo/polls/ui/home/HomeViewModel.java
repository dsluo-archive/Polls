package dev.dsluo.polls.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.User;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private MutableLiveData<List<Group>> groups;
    private List<ListenerRegistration> listenerRegistrations = new ArrayList<>();

    private String userId = FirebaseAuth.getInstance().getUid();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public LiveData<User> getUser() {
        if (this.user == null) {
            this.user = new MutableLiveData<>();
            ListenerRegistration userListenerRegistration =
                    firestore
                            .collection(USER_COLLECTION)
                            .document(userId)
                            .addSnapshotListener(
                                    (documentSnapshot, e) -> {
                                        User user = documentSnapshot != null ? documentSnapshot.toObject(User.class) : null;
                                        this.user.postValue(user);
                                    }
                            );
            listenerRegistrations.add(userListenerRegistration);
        }
        return user;
    }

    public LiveData<List<Group>> getGroups() {
        if (this.groups == null) {
            this.groups = new MutableLiveData<>();
            ListenerRegistration groupsListenerRegistration =
                    firestore
                            .collection(USER_COLLECTION)
                            .document(userId)
                            .collection(GROUP_COLLECTION)
                            .addSnapshotListener(
                                    (queryDocumentSnapshots, e) -> {
                                        if (queryDocumentSnapshots == null)
                                            return;
                                        List<Group> groups = queryDocumentSnapshots.toObjects(Group.class);
                                        this.groups.postValue(groups);
                                    }
                            );
            listenerRegistrations.add(groupsListenerRegistration);
        }
        return groups;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        for (ListenerRegistration registration : listenerRegistrations) {
            registration.remove();
        }
    }
}
