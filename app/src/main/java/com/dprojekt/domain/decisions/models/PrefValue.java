package com.dprojekt.domain.decisions.models;

/**
 * Domain layer representation of the value of a preference of a participant for an option.
 */
public final class PrefValue {

    // ==========================================================================
    // Constants
    // ==========================================================================

    /** Representation of a "favourite" preference */
    public static final Integer FAV_PREF = 101;

    /** Representation of a "reject" preference */
    public static final Integer REJECT_PREF = -1;

    /** Representation of a blank preference */
    public static final Integer BLANK_PREF = null;

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Encapsulated variable used to store the preference value */
    private final Integer value;

    // ==========================================================================
    // Instantiation
    // ==========================================================================

    // Private constructor
    private PrefValue(Integer value) {
        this.value = value;
    }

    /** Get a new PrefValue representing a Favourite value */
    public static PrefValue newFavValue() {
        return new PrefValue(FAV_PREF);
    }

    /** Get a new PrefValue representing a Reject value */
    public static PrefValue newRejectValue() {
        return new PrefValue(REJECT_PREF);
    }

    /** Get a new PrefValue representing a Blank value */
    public static PrefValue newBlankValue() {
        return new PrefValue(BLANK_PREF);
    }

    /**
     * Get a new PrefValue representing an Ok value.
     * Value must be in the range [0-100]
     * @param okValue ok preference value. Must be in the range [0-100]
     */
    public static PrefValue newOkValue(int okValue) {
        checkOKValue(okValue);
        return new PrefValue(okValue);
    }

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public boolean isFavValue() {
        return value == FAV_PREF;
    }

    public boolean isRejectValue() {
        return value == REJECT_PREF;
    }

    public boolean isBlankValue() {
        return value == BLANK_PREF;
    }

    public boolean isOkValue() {
        return value != null && value >= 0 && value <= 100;
    }

    public boolean isOkValueGrade(int okValue) {
        return value == okValue;
    }

    public int getOkValueGrade() {
        if (isOkValue()) {
            return value;
        } else {
            throw new UnsupportedOperationException(
                    "Trying to access \"ok value\" grade when not an \"ok value\"");
        }
    }

    // ==========================================================================
    // Object methods
    // ==========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrefValue prefValue = (PrefValue) o;

        return !(value != null ? !value.equals(prefValue.value) : prefValue.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        if (value == REJECT_PREF) {
            return "REJECT_PREF";
        } else if (value == FAV_PREF) {
            return "FAV_PREF";
        } else if (value == BLANK_PREF) {
            return "BLANK_PREF";
        } else {
            return "OK_" + value + "_PREF";
        }
    }

    // ==========================================================================
    // Custom methods
    // ==========================================================================

    /** Check the int value used as Ok is in the right range [0-100] */
    private static void checkOKValue(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Ok value not in range [0, 100]");
        }
    }
}
