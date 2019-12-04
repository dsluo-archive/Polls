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
@SuppressWarnings("unused")
public class Poll {
    @DocumentId
    public String pollId;
    public DocumentReference author;
    public String question;
    // Maps choices -> counts
    public Map<String, Integer> choices;

    /**
     * Constructor for {@link com.google.firebase.firestore.DocumentSnapshot#toObject(Class)}
     */
    public Poll() {
    }

    /**
     * Constructor for exiting polls.
     *
     * @param author   The author
     * @param question The question
     * @param choices  A Map of choices to their counts.
     */
    public Poll(DocumentReference author, String question, Map<String, Integer> choices) {
        this.author = author;
        this.question = question;
        this.choices = choices;
    }

    /**
     * Constructor for new polls.
     *
     * @param author   The author
     * @param question The question
     * @param choices  A list of choices that should be in the poll. All counts will be zero.
     */
    public Poll(DocumentReference author, String question, List<String> choices) {
        this.author = author;
        this.question = question;
        this.choices = new HashMap<>();
        for (String choice : choices)
            this.choices.put(choice, 0);
    }

    /**
     * Get the total number of votes on this poll.
     *
     * @return The total number of votes.
     */
    public int getVoteCount() {
        int total = 0;
        for (Integer count : choices.values()) {
            total += count;
        }
        return total;
    }
}
