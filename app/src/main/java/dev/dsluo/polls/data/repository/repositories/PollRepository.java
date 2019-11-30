package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.Poll;
import dev.dsluo.polls.data.repository.FirebaseRepository;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.POLL_COLLECTION;

public class PollRepository extends FirebaseRepository {
    private MutableLiveData<List<Poll>> polls;

    private Group activeGroup = null;

    /**
     * Get the polls for the currently displayed {@link Group}.
     *
     * @return A {@link LiveData} of {@link List} of {@link Poll}s.
     */
    public LiveData<List<Poll>> getPolls() {
        if (this.polls == null) {
            this.polls = new MutableLiveData<>();
        }
        return polls;
    }

    /**
     * Get the currently displayed {@link Group}.
     *
     * @return The currently displayed {@link Group}.
     */
    public Group getActiveGroup() {
        return activeGroup;
    }

    /**
     * Set the currently displayed {@link Group} and start listening for {@link Poll}s in that {@link Group}.
     *
     * @param group The {@link Group} to display.
     */
    public void setActiveGroup(Group group) {
        this.activeGroup = group;

        clearRegistrations();
        if (group != null) {
            ListenerRegistration pollListenerRegistration =
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
            registerListenerRegistration(pollListenerRegistration);
        }
    }
}
