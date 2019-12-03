package dev.dsluo.polls.ui.join;

import androidx.lifecycle.ViewModel;

import dev.dsluo.polls.data.repository.repositories.GroupRepository;

/**
 * {@link ViewModel} for {@link JoinActivity}
 *
 * @author David Luo
 */
public class JoinViewModel extends ViewModel {
    private GroupRepository groupRepository = new GroupRepository();

    /**
     * See {@link GroupRepository#joinGroup(String, GroupRepository.OnGroupJoinedListener)}
     */
    public void joinGroup(String groupId, GroupRepository.OnGroupJoinedListener onGroupJoinedListener) {
        groupRepository.joinGroup(groupId, onGroupJoinedListener);
    }
}
