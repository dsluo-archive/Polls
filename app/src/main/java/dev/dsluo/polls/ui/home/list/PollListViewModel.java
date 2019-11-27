package dev.dsluo.polls.ui.home.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.Poll;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.POLL_COLLECTION;

public class PollListViewModel extends ViewModel {
    private MutableLiveData<List<Poll>> polls;
    private ListenerRegistration pollListenerRegistration = null;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Group activeGroup = null;

    public LiveData<List<Poll>> getPolls() {
        if (this.polls == null) {
            this.polls = new MutableLiveData<>();
        }
        return polls;
    }

    public Group getActiveGroup() {
        return activeGroup;
    }

    public void setActiveGroup(Group group) {
        this.activeGroup = group;

        if (pollListenerRegistration != null)
            pollListenerRegistration.remove();
        if (group != null) {
            pollListenerRegistration =
                    firestore
                            .collection(GROUP_COLLECTION)
                            .document(group.groupId)
                            .collection(POLL_COLLECTION)
                            .addSnapshotListener(
                                    (queryDocumentSnapshots, e) -> {
                                        if (queryDocumentSnapshots == null)
                                            return;
                                        List<Poll> polls = queryDocumentSnapshots.toObjects(Poll.class);
                                        this.polls.postValue(polls);
                                    }
                            );
        } else {
            pollListenerRegistration = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (pollListenerRegistration != null)
            pollListenerRegistration.remove();
    }
}
