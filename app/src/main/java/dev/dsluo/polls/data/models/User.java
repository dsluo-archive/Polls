package dev.dsluo.polls.data.models;

import android.net.Uri;

/**
 * Model for Users
 *
 * @author David Luo
 */
public class User {
    public String userId;
    public boolean emailVerified;
    public String email;
    public String displayName;
    public Uri photoURL;
    public String phoneNumber;
    public boolean disabled;
}
