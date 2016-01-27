package com.dprojekt.domain.decisions.models;

/**
 * Domain layer model for a preference of a participant for a option within a decision
 */
public class PrefModel {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** ID of the participant who set this preference */
    private long parId;

    /** ID of the option this preference has been set for */
    private long optId;

    /** The preference value */
    private PrefValue prefValue;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public long getParId() {
        return parId;
    }

    public void setParId(long parId) {
        this.parId = parId;
    }

    public long getOptId() {
        return optId;
    }

    public void setOptId(long optId) {
        this.optId = optId;
    }

    public PrefValue getPrefValue() {
        return prefValue;
    }

    public void setPrefValue(PrefValue prefValue) {
        this.prefValue = prefValue;
    }

    // ==========================================================================
    // Constructor
    // ==========================================================================

    public PrefModel() {
    }

    public PrefModel(long optId, PrefValue prefValue) {
        this.optId = optId;
        this.prefValue = prefValue;
    }
}
