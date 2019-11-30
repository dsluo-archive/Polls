package dev.dsluo.polls.data.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model for Polls
 *
 * @author David Luo
 */
public class Poll {
    @DocumentId
    public String pollId;
    public DocumentReference author;
    public String question;
    // Maps choices -> counts
    public Map<String, Integer> choices;

    public Poll() {
    }

    public Poll(DocumentReference author, String question, Map<String, Integer> choices) {
        this.author = author;
        this.question = question;
        this.choices = choices;
    }

    public Poll(DocumentReference author, String question, List<String> choices) {
        this.author = author;
        this.question = question;
        this.choices = new HashMap<>();
        for (String choice : choices)
            this.choices.put(choice, 0);
    }
}
