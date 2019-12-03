package dev.dsluo.polls.ui.home.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.Poll;
import dev.dsluo.polls.data.repository.repositories.PollRepository;

/**
 * {@link ViewModel} for {@link PollListFragment}.
 *
 * @author David Luo
 */
public class PollListViewModel extends ViewModel {
    private PollRepository pollRepository = new PollRepository();

    /**
     * See {@link PollRepository#getPolls()}
     */
    public LiveData<List<Poll>> getPolls() {
        return pollRepository.getPolls();
    }

    /**
     * See {@link PollRepository#getActiveGroup()}
     * @return
     */
    public LiveData<Group> getActiveGroup() {
        return pollRepository.getActiveGroup();
    }

    /**
     * See {@link PollRepository#setActiveGroup(Group)}
     */
    public void setActiveGroup(Group group) {
        pollRepository.setActiveGroup(group);
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
