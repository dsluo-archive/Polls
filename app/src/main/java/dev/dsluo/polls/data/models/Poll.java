package dev.dsluo.polls.data.models;

import com.google.firebase.firestore.DocumentId;

import java.util.Map;

/**
 * Model for Polls
 *
 * @author David Luo
 */
public class Poll {
    @DocumentId
    public String pollId;
    public String question;
    // Maps choices -> counts
    public Map<String, Integer> choices;
}
