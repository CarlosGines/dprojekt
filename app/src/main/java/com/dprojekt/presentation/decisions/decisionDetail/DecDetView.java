package com.dprojekt.presentation.decisions.decisionDetail;

import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.decisions.models.PrefValue;
import com.dprojekt.presentation.common.BaseView;

/**
 * Interface that represents the View getting callbacks from {@link DecDetPresenter}.
 */
public interface DecDetView extends BaseView{

    /** Render the decision complete */
    void renderDecDet(DecModel dec);

    /** Set the current tab (participants, options or results tab) */
    void setCurrentTab(int index);

    /** Show/hide button for users who are not participant to join the decision */
    void showJoinButton(boolean show);

    /** Show dialog requesting user to register in order to continue */
    void showRegistrationRequest();

    /** Show confirmation that the user joined the decision successfully */
    void confirmUserJoined();

    /** Update the options menu according to view state */
    void updateOptionsMenu();

    /** Show confirmation dialog to join decision and send preferences at the same time */
    void showJoinAndSendPrefsConfirmation(int numBlanks);

    /** Expand the details view with the description and the full title */
    void expandDetails();

    /** Collapse the details view with the description and the full title */
    void collapseDetails();

    /** Show/hide "Send preferences" and "Reset preferences" buttons */
    void showPrefsButtons(boolean show);

    /** Set the given preference value for the given option */
    void setPrefViewValue(int optPos, PrefValue prefValue);

    /** Show confirmation dialog to send preferences */
    void showSendPrefsConfirmation(int numBlanks);

    /** Show confirmation that preferences were sent successfully */
    void showPrefsSentMessage();

    /** Set the preferences of the participant at the given position as read */
    void setPrefsRead(int parPos);

    /** Show confirmation that the given participant was added as contact successfully  */
    void showConAddedMessage(ParModel par);

    /** Show confirmation that the given participant was blocked successfully  */
    void showUserBlockedMessage(ParModel par);

    /** Show confirmation dialog to delete the decision */
    void showDeleteDecConfirmation();

    /** Show confirmation that the decision was deleted successfully */
    void showDecDeletedMessage();

    /** Show confirmation dialog to leave the decision */
    void showExitDecConfirmation();

    /** Show confirmation that user left the decision successfully */
    void showDecExitedMessage();

    /** Show confirmation dialog to close the decision */
    void showCloseDecConfirmation();

    /** Update decision to closed and inform the user about it */
    void updateDecClosed();

    /** Launch sharing intent for this decision */
    void shareDec();
}
