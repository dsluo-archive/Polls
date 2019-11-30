package dev.dsluo.polls.ui.newgroup;

import androidx.lifecycle.ViewModel;

import dev.dsluo.polls.data.repository.repositories.GroupRepository;

public class NewGroupViewModel extends ViewModel {
    private GroupRepository groupRepository = new GroupRepository();

    public void createNewGroup(String name, String description, GroupRepository.OnGroupCreatedListener onGroupCreatedListener) {
        groupRepository.createNewGroup(name, description, onGroupCreatedListener);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        groupRepository.clearRegistrations();
    }
}
