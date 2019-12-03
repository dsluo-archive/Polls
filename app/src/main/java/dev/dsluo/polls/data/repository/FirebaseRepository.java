package dev.dsluo.polls.data.repository;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import static dev.dsluo.polls.data.Constants.USER_COLLECTION;

/**
 * Abstract base class for Firebase Repositories.
 * Mostly provides common methods and properties for use in subclasses.
 *
 * @author David Luo
 */
public abstract class FirebaseRepository {
    private List<ListenerRegistration> listenerRegistrations = new ArrayList<>();

    protected FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    protected FirebaseAuth auth = FirebaseAuth.getInstance();

    protected final DocumentReference USER_DOC =
            firestore.collection(USER_COLLECTION).document(auth.getUid());

    /**
     * Record a {@link ListenerRegistration} so that it may be cleared later.
     *
     * @param registration A {@link ListenerRegistration} as returned by
     *                     {@link com.google.firebase.firestore.DocumentReference#addSnapshotListener(EventListener)}
     *                     and similar methods.
     */
    protected void registerListenerRegistration(ListenerRegistration registration) {
        listenerRegistrations.add(registration);
    }

    /**
     * Clear all {@link ListenerRegistration}s. This will stop auto-updating any UI elements.
     * This should be called in {@link ViewModel#onCleared()} to avoid memory leaks.
     * <p>
     * TODO: there's probably a better way to structure this so that subclasses don't have to do the above manually.
     */
    public void clearRegistrations() {
        for (ListenerRegistration registration : listenerRegistrations) {
            registration.remove();
        }
        listenerRegistrations.clear();
    }
}
