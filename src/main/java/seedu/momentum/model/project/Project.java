package seedu.momentum.model.project;

import static seedu.momentum.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.momentum.model.tag.Tag;

/**
 * Represents a Project in the project book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Project {

    // Identity fields
    private final Name name;

    //data fields
    private final Description description;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Project(Name name, Description description, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.description = description;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both projects of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two projects.
     */
    public boolean isSameProject(Project otherProject) {
        if (otherProject == this) {
            return true;
        }

        return otherProject != null
                && otherProject.getName().equals(getName()) && otherProject.getDescription().equals(getDescription());
    }

    /**
     * Returns true if both projects have the same identity and data fields.
     * This defines a stronger notion of equality between two projects.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Project)) {
            return false;
        }

        Project otherProject = (Project) other;
        return otherProject.getName().equals(getName())
                && otherProject.getDescription().equals(getDescription())
                && otherProject.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, description, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Description: ")
                .append(getDescription())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
