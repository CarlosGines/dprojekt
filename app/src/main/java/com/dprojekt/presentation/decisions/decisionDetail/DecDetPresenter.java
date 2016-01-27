package com.dprojekt.presentation.decisions.decisionDetail;

import com.dprojekt.domain.decisions.usecases.CheckDecImgUseCase;
import com.dprojekt.domain.decisions.usecases.CheckOptImgUseCase;
import com.dprojekt.domain.decisions.usecases.CloseDecUseCase;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.usecases.DeleteDecUseCase;
import com.dprojekt.domain.decisions.usecases.ExitDecUseCase;
import com.dprojekt.domain.decisions.usecases.GetDecDetUseCase;
import com.dprojekt.domain.decisions.usecases.JoinDecUseCase;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.decisions.models.PrefModel;
import com.dprojekt.domain.decisions.models.PrefValue;
import com.dprojekt.domain.decisions.usecases.PreviewDecDetUseCase;
import com.dprojekt.domain.decisions.usecases.SendPrefsUseCase;
import com.dprojekt.domain.decisions.usecases.SetParPrefsReadUseCase;
import com.dprojekt.domain.decisions.usecases.SetPrefUseCase;
import com.dprojekt.domain.decisions.usecases.UpdateDecReadTimeUseCase;
import com.dprojekt.domain.users.usecases.AddConUseCase;
import com.dprojekt.domain.users.usecases.BlockUserUseCase;
import com.dprojekt.domain.users.usecases.CheckParImgUseCase;
import com.dprojekt.domain.users.models.ConModel;
import com.dprojekt.presentation.common.BaseView;
import com.dprojekt.presentation.common.rx.DefaultSubscriber;
import com.dprojekt.presentation.common.Navigator;
import com.dprojekt.presentation.common.di.PerActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter that controls communication between views and models of the presentation layer.
 */
@PerActivity
public class DecDetPresenter {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // Indexes for the tabs in the view
    public static final int PAR_TAB_INDEX = 0;
    public static final int OPT_TAB_INDEX = 1;
    public static final int RES_TAB_INDEX = 2;

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Navigator */
    @Inject
    Navigator mNavigator;

    /** View object for events callbacks */
    private DecDetView mView;

    /** State of the view */
    private DecDetViewState mViewState;

    // Use cases
    private final GetDecDetUseCase mGetDecDetUseCase;
    private final UpdateDecReadTimeUseCase mUpdateDecReadTimeUseCase;
    private final CheckDecImgUseCase mCheckDecImgUseCase;
    private final CheckParImgUseCase mCheckParImgUseCase;
    private final CheckOptImgUseCase mCheckOptImgUseCase;
    private final SetPrefUseCase mSetPrefUseCase;
    private final SendPrefsUseCase mSendPrefsUseCase;
    private final SetParPrefsReadUseCase mSetParPrefsReadUseCase;
    private final ExitDecUseCase mExitDecUseCase;
    private final DeleteDecUseCase mDeleteDecUseCase;
    private final CloseDecUseCase mCloseDecUseCase;
    private final AddConUseCase mAddConUseCase;
    private final BlockUserUseCase mBlockUserUseCase;

    private final PreviewDecDetUseCase mPreviewDecDetUseCase;
    private final JoinDecUseCase mJoinDecUseCase;

    /** Flag used to indicate that SendPrefsUseCase must be executed after JoinDecUseCase */
    private boolean mSendPrefsAfterJoin;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public DecDetPresenter(GetDecDetUseCase getOptListUseCase,
                           UpdateDecReadTimeUseCase updateDecReadTimeUseCase,
                           PreviewDecDetUseCase previewDecDetUseCase,
                           JoinDecUseCase joinDecUseCase,
                           CheckDecImgUseCase checkDecImgUseCase,
                           CheckParImgUseCase checkParImgUseCase,
                           CheckOptImgUseCase checkOptImgUseCase,
                           SetPrefUseCase setPrefUseCase,
                           SendPrefsUseCase sendPrefsUseCase,
                           SetParPrefsReadUseCase setParPrefsReadUseCase,
                           ExitDecUseCase exitDecUseCase,
                           DeleteDecUseCase deleteDecUseCase,
                           CloseDecUseCase closeDecUseCase,
                           AddConUseCase addConUseCase,
                           BlockUserUseCase blockUserUseCase) {
        mGetDecDetUseCase = getOptListUseCase;
        mUpdateDecReadTimeUseCase = updateDecReadTimeUseCase;
        mPreviewDecDetUseCase = previewDecDetUseCase;
        mJoinDecUseCase = joinDecUseCase;
        mCheckDecImgUseCase = checkDecImgUseCase;
        mCheckParImgUseCase = checkParImgUseCase;
        mCheckOptImgUseCase = checkOptImgUseCase;
        mSetPrefUseCase = setPrefUseCase;
        mSendPrefsUseCase = sendPrefsUseCase;
        mSetParPrefsReadUseCase = setParPrefsReadUseCase;
        mExitDecUseCase = exitDecUseCase;
        mDeleteDecUseCase = deleteDecUseCase;
        mCloseDecUseCase = closeDecUseCase;
        mAddConUseCase = addConUseCase;
        mBlockUserUseCase = blockUserUseCase;
    }

    // ==========================================================================
    // View events
    // ==========================================================================

    public void onInit(DecDetView view, DecDetViewState viewState) {
        // Set view and view state
        mView = view;
        mViewState = viewState;
        // Init
        mViewState.isLoading = true;
        mView.setLoading(true);
        this.checkReadingNewPrefs();
    }

    /** Check whether we are reading someone prefs on view init */
    private void checkReadingNewPrefs() {
        if (mViewState.readPrefsParId != 0) {
            // We are reading someone prefs
            mSetParPrefsReadUseCase.execute(mViewState.decId, mViewState.readPrefsParId,
                    new DefaultSubscriber(mView));
        }
    }

    public void onResume() {
        // Get decision details
        if (!mViewState.isPreview) {
            // Get decision from ID, standard access
            mGetDecDetUseCase.execute(mViewState.decId, mViewState.myUserId,
                    new GetDecDetSubscriber(mView));
            mUpdateDecReadTimeUseCase.execute(mViewState.decId, false, new DefaultSubscriber(mView));
        } else {
            // Get decision from public link, preview access
            mPreviewDecDetUseCase.execute(mViewState.decSeed, mViewState.myUserId,
                    new GetDecDetSubscriber(mView));
        }
    }

    public void onPause() {
        if (!mViewState.isPreview && mViewState.dec != null) {
            // Update decision read time
            mUpdateDecReadTimeUseCase.execute(mViewState.decId, false, new DefaultSubscriber(mView));
        }
    }

    public void onDestroy() {
        // Unsibscribe from use cases
        mGetDecDetUseCase.unsubscribe();
        mUpdateDecReadTimeUseCase.unsubscribe();
        mPreviewDecDetUseCase.unsubscribe();
        mJoinDecUseCase.unsubscribe();
        mCheckDecImgUseCase.unsubscribe();
        mCheckParImgUseCase.unsubscribe();
        mCheckOptImgUseCase.unsubscribe();
        mSetPrefUseCase.unsubscribe();
        mSendPrefsUseCase.unsubscribe();
        mSetParPrefsReadUseCase.unsubscribe();
        mExitDecUseCase.unsubscribe();
        mDeleteDecUseCase.unsubscribe();
        mCloseDecUseCase.unsubscribe();
        mAddConUseCase.unsubscribe();
        mBlockUserUseCase.unsubscribe();
    }

    public void onDetExpandCollapseClick() {
        if (mViewState.isDetExpanded) {
            // Collapse details view
            mView.collapseDetails();
        } else {
            // Expand details view
            mView.expandDetails();
        }
        mViewState.isDetExpanded = !mViewState.isDetExpanded;
    }

    public void onTabChanged(int index) {
        mViewState.currentTabIndex = index;
        // Check for join/prefs button display according to view specifications
        if (mViewState.prefsModified) {
            if (index == OPT_TAB_INDEX) {
                mView.showPrefsButtons(true);
                if (mViewState.userPar == null) {
                    mView.showJoinButton(false);
                }
            } else {
                mView.showPrefsButtons(false);
                if (mViewState.userPar == null) {
                    mView.showJoinButton(true);
                }
            }
        }
    }

    public CheckDecImgUseCase onCheckDecImg() {
        return mCheckDecImgUseCase;
    }

    public CheckParImgUseCase onCheckUserImg() {
        return mCheckParImgUseCase;
    }

    public CheckOptImgUseCase onCheckOptImg() {
        return mCheckOptImgUseCase;
    }

    /** The user has just set a preference for an option.
     *
     * @param pos position of the option receiving prefs in the options list.
     * @param prefValue the new pref value set.
     */
    public void onPrefSet(int pos, PrefValue prefValue) {
        // Update state
        OptModel opt = mViewState.dec.getOptList().get(pos);
        opt.setUserPrefValue(prefValue);
        // Execute use case if not previewing to persist preference
        mSetPrefUseCase.execute(mViewState.decId, mViewState.isPreview, opt.getId(), prefValue,
                new DefaultSubscriber(mView));
        // Check for prefs modifications to update view
        this.checkPrefsModified();
    }

    public void onJoinDecClick() {
        if (mViewState.myUserId != Constants.NO_ID) {
            // Join decision
            mJoinDecUseCase.execute(mViewState.dec, mViewState.myUserId, new JoinDecSubscriber(mView));
        } else {
            // Not registered yet, request registration
            mView.showRegistrationRequest();
        }
    }

    public void onRegisterConfirmed() {
        mNavigator.navigateToWelcome(mViewState.decId);
    }

    public void onResetPrefsClick() {
        // Set all options preferences to either blank or last sent values
        int optPos = 0;
        for (OptModel opt : mViewState.dec.getOptList()) {
            // Get current and sent preferences
            PrefValue sentPrefValue = null;
            if (mViewState.userPar != null) {
                for (PrefModel pref : mViewState.userPar.getPrefList()) {
                    if (pref.getOptId() == opt.getId()) {
                        sentPrefValue = pref.getPrefValue();
                        break;
                    }
                }
            }
            // Match current with sent preference state
            if (sentPrefValue != null) {
                opt.setUserPrefValue(sentPrefValue);
            } else {
                opt.setUserPrefValue(PrefValue.newBlankValue());
            }
            // Update view for this option
            mView.setPrefViewValue(optPos, opt.getUserPrefValue());
            // Execute use case
            mSetPrefUseCase.execute(mViewState.decId, mViewState.isPreview, opt.getId(),
                    opt.getUserPrefValue(), new DefaultSubscriber(mView));
            // Update index
            optPos++;
        }
        // Update view and state
        mViewState.prefsModified = false;
        mView.showPrefsButtons(false);
        // If previewing decision, show join button
        if (mViewState.userPar == null) {
            mView.showJoinButton(true);
        }
    }

    public void onSendPrefsClick() {
        if (mViewState.myUserId != Constants.NO_ID) {
            // User registered
            if (mViewState.userPar != null) {
                // User is participant
                mView.showSendPrefsConfirmation(getNumBlanks());
            } else {
                // User not participant yet
                mView.showJoinAndSendPrefsConfirmation(getNumBlanks());
            }
        } else {
            // User not registered. Request registration
            mView.showRegistrationRequest();
        }
    }

    /** @retrun the number of blank preferences in the user's preference list */
    private int getNumBlanks() {
        int numBlank = 0;
        for (OptModel opt : mViewState.dec.getOptList()) {
            if (opt.getUserPrefValue().isBlankValue()) {
                numBlank++;
            }
        }
        return numBlank;
    }

    public void onSendPrefsConfirmed() {
        // Update sent prefs and result in state
        mViewState.prefsModified = false;
        List<PrefModel> sentUserPrefList = mViewState.userPar.getPrefList();
        sentUserPrefList.clear();
        for (OptModel opt : mViewState.dec.getOptList()) {
            sentUserPrefList.add(new PrefModel(opt.getId(), opt.getUserPrefValue()));
        }
        // Calculate new result
        mViewState.dec.calcRes();
        // Update participation status in state
        mViewState.userPar.setParStatus(ParModel.PREFS_SENT_STATUS);

        // Execute use case
        mSendPrefsUseCase.execute(mViewState.decId, mViewState.isPreview, mViewState.userPar,
                new DefaultSubscriber(mView));
        // Update view
        mView.showPrefsButtons(false);
        mView.showPrefsSentMessage();
    }

    public void onJoinAndSendPrefsConfirmed() {
        // Join decision and send preferences afterwards using flag
        mSendPrefsAfterJoin = true;
        mJoinDecUseCase.execute(mViewState.dec, mViewState.myUserId, new JoinDecSubscriber(mView));
    }

    public void onParItemClick(int pos) {
        ParModel par = mViewState.dec.getParList().get(pos);
        long parId = par.getId();

        // Set preferences as read
        if (par.isPrefsUnread()) {
            // Update prefs read in in state
            par.setPrefsUnread(false);
            // Execute use case
            mSetParPrefsReadUseCase.execute(mViewState.decId, parId, new DefaultSubscriber(mView));
            // Update view
            mView.setPrefsRead(pos);
        }

        // Navigate to participant prefs detail activity (which has dialog style)
        mNavigator.navigateToParPrefList(mViewState.dec.getId(), mViewState.decSeed,
                mViewState.isPreview, parId);
    }

    public void onParViewProfClick(ParModel par) {
        mNavigator.navigateToPublicProf(par.getUserId());
    }

    public void onParAddConClick(ParModel par) {
        // Update state
        par.setConRole(ConModel.IS_CONTACT_ROLE);
        // Execute use case
        mAddConUseCase.execute(par.getUserId(), par.getName(), par.getLastName(),
                new DefaultSubscriber(mView));
        // Show message to user
        mView.showConAddedMessage(par);
    }

    public void onParBlockClick(ParModel par) {
        // Update state
        par.setConRole(ConModel.I_BLOCKED_ROLE);
        // Execute use case
        mBlockUserUseCase.execute(par.getUserId(), new DefaultSubscriber(mView));
        // Show message to user
        mView.showUserBlockedMessage(par);
    }

    public void onResItemClick(int pos) {
        // Start prefs detail activity (which has dialog style)
        OptModel opt = mViewState.dec.getResList().get(pos);
        // Navigate to option prefs detail activity (which has dialog style)
        mNavigator.navigateToOptPrefList(mViewState.decId, mViewState.decSeed,
                mViewState.isPreview, opt.getId());
    }

    public void onExitDecClick() {
        mView.showExitDecConfirmation();
    }

    public void onExitDecConfirmed() {
        // Execute use case
        mExitDecUseCase.execute(mViewState.decId, mViewState.isPreview, new DefaultSubscriber<>(mView));
        // Update model
        mViewState.dec = null;
        // User feedback
        mView.showDecExitedMessage();
        // Navigate back
        mNavigator.navigateBack();
    }

    public void onDeleteDecClick() {
        mView.showDeleteDecConfirmation();
    }

    public void onDeleteDecConfirmed() {
        // Execute use case
        mDeleteDecUseCase.execute(mViewState.decId, mViewState.isPreview, new DefaultSubscriber<>(mView));
        // Update model
        mViewState.dec = null;
        // User feedback
        mView.showDecDeletedMessage();
        // Navigate back
        mNavigator.navigateBack();
    }

    public void onCloseDecClick() {
        mView.showCloseDecConfirmation();
    }

    public void onCloseDecConfirmed() {
        // Execute use case
        mCloseDecUseCase.execute(mViewState.decId, mViewState.isPreview, new DefaultSubscriber(mView));
        // Update state
        mViewState.dec.setState(DecModel.CLOSED_STATE);
        if (mViewState.prefsModified) {
            // Current prefs must be reset
            this.onResetPrefsClick();
        }
        // Update view
        mView.updateDecClosed();

    }

    public void onShareDecClick() {
        mView.shareDec();
    }

    // ==========================================================================
    // Use Case Subscribers
    // ==========================================================================

    /**
     * Use case subscriber to receive notifications from GetDecDetUseCase and PreviewDecDetUseCase
     */
    private final class GetDecDetSubscriber extends DefaultSubscriber<DecModel> {

        public GetDecDetSubscriber(BaseView baseView) {
            super(baseView);
        }

        @Override
        public void onNext(DecModel dec) {

            // Update state
            mViewState.dec = dec;
            mViewState.decId = dec.getId();
            mViewState.isPreview = dec.isPreview();
            mViewState.decSeed = dec.getSeed();
            DecDetPresenter.this.extractUserPar();

            // Update view
            this.setDefaultTab();
            if (mViewState.userPar != null) {
                mView.showJoinButton(false);
            }
            DecDetPresenter.this.checkPrefsModified();
            if (mViewState.prefsModified && mViewState.dec.getState() == DecModel.CLOSED_STATE) {
                // If decision closed, current prefs must be reset
                DecDetPresenter.this.onResetPrefsClick();
            }
            mView.renderDecDet(dec);
            if (mViewState.isLoading) {
                mViewState.isLoading = false;
                mView.setLoading(false);
            }
        }

        // Set the tab we show on init
        private void setDefaultTab() {
            if (mViewState.currentTabIndex == -1) {
                if (mViewState.userPar == null) {
                    // If previewing decision, set options tab
                    mViewState.currentTabIndex = OPT_TAB_INDEX;
                } else if (mViewState.dec.getState() == DecModel.CLOSED_STATE) {
                    // If decision closed, set results tab
                    mViewState.currentTabIndex = RES_TAB_INDEX;
                } else if (mViewState.userPar.getParStatus() != ParModel.PREFS_SENT_STATUS) {
                    // If user has not sent prefs, set options tab
                    mViewState.currentTabIndex = OPT_TAB_INDEX;
                } else {
                    // otherwise, set participants tab
                    mViewState.currentTabIndex = PAR_TAB_INDEX;
                }
            }
            mView.setCurrentTab(mViewState.currentTabIndex);
            DecDetPresenter.this.onTabChanged(mViewState.currentTabIndex);
        }

        @Override
        public void onCompleted() {
            if (mViewState.isLoading) {
                mViewState.isLoading = false;
                mView.setLoading(false);
            }
        }

        @Override
        public void onError() {
            if (mViewState.isLoading) {
                mViewState.isLoading = false;
                mView.setLoading(false);
            }
        }
    }

    /**
     * Use case subscriber to receive notifications from JoinDecUseCase
     */
    private final class JoinDecSubscriber extends DefaultSubscriber<DecModel> {

        public JoinDecSubscriber(BaseView baseView) {
            super(baseView);
        }

        @Override
        public void onNext(DecModel dec) {
            // Update state
            mViewState.dec = dec;
            mViewState.decId = dec.getId();
            mViewState.isPreview = false;
            DecDetPresenter.this.extractUserPar();
            this.saveCurrentPrefs();

            // Update view
            mView.renderDecDet(dec);
            mView.showJoinButton(false);
            mView.updateOptionsMenu();
            mView.confirmUserJoined();

            // Check if we are coming from "JoinDec and SendPrefs"
            if (mSendPrefsAfterJoin) {
                mSendPrefsAfterJoin = false;
                // Perform send prefs
                onSendPrefsConfirmed();
            }
        }

        // Save the current user preferences (not sent) in persistence
        private void saveCurrentPrefs() {
            for (OptModel opt : mViewState.dec.getOptList()) {
                mSetPrefUseCase.execute(mViewState.decId, false, opt.getId(), opt.getUserPrefValue(),
                        new DefaultSubscriber(mView));
            }
        }
    }

    // ==========================================================================
    // Helper methods
    // ==========================================================================

    /** Find the user as participant and save it in state object */
    private void extractUserPar() {
        for (ParModel par : mViewState.dec.getParList()) {
            if (par.getUserId() == mViewState.myUserId) {
                mViewState.userPar = par;
                break;
            }
        }
    }

    /** Check whether current prefs of this user differ from those sent to server. */
    private void checkPrefsModified() {
        for (OptModel opt : mViewState.dec.getOptList()) {
            PrefValue sentPrefValue = null;
            // Get user prefs if the user is participant (not previewing)
            if (mViewState.userPar != null) {
                for (PrefModel pref : mViewState.userPar.getPrefList()) {
                    if (pref.getOptId() == opt.getId()) {
                        sentPrefValue = pref.getPrefValue();
                        break;
                    }
                }
            }
            // Check current and sent preference
            if (sentPrefValue != null) {
                if (!sentPrefValue.equals(opt.getUserPrefValue())) {
                    // Modified. Different pref value for an option. Update accordingly.
                    this.updatePrefsModified();
                    return;
                }
            } else {
                if (!opt.getUserPrefValue().isBlankValue()) {
                    // Modified. Different pref value for an option. Update accordingly.
                    this.updatePrefsModified();
                    return;
                }
            }
        }
        // No modifications found
        mViewState.prefsModified = false;
        mView.showPrefsButtons(false);
        // If user not participant, show join button
        if (mViewState.userPar == null) {
            mView.showJoinButton(true);
        }
    }

    /** Current prefs of this user differ from those sent to server. Update accordingly. */
    private void updatePrefsModified() {
        // Update state
        mViewState.prefsModified = true;
        // Update view
        if (mViewState.currentTabIndex == OPT_TAB_INDEX) {
            // If user not participant, hide join button first
            if (mViewState.userPar == null) {
                mView.showJoinButton(false);
            }
            // Show prefs buttons
            mView.showPrefsButtons(true);
        }
    }
}
