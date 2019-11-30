package dev.dsluo.polls.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.data.models.User;
import dev.dsluo.polls.data.repository.repositories.GroupRepository;
import dev.dsluo.polls.data.repository.repositories.UserRepository;

/**
 * {@link ViewModel} for {@link HomeActivity}.
 */
public class HomeViewModel extends ViewModel {
    private UserRepository userRepository = new UserRepository();
    private GroupRepository groupRepository = new GroupRepository();


    /**
     * See {@link UserRepository#getUser()}
     */
    public LiveData<User> getUser() {
        return userRepository.getUser();
    }

    /**
     * See {@link GroupRepository#getGroups()}
     */
    public LiveData<List<Group>> getGroups() {
        return groupRepository.getGroups();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        userRepository.clearRegistrations();
        groupRepository.clearRegistrations();
    }
}
