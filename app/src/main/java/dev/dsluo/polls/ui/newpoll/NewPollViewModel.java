package dev.dsluo.polls.ui.newpoll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.repository.repositories.GroupRepository;
import dev.dsluo.polls.data.repository.repositories.PollRepository;

public class NewPollViewModel extends ViewModel {
    private PollRepository pollRepository = new PollRepository();
    private GroupRepository groupRepository = new GroupRepository();


    public void createNewPoll(Group group, String question, List<String> choices, PollRepository.OnPollCreatedListener onPollCreatedListener) {
        pollRepository.createNewPoll(group, question, choices, onPollCreatedListener);
    }

    public LiveData<List<Group>> getGroups() {
        return groupRepository.getGroups();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        pollRepository.clearRegistrations();
        groupRepository.clearRegistrations();
    }
}
