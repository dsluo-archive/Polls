package dev.dsluo.polls.data.models;

import java.util.Map;

public class Poll {
    public String question;
    // Maps choices -> counts
    public Map<String, Integer> choices;
}
