package today.datamodel;

import java.util.Objects;

/**
 * The category of an event, with primary and secondary categories.
 */
public record Category(String primary, String secondary) implements Comparable<Category> {
    @Override
    public int compareTo(Category otherCategory) {
        // First compare by primary category
        int primaryComparison = this.primary().compareTo(otherCategory.primary());
        if (primaryComparison != 0) {
            return primaryComparison;
        }

        // If primary category is the same, compare by secondary
        return this.secondary().compareTo(otherCategory.secondary());
    }

    /**
     * Parse a category string in the format "primary"
     * or "primary/secondary" and make a category of them.
     * Folds the category parts to lower case.
     * Throws java.lang.IllegalArgumentException if the
     * string is of the wrong format.
     *
     * @param categoryString the string to parse
     * @return new category
     */
    public static Category parse(String categoryString) {
        Objects.requireNonNull(categoryString);

        String message = "category should be primary/secondary";
        if (categoryString.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        String[] categoryParts = categoryString.split("/");
        switch (categoryParts.length) {
            case 1:
                return new Category(categoryParts[0], "");
            case 2:
                return new Category(categoryParts[0], categoryParts[1]);
            default:
                throw new IllegalArgumentException(message);
        }
    }

    @Override
    public String toString() {
        if (secondary().isBlank()) {
            return primary();
        }
        return primary() + "/" + secondary();
    }

}