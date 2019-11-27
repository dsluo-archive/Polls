package dev.dsluo.polls.data.models;

import java.util.Map;

/**
 * Model for Polls
 *
 * @author David Luo
 */
public class Poll {
    public String pollId;
    public String question;
    // Maps choices -> counts
    public Map<String, Integer> choices;
}
