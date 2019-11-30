package dev.dsluo.polls.ui.newgroup;

import androidx.lifecycle.ViewModel;

import dev.dsluo.polls.data.repository.repositories.GroupRepository;

/**
 * {@link ViewModel} for {@link NewGroupActivity}
 *
 * @author David Luo
 */
public class NewGroupViewModel extends ViewModel {
    private GroupRepository groupRepository = new GroupRepository();

    /**
     * See {@link GroupRepository#createNewGroup(String, String, GroupRepository.OnGroupCreatedListener)}
     */
    public void createNewGroup(String name, String description, GroupRepository.OnGroupCreatedListener onGroupCreatedListener) {
        groupRepository.createNewGroup(name, description, onGroupCreatedListener);
    }

    /**
     * Clears Firebase subscriptions created by database operations.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        groupRepository.clearRegistrations();
    }
}
