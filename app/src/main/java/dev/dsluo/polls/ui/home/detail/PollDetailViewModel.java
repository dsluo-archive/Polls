package dev.dsluo.polls.ui.home.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.dsluo.polls.data.models.Poll;
import dev.dsluo.polls.data.repository.repositories.PollRepository;

/**
 * {@link ViewModel} for {@link PollDetailFragment}
 *
 * @author David Luo
 * @author Darren Ing
 */
public class PollDetailViewModel extends ViewModel {
    private PollRepository pollRepository = new PollRepository();

    public String groupId = null;
    public String pollId = null;

    private MutableLiveData<Boolean> showingResults = new MutableLiveData<>(false);

    private LiveData<Poll> currentPoll;

    /**
     * Check if results of poll are currently showing
     */
    public boolean isShowingResults() {
        return showingResults.getValue() != null ? showingResults.getValue() : false;
    }

    /**
     * Listen for change in whether or not to show results
     */
    public LiveData<Boolean> listenShowingResults() {
        return showingResults;
    }

    /**
     * Set boolean value for MutableLiveData<Boolean> showingResults, determining if results should
     * be displayed or not
     *
     * @param value boolean for showingResults
     */
    public void setShowingResults(boolean value) {
        showingResults.setValue(value);
    }

    /**
     * Set the current poll using groupId and pollId
     *
     * @param groupId {@inheritDoc}
     * @param pollId {@inheritDoc}
     */
    public void setPoll(String groupId, String pollId) {
        this.groupId = groupId;
        this.pollId = pollId;
        currentPoll = pollRepository.getPoll(groupId, pollId);
    }

    /**
     * Return the current poll
     */
    public LiveData<Poll> getPoll() {
        return currentPoll;
    }

    /**
     * See {@link PollRepository#vote(String, String, String, PollRepository.OnVoteListener)}
     */
    public void vote(String choiceKey, PollRepository.OnVoteListener onVoteListener) {
        pollRepository.vote(groupId, pollId, choiceKey, onVoteListener);
    }

    /**
     * Clears Firebase subscriptions created by database operations.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        pollRepository.clearRegistrations();
    }
}
