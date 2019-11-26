import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

export const createUser = functions.auth.user().onCreate(user => {
    const userDoc = {
        groups: []
    };
    return admin.firestore().collection('users').doc(user.uid).set(userDoc)
        .catch(reason => {
            console.log('Failed to create user document:', reason);
        });
});

export const deleteUser = functions.auth.user().onDelete((user, _context) => {
    return admin.firestore().collection('users').doc(user.uid).delete();
});