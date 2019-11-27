package dev.dsluo.polls.data.models;

import java.util.List;

/**
 * Model for Groups
 */
public class Group {
    public String groupId;
    public String name;
    public List<User> owners;
}
