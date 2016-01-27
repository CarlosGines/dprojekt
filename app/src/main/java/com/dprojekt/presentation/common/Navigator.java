package com.dprojekt.presentation.common;

import android.app.Activity;
import android.content.Intent;

import com.dprojekt.activities.PublicProfileActivity;
import com.dprojekt.activities.WelcomeActivity;
import com.dprojekt.presentation.common.di.PerActivity;
import com.dprojekt.presentation.decisions.prefList.PrefListActivity;

import javax.inject.Inject;

/**
 * Class used to navigate through the application.
 */
@PerActivity
public class Navigator {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** The current activity */
    private final Activity mActivity;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public Navigator(Activity activity) {
        mActivity = activity;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Provides back navigation */
    public void navigateBack() {
        mActivity.finish();
    }

    /** Navigates to PublicProfileActivity
     *
     * @param userId ID of the user whose public profile we are checking.
     */
    public void navigateToPublicProf(long userId) {
        Intent i = new Intent(mActivity, PublicProfileActivity.class);
        i.putExtra(Constants.KEY_USER_ID, userId);
        mActivity.startActivity(i);
    }

    /** Navigates to WelcomeActivity
     *
     * @param decId ID of the decision to navigate to after WelcomeActivity.
     */
    public void navigateToWelcome(long decId) {
        Intent i = new Intent(mActivity, WelcomeActivity.class);
        i.putExtra(Constants.KEY_DEC_ID, decId);
        mActivity.startActivity(i);
    }

    /** Navigates to PrefListActivity showing the preferences or a participant
     *
     * @param decId ID of the current decision.
     * @param decSeed seed of the current decision
     * @param isPreview whether the decision is a preview accessed form a public link.
     * @param parId ID of the participant whose preferences we are checking.
     */
    public void navigateToParPrefList(long decId, String decSeed, boolean isPreview, long parId) {
        Intent i = new Intent(mActivity, PrefListActivity.class);
        i.putExtra(Constants.KEY_DEC_ID, decId);
        i.putExtra(Constants.KEY_DEC_SEED, decSeed);
        i.putExtra(Constants.KEY_IS_PREVIEW, isPreview);
        i.putExtra(Constants.KEY_PAR_ID, parId);
        mActivity.startActivity(i);
    }

    /** Navigates to PrefListActivity showing the preferences or an option
     *
     * @param decId ID of the current decision.
     * @param decSeed seed of the current decision
     * @param isPreview whether the decision is a preview accessed form a public link.
     * @param optId ID of the option whose preferences we are checking.
     */
    public void navigateToOptPrefList(long decId, String decSeed, boolean isPreview, long optId) {
        Intent i = new Intent(mActivity, PrefListActivity.class);
        i.putExtra(Constants.KEY_DEC_ID, decId);
        i.putExtra(Constants.KEY_DEC_SEED, decSeed);
        i.putExtra(Constants.KEY_IS_PREVIEW, isPreview);
        i.putExtra(Constants.KEY_OPT_ID, optId);
        mActivity.startActivity(i);
    }
}