package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.Poll;
import dev.dsluo.polls.data.repository.FirebaseRepository;

import static dev.dsluo.polls.data.Constants.GROUP_COLLECTION;
import static dev.dsluo.polls.data.Constants.POLL_COLLECTION;
import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

/**
 * Handle database operations regarding {@link Poll}s
 *
 * @author David Luo
 * @author Darren Ing
 */
public class PollRepository extends FirebaseRepository {
    private MutableLiveData<List<Poll>> polls;

    private MutableLiveData<Group> activeGroup = new MutableLiveData<>();

    /**
     * Return a poll from the Firebase collection identified by groupId and pollId
     *
     * @param groupId   The ID of the group that the poll belongs to.
     * @param pollId    The ID of the poll.
     * @return {@inheritDoc}
     */
    public LiveData<Poll> getPoll(String groupId, String pollId) {
        MutableLiveData<Poll> poll = new MutableLiveData<>();
        ListenerRegistration pollListenerRegistration = firestore.collection(GROUP_COLLECTION)
                .document(groupId)
                .collection(POLL_COLLECTION)
                .document(pollId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot == null)
                        return;
                    Poll pollPojo = documentSnapshot.toObject(Poll.class);
                    poll.postValue(pollPojo);
                });
        registerListenerRegistration(pollListenerRegistration);
        return poll;
    }

    /**
     * Set interface for vote success
     */
    public interface OnVoteListener {
        void onVote(boolean isSuccessful);
    }

    /**
     * Vote on a current poll with the current user as the author.
     *
     * @param groupId           Which group to the poll was created for.
     * @param pollId            The ID of the poll.
     * @param choiceKey         Selected choice for the poll.
     * @param onVoteListener    What to do after the vote.
     */
    public void vote(String groupId, String pollId, String choiceKey, OnVoteListener onVoteListener) {
        DocumentReference pollDoc = firestore.collection(GROUP_COLLECTION)
                .document(groupId)
                .collection(POLL_COLLECTION)
                .document(pollId);
        firestore.runTransaction(
                transaction -> {
                    DocumentSnapshot snapshot = transaction.get(pollDoc);
                    Poll poll = snapshot.toObject(Poll.class);
                    poll.choices.put(choiceKey, poll.choices.get(choiceKey) + 1);
                    transaction.update(pollDoc, "choices", poll.choices);
                    return null;
                }
        ).addOnCompleteListener(task -> onVoteListener.onVote(task.isSuccessful()));
    }

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
    public LiveData<Group> getActiveGroup() {
        return activeGroup;
    }

    /**
     * Set the currently displayed {@link Group} and start listening for {@link Poll}s in that {@link Group}.
     *
     * @param group The {@link Group} to display.
     */
    public void setActiveGroup(Group group) {
        this.activeGroup.postValue(group);

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

    /**
     * Handles result of poll creation.
     */
    public interface OnPollCreatedListener {
        void onPollCreated(boolean isSuccessful);
    }

    /**
     * Create a new poll with the current user as the author.
     *
     * @param group                 Which group to create the poll in.
     * @param question              The question to poll.
     * @param choices               Choices for the poll.
     * @param onPollCreatedListener What to do after the poll is created.
     */
    public void createNewPoll(Group group, String question, List<String> choices, OnPollCreatedListener onPollCreatedListener) {
        FirebaseUser author = auth.getCurrentUser();
        DocumentReference authorDoc = firestore.collection(USER_COLLECTION).document(author.getUid());

        Poll newPoll = new Poll(
                authorDoc,
                question,
                choices
        );

        DocumentReference newPollDoc =
                firestore
                        .collection(GROUP_COLLECTION)
                        .document(group.groupId)
                        .collection(POLL_COLLECTION)
                        .document();

        newPollDoc.set(newPoll)
                .addOnCompleteListener(task -> onPollCreatedListener.onPollCreated(task.isSuccessful()));
    }
}
