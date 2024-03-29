package dev.dsluo.polls.data.repository.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.ListenerRegistration;

import dev.dsluo.polls.data.models.User;
import dev.dsluo.polls.data.repository.FirebaseRepository;

import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

/**
 * Handle database operations regarding {@link User}s
 *
 * @author David Luo
 */
public class UserRepository extends FirebaseRepository {
    private MutableLiveData<User> user;

    /**
     * Retrieve the current authenticated {@link User}.
     *
     * @return The current {@link User}.
     */
    public LiveData<User> getUser() {
        if (this.user == null) {
            this.user = new MutableLiveData<>();
            ListenerRegistration userListenerRegistration =
                    firestore
                            .collection(USER_COLLECTION)
                            .document(auth.getUid())
                            .addSnapshotListener(
                                    (documentSnapshot, e) -> {
                                        User user = documentSnapshot != null ? documentSnapshot.toObject(User.class) : null;
                                        this.user.postValue(user);
                                    }
                            );
            registerListenerRegistration(userListenerRegistration);
        }
        return user;
    }

}
