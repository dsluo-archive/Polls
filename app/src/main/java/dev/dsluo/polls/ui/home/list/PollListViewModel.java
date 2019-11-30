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
     * See {}
     */
    public LiveData<List<Poll>> getPolls() {
        return pollRepository.getPolls();
    }

    /**
     * Get the currently displayed {@link Group}.
     *
     * @return The currently displayed {@link Group}.
     */
    public Group getActiveGroup() {
        return pollRepository.getActiveGroup();
    }

    /**
     * Set the currently displayed {@link Group} and start listening for {@link Poll}s in that {@link Group}.
     *
     * @param group The {@link Group} to display.
     */
    public void setActiveGroup(Group group) {
        pollRepository.setActiveGroup(group);
    }

    /**
     * Clears any remaining subscriptions subscribed by {@link #setActiveGroup(Group)}.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        pollRepository.clearRegistrations();
    }
}
