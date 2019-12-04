package dev.dsluo.polls.ui.home.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.dsluo.polls.data.models.Poll;
import dev.dsluo.polls.data.repository.repositories.PollRepository;

public class PollDetailViewModel extends ViewModel {
    private PollRepository pollRepository = new PollRepository();

    public String groupId = null;
    public String pollId = null;

    private MutableLiveData<Boolean> showingResults = new MutableLiveData<>(false);

    private LiveData<Poll> currentPoll;

    public boolean isShowingResults() {
        return showingResults.getValue() != null ? showingResults.getValue() : false;
    }

    public LiveData<Boolean> listenShowingResults() {
        return showingResults;
    }

    public void setShowingResults(boolean value) {
        showingResults.setValue(value);
    }

    public void setPoll(String groupId, String pollId) {
        this.groupId = groupId;
        this.pollId = pollId;
        currentPoll = pollRepository.getPoll(groupId, pollId);
    }

    public LiveData<Poll> getPoll() {
        return currentPoll;
    }

    public void vote(String choiceKey, PollRepository.OnVoteListener onVoteListener) {
        pollRepository.vote(groupId, pollId, choiceKey, onVoteListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        pollRepository.clearRegistrations();
    }
}
