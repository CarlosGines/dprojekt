package com.dprojekt.domain.decisions.models;

import java.util.Comparator;

/**
 * Created by efrel on 18/8/15.
 */
public class ResModel {

    private int favs;
    private int rejects;
    private int oks;
    private int points;

    public int getFavs() {
        return favs;
    }

    public int getRejects() {
        return rejects;
    }

    public int getOks() {
        return oks;
    }

    // ==========================================================================
    // Custom methods
    // ==========================================================================

    public int getNumPrefs() {
        return favs + rejects + oks;
    }

    public float getOkAvg() {
        if (oks > 0) {
            return points * 1f / oks;
        } else {
            return 0;
        }
    }

    public void addPrefValue(PrefValue prefValue) {
        if (prefValue.isFavValue()) {
            favs++;
        } else if (prefValue.isRejectValue()) {
            rejects++;
        } else if (prefValue.isOkValue()) {
            oks++;
            points += prefValue.getOkValueGrade();
        }
    }

    // ==========================================================================
    // Custom Comparators
    // ==========================================================================

    /**
     * Comparator for a consensus result
     */
    static class ConsensusResComparator implements Comparator<ResModel> {
        @Override
        public int compare(ResModel lhs, ResModel rhs) {
            // Check rejects
            int dRejects = lhs.rejects - rhs.rejects;
            if (dRejects != 0) {
                return dRejects;
            }
            // If not solved, then check favs
            int dFavs = lhs.favs - rhs.favs;
            if (dFavs != 0) {
                return -dFavs;
            }
            // If not solved, then check ok average
            float dOkAvg = lhs.getOkAvg() - rhs.getOkAvg();
            if (dOkAvg != 0) {
                return (int) -dOkAvg;
            }
            // If not solved, then check num prefs
            int dNumPrefs = lhs.getNumPrefs() - rhs.getNumPrefs();
            if (dNumPrefs != 0) {
                return -dNumPrefs;
            }
            // If not solved, then they are the same
            return 0;
        }
    }
}
