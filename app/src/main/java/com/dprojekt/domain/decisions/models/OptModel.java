package com.dprojekt.domain.decisions.models;

import com.dprojekt.domain.images.ImgModel;

import java.util.Comparator;

/**
 * Domain layer model for an option within a decision
 */
public class OptModel {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** ID of the option */
    private long id;

    /** Title of the option */
    private String title;

    /** Image of this option */
    private ImgModel img;

    /** Current pref set by the user for this option. Might not be sent yet. */
    private PrefValue userPrefValue;

    /** Result with all sent preferences for this option */
    private ResModel result;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImgModel getImg() {
        return img;
    }

    public void setImg(ImgModel img) {
        this.img = img;
    }

    public PrefValue getUserPrefValue() {
        return userPrefValue;
    }

    public void setUserPrefValue(PrefValue userPrefValue) {
        this.userPrefValue = userPrefValue;
    }

    public ResModel getResult() {
        return result;
    }

    public void setResult(ResModel result) {
        this.result = result;
    }

    // ==========================================================================
    // Constructor
    // ==========================================================================

    public OptModel(long id) {
        this.id = id;
    }

    // ==========================================================================
    // Custom Comparators
    // ==========================================================================

    /**
     * Comparator to rank options of the decision trying to maximize consensus.
     */
    public static class ConsensusOptComparator implements Comparator<OptModel> {
        @Override
        public int compare(OptModel lhs, OptModel rhs) {
            // First, use the Consensus Comparator with the results.
            ResModel.ConsensusResComparator comparator = new ResModel.ConsensusResComparator();
            int resComp = comparator.compare(lhs.getResult(), rhs.getResult());
            if (resComp != 0) {
                return resComp;
            } else {
                // If not solved, use alphabetic ordering with the titles.
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        }
    }
}