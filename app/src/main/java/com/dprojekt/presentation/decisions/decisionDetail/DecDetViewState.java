package com.dprojekt.presentation.decisions.decisionDetail;

import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.ParModel;

/**
 * POJO (not encapsulated) with the state of the view, used by view and presenter classes.
 */
public class DecDetViewState {

    /** Whether the data is being loaded */
    public boolean isLoading;

    /** ID of the user */
    public long myUserId;

    /** ID of the decision we are retrieving */
    public long decId;

    /** Seed of the decision, used for public links */
    public String decSeed;

    /** Whether this decision is a preview (we are not participants) */
    public boolean isPreview;

    /** Id of the user we are reading prefs, in case we are, 0 otherwise */
    public long readPrefsParId;

    /** The decision we are retrieving */
    public DecModel dec;

    /** The participant corresponding to this user. */
    public ParModel userPar;

    /** Whether the current and the sent preferences are different */
    public boolean prefsModified;

    /** Index of the current tab */
    public int currentTabIndex;

    /** Whether the details area is expanded */
    public boolean isDetExpanded;

    /** Whether the initial tips have been shown */
    public boolean initTipsDone;

    /** Step of the pref setting tutorial in which the user is. */
    public int prefSetterTutStep;
}
