package dev.dsluo.polls.ui.newpoll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.repository.repositories.GroupRepository;
import dev.dsluo.polls.data.repository.repositories.PollRepository;

/**
 * {@link ViewModel} for {@link NewPollActivity}
 *
 * @author David Luo
 */
public class NewPollViewModel extends ViewModel {
    private PollRepository pollRepository = new PollRepository();
    private GroupRepository groupRepository = new GroupRepository();


    /**
     * See {@link PollRepository#createNewPoll(Group, String, List, PollRepository.OnPollCreatedListener)}
     */
    public void createNewPoll(Group group, String question, List<String> choices, PollRepository.OnPollCreatedListener onPollCreatedListener) {
        pollRepository.createNewPoll(group, question, choices, onPollCreatedListener);
    }

    /**
     * See {@link GroupRepository#getGroups()}
     */
    public LiveData<List<Group>> getGroups() {
        return groupRepository.getGroups();
    }

    /**
     * Clears Firebase subscriptions created by database operations.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        pollRepository.clearRegistrations();
        groupRepository.clearRegistrations();
    }
}
