package com.dprojekt.domain.decisions.models;

import android.support.annotation.NonNull;

import com.dprojekt.domain.images.ImgModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Domain layer model for a decision.
 */
public class DecModel implements Comparable<DecModel> {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // Decision states
    public static final int DRAFT_STATE = 1;
    public static final int OPEN_STATE = 2;
    public static final int CLOSED_STATE = 3;
    public static final int OUTBOX_STATE = 4;

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** ID of the Decision */
    private long id;

    /** Seed of the Decision, used to identify decision in public links. It is an encoded UUID */
    private String seed;

    /** Id of the user that created this decision */
    private long adminId;

    /** Title of the Decision */
    private String title;

    /** Description of the Decision */
    private String description;

    /** Image of this decision */
    private ImgModel img;

    /** State of the Decision: DRAFT, OUTBOX, OPEN or CLOSED */
    private int state;

    /** List of participants of this decision */
    private List<ParModel> parList;

    /** List of options of this decision */
    private List<OptModel> optList;

    /** List of options ordered for the result */
    private List<OptModel> resList;

    /** Whether this decision is a preview accessed form a public link */
    private boolean isPreview;

    /**
     * Amount of new prefs received and not read. Calculated from DecUpdates and
     * decision last read time.
     */
    private int numNewPrefs;

    /** Timestamp in seconds of last time the user read this decision */
    private int lastReadTime;

    /** Timestamp in seconds of last update of the Decision */
    private int updateTime;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImgModel getImg() {
        return img;
    }

    public void setImg(ImgModel img) {
        this.img = img;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ParModel> getParList() {
        return parList;
    }

    public void setParList(List<ParModel> parList) {
        this.parList = parList;
    }

    public List<OptModel> getOptList() {
        return optList;
    }

    public void setOptList(List<OptModel> optList) {
        this.optList = optList;
    }

    public List<OptModel> getResList() {
        return resList;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setIsPreview(boolean isPreview) {
        this.isPreview = isPreview;
    }

    public int getNumNewPrefs() {
        return numNewPrefs;
    }

    public void setNumNewPrefs(int numNewPrefs) {
        this.numNewPrefs = numNewPrefs;
    }

    public int getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(int lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    // ==========================================================================
    // Constructors
    // ==========================================================================

    public DecModel() {}

    public DecModel (long id) {
        this.id = id;
    }

    // ==========================================================================
    // Object methods
    // ==========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecModel decModel = (DecModel) o;

        return id == decModel.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    // ==========================================================================
    // Comparable implementation
    // ==========================================================================

    @Override
    public int compareTo(@NonNull DecModel another) {
        return another.updateTime - updateTime;
    }

    // ==========================================================================
    // Utils
    // ==========================================================================

    /**
     * Check if empty. Empty decisions are not stored.
     *
     * @return whether the decision is empty
     */
    public boolean isEmpty() {
        return !(title != null || description != null || img != null
                || parList.size() > 1 || !optList.isEmpty());
    }

    /**
     * @return the list of IDs of participants of the decision
     */
    public List<Long> getParIdList() {
        List<Long> parIdList = new ArrayList<>();
        for (ParModel par : parList) {
            parIdList.add(par.getId());
        }
        return parIdList;
    }

    /**
     * Check if a certain user is a participant of this decision
     *
     * @param userId the user to be checked
     * @return whether the user is a participant
     */
    public boolean isPar(long userId) {
        for (ParModel par : parList) {
            if (par.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the result for each option from the preferences list of the participants and
     * sets it in the results list.
     */
    public void calcRes() {
        // Set the result for each option.
        // First prepare a map (optionId -> option) and set new result object for teh option.
        Map<Long, OptModel> optMap = new HashMap<>();
        int numPrefs = 0;
        for (OptModel opt : optList) {
            opt.setResult(new ResModel());
            optMap.put(opt.getId(), opt);
        }
        // Iterate over the preferences of each participant and add it to the corresponding option.
        for (ParModel par : parList) {
            for (PrefModel pref : par.getPrefList()) {
                PrefValue prefValue = pref.getPrefValue();
                optMap.get(pref.getOptId()).getResult().addPrefValue(prefValue);
                numPrefs += prefValue.isBlankValue() ? 0 : 1;
            }
        }

        // Create the results list, already solved (options list with results and ordered)
        if (numPrefs > 0) {
            resList = new ArrayList<>(optList);
            Collections.sort(resList, new OptModel.ConsensusOptComparator());
        } else {
            resList = new ArrayList<>();
        }
    }
}
