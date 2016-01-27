package com.dprojekt.presentation.decisions.decisionDetail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.decisions.models.PrefValue;
import com.dprojekt.presentation.common.AlertDialogFragment;
import com.dprojekt.presentation.common.BaseActivity;
import com.dprojekt.presentation.decisions.DaggerDecComponent;
import com.dprojekt.presentation.decisions.DecTutActivity;
import com.dprojekt.presentation.decisions.di.DecComponent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Decision Detail Activity.
 */
public class DecDetActivity extends BaseActivity implements AlertDialogFragment.AlertDialogListener,
        DecDetView {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // Saved instance state keys
    private static final String KEY_IS_DEC_PREVIEW = "is_dec_preview";
    private static final String KEY_TAB_INDEX = "tab_index";

    // Tags to identify tabs from the TabHost
    private static final String PAR_TAB_TAG = "par_tab";
    private static final String OPT_TAB_TAG = "opt_tab";
    private static final String RES_TAB_TAG = "res_tab";

    // Request codes for dialogs
    private static final int REGISTER_RC = 100;
    private static final int SEND_PREF_RC = 101;
    private static final int JOIN_AND_SEND_PREF_RC = 102;
    private static final int DELETE_DEC_RC = 103;
    private static final int EXIT_DEC_RC = 104;
    private static final int CLOSE_DEC_RC = 105;

    // Shared preferences keys
    public static final String KEY_PREF_SETTER_TUT_STEP = "pref_setter_tut_step";
    public static final String KEY_INIT_TIPS_DONE = "init_tips_done";

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Decision Detail Presenter from Model-View-Presenter pattern */
    @Inject DecDetPresenter mPresenter;

    // Bound layout views
    @Bind(R.id.main_view) View v_main;
    @Bind(R.id.loading_view) View v_loading;
    @Bind(R.id.dec_image) UpdatableImageView uiv_dec;
    @Bind(R.id.title) TextView tv_dec_title;
    @Bind(R.id.expand_collapse_button) ImageView ib_expand_collapse;
    @Bind(R.id.decision_details) View v_dec_dets;
    @Bind(R.id.detail_title) TextView tv_dec_det_title;
    @Bind(R.id.detail_description) TextView tv_dec_description;
    @Bind(android.R.id.tabhost) FragmentTabHost th_dec;
    @Bind(R.id.join_dec_button_container) View v_join_dec;
    @Bind(R.id.prefs_buttons_container) View v_prefs_buttons;

    /** State of this view */
    private DecDetViewState mViewState;

    // Fragments of the tab widget
    private ParListFragment mParListFragment;
    private OptListFragment mOptListFragment;
    private ResListFragment mResListFragment;

    /** Dagger component for decisions */
    private DecComponent mDecComponent;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    /** @return the view state for this view */
    DecDetViewState getViewState() {
        return mViewState;
    }

    /** @return the presenter for this view */
    DecDetPresenter getPresenter() {
        return mPresenter;
    }

    /** Set the participants list fragment */
    void setParListFragment(ParListFragment parListFragment) {
        mParListFragment = parListFragment;
    }

    /** Set the options list fragment */
    void setOptListFragment(OptListFragment optListFragment) {
        mOptListFragment = optListFragment;
    }

    /** Set the results list fragment */
    void setResFragment(ResListFragment resListFragment) {
        mResListFragment = resListFragment;
    }

    // ==========================================================================
    // Activity lifecycle methods
    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_det);
        ButterKnife.bind(this);

        // Initialize state for this activity
        this.initState(savedInstanceState);
        // Set the toolbar
        this.initToolbar();
        // Init the views
        this.initViews();
        // Prepare injector and inject
        this.initInjector();
        // Init event for presenter
        mPresenter.onInit(this, mViewState);
    }

    // Initializes this activity.
    private void initState(Bundle savedInstanceState) {
        // New view state to set
        mViewState = new DecDetViewState();

        // Get my user ID
        mViewState.myUserId = AccountUtils.getMyUserId(this);

        if (savedInstanceState == null) {
            // Activity open for the first time
            Uri data = getIntent().getData();
            if (data == null) {
                // Coming form another activity
                Bundle extras = getIntent().getExtras();
                // Most of times we get decision ID from extra bundle
                mViewState.decId = extras.getLong(Constants.KEY_DEC_ID);
                // We might get a decision seed if coming from InstallReferrerReceiver
                mViewState.decSeed = extras.getString(Constants.KEY_DEC_SEED);
                mViewState.isPreview = mViewState.decId == 0;
                mViewState.readPrefsParId = extras.getLong(Constants.KEY_PAR_ID);
            } else {
                // Coming from a public link
                mViewState.decSeed = data.getQueryParameter(Constants.KEY_DEC_SEED);
                mViewState.isPreview = true;
            }
            mViewState.currentTabIndex = -1;
        } else {
            // Restoring activity
            mViewState.decId = savedInstanceState.getLong(Constants.KEY_DEC_ID);
            mViewState.decSeed = savedInstanceState.getString(Constants.KEY_DEC_SEED);
            mViewState.isPreview = savedInstanceState.getBoolean(KEY_IS_DEC_PREVIEW);
            mViewState.currentTabIndex = savedInstanceState.getInt(KEY_TAB_INDEX);
        }

        // Get tutorial state form shared preferences
        SharedPreferences sp = getSharedPreferences(Constants.DEFAULT_PREFS, Activity.MODE_PRIVATE);
        mViewState.prefSetterTutStep = sp.getInt(KEY_PREF_SETTER_TUT_STEP, 0);
        mViewState.initTipsDone = sp.getBoolean(KEY_INIT_TIPS_DONE, false);
        // Check whether to show initial tips
        if (!mViewState.initTipsDone) {
            mViewState.initTipsDone = true;
            getSharedPreferences(Constants.DEFAULT_PREFS, Activity.MODE_PRIVATE).edit()
                    .putBoolean(DecDetActivity.KEY_INIT_TIPS_DONE, true).apply();
            startActivity(new Intent(this, DecTutActivity.class));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(Constants.KEY_DEC_ID, mViewState.decId);
        outState.putString(Constants.KEY_DEC_SEED, mViewState.decSeed);
        outState.putBoolean(KEY_IS_DEC_PREVIEW, mViewState.isPreview);
        outState.putInt(KEY_TAB_INDEX, mViewState.currentTabIndex);
        super.onSaveInstanceState(outState);
    }

    // Initializes the toolbar for this activity
    protected void initToolbar() {
        super.initToolbar();
        // Don't show title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // Initializes the views of this activity
    private void initViews() {
        ImageUtils.resizeViewHeight(uiv_dec, Constants.HEADER_IMG_RATIO);
        this.initTabHost();
    }

    /**
     * Initialize TabHost that provides tabs to navigate
     */
    private void initTabHost() {
        // Calling setup() necessary before adding tabs
        th_dec.setup(this, getFragmentManager(), android.R.id.tabcontent);
        // Add the tabs
        th_dec.addTab(th_dec.newTabSpec(PAR_TAB_TAG).setIndicator(getString(R.string.participants)),
                ParListFragment.class, null);
        th_dec.addTab(th_dec.newTabSpec(OPT_TAB_TAG).setIndicator(getString(R.string.options)),
                OptListFragment.class, null);
        th_dec.addTab(th_dec.newTabSpec(RES_TAB_TAG).setIndicator(getString(R.string.result)),
                ResListFragment.class, null);

        // Set on tab change listener
        final int tabsCount = th_dec.getTabWidget().getChildCount();
        th_dec.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // Change selected tab alpha
                for (int i = 0; i < tabsCount; i++) {
                    if (i == th_dec.getCurrentTab()) {
                        mPresenter.onTabChanged(i);
                        break;
                    }
                }
            }
        });
    }

    // Initializes injector and inject
    private void initInjector() {
        this.mDecComponent = DaggerDecComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        this.mDecComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset myUserId, in case we just signed in
        mViewState.myUserId = AccountUtils.getMyUserId(this);
        this.mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    // ==========================================================================
    // User input
    // ==========================================================================

    @OnClick(R.id.expand_collapse_button)
    public void onDetExpandCollapseClick() {
        mPresenter.onDetExpandCollapseClick();
    }

    @OnClick(R.id.join_dec_button)
    public void onJoinDecClick() {
        mPresenter.onJoinDecClick();
    }

    @OnClick(R.id.reset_prefs_button)
    public void onResetPrefsClick() {
        mPresenter.onResetPrefsClick();
    }

    @OnClick(R.id.send_prefs_button)
    public void onSendPrefsClick() {
        mPresenter.onSendPrefsClick();
    }

    // ==========================================================================
    // Options menu
    // ==========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mViewState.dec != null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.dec_det_menu, menu);
            // Set options menu according to state
            if(mViewState.userPar != null) {
                if (mViewState.dec.getAdminId() == mViewState.myUserId) {
                    menu.removeItem(R.id.action_exit);
                    if (mViewState.dec.getState() == DecModel.CLOSED_STATE) {
                        menu.removeItem(R.id.action_close);
                    }
                } else {
                    menu.removeItem(R.id.action_delete);
                    menu.removeItem(R.id.action_close);
                }
            } else {
                menu.removeItem(R.id.action_exit);
                menu.removeItem(R.id.action_delete);
                menu.removeItem(R.id.action_close);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete:
                mPresenter.onDeleteDecClick();
                return true;
            case R.id.action_exit:
                mPresenter.onExitDecClick();
                return true;
            case R.id.action_close:
                mPresenter.onCloseDecClick();
                return true;
            case R.id.action_share:
                mPresenter.onShareDecClick();
                return true;
            case R.id.action_show_tutorial:
                // Show tutorial activity
                mViewState.prefSetterTutStep = 0;
                getSharedPreferences(Constants.DEFAULT_PREFS, Activity.MODE_PRIVATE).edit()
                        .remove(DecDetActivity.KEY_PREF_SETTER_TUT_STEP).apply();
                startActivity(new Intent(this, DecTutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ==========================================================================
    // DecDetView implementation
    // ==========================================================================

    @Override
    public void setLoading(final boolean isLoading) {
        v_main.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        v_loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNoConnection(boolean isNoConnection) {
        if (isNoConnection) {
            super.showMessage(R.string.no_connection);
        }
    }

    @Override
    public void renderDecDet(final DecModel dec) {
        // Update menu
        invalidateOptionsMenu();

        // Set activity title
        String title = dec.getTitle();
        setTitle(title);

        // Fill views
        uiv_dec.loadAndCheckDecImg(dec, getPresenter().onCheckDecImg());
        tv_dec_title.setText(title);
        tv_dec_det_title.setText(title);
        tv_dec_description.setText(dec.getDescription());

        // Render active tab fragment
        if (mViewState.currentTabIndex == DecDetPresenter.PAR_TAB_INDEX &&
                mParListFragment != null) {
            mParListFragment.renderParList(dec);
        } else if (mViewState.currentTabIndex == DecDetPresenter.OPT_TAB_INDEX &&
                mOptListFragment != null) {
            mOptListFragment.renderOptList(dec);
        } else if (mViewState.currentTabIndex == DecDetPresenter.RES_TAB_INDEX &&
                mResListFragment != null) {
            mResListFragment.renderRes(dec);
        }
    }

    @Override
    public void setCurrentTab(int index) {
        th_dec.setCurrentTab(index);
    }

    @Override
    public void showJoinButton(boolean show) {
        v_join_dec.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRegistrationRequest() {
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(0, 0,
                R.string.registration_request, 2, REGISTER_RC,
                new int[]{R.string.register_button, R.string.cancel});
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void confirmUserJoined() {
        super.showMessage(R.string.user_joined_dec_message);
    }

    @Override
    public void updateOptionsMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void expandDetails() {
        tv_dec_title.setVisibility(View.INVISIBLE);
        ib_expand_collapse.setImageResource(R.drawable.ic_action_collapse);
        v_dec_dets.setVisibility(View.VISIBLE);
    }

    @Override
    public void collapseDetails() {
        tv_dec_title.setVisibility(View.VISIBLE);
        ib_expand_collapse.setImageResource(R.drawable.ic_action_expand);
        v_dec_dets.setVisibility(View.GONE);
    }

    @Override
    public void showPrefsButtons(boolean show) {
        if (v_prefs_buttons != null) {
            v_prefs_buttons.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setPrefViewValue(int optPos, PrefValue prefValue) {
        if (mOptListFragment != null) {
            mOptListFragment.setPrefViewValue(optPos, prefValue);
        }
    }

    @Override
    public void showSendPrefsConfirmation(int numBlanks) {
        // Prepare alert dialog message:
        // Get the main message
        String message = getString(R.string.sending_pref_confirmation);
        // Add the num of blank prefs to the message
        if (numBlanks > 0) {
            message += "\n" + getResources().getQuantityString(R.plurals.numBlanks, numBlanks,
                    numBlanks);
        }
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(0, 0, message, 2,
                SEND_PREF_RC, null);
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void showJoinAndSendPrefsConfirmation(int numBlanks) {
        // Prepare alert dialog message:
        // Get the main message
        String message = getString(R.string.join_and_send_pref_confirmation);
        // Add the num of blank prefs to the message
        if (numBlanks > 0) {
            message += "\n" + getResources().getQuantityString(R.plurals.numBlanks, numBlanks,
                    numBlanks);
        }
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(0, 0, message, 2,
                JOIN_AND_SEND_PREF_RC, null);
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void showPrefsSentMessage() {
        super.showMessage(R.string.toast_prefs_sent);
    }

    @Override
    public void setPrefsRead(int parPos) {
        if (mParListFragment != null) {
            mParListFragment.updateParRender(parPos);
        }
    }

    @Override
    public void showConAddedMessage(ParModel par) {
        super.showMessage(par.getFullName() + " " + getString(R.string.toast_contact_added));
    }

    @Override
    public void showUserBlockedMessage(ParModel par) {
        super.showMessage(par.getFullName() + " " + getString(R.string.toast_block));
    }

    @Override
    public void showDeleteDecConfirmation() {
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(
                R.string.deleting_confirmation, 2, DELETE_DEC_RC);
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void showDecDeletedMessage() {
        super.showMessage(R.string.toast_dec_deleted);
    }

    @Override
    public void showExitDecConfirmation() {
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(
                R.string.exiting_confirmation, 2, EXIT_DEC_RC);
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void showDecExitedMessage() {
        super.showMessage(R.string.toast_dec_exited);
    }

    @Override
    public void showCloseDecConfirmation() {
        // Build and show the alert dialog
        AlertDialogFragment confirmationDialog = AlertDialogFragment.newInstance(
                R.string.closing_confirmation, 2, CLOSE_DEC_RC);
        confirmationDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    @Override
    public void updateDecClosed() {
        // Update options menu
        invalidateOptionsMenu();
        // Update options list render
        if (mOptListFragment != null) {
            mOptListFragment.renderOptList(mViewState.dec);
        }
        // Show message to user
        super.showMessage(R.string.toast_dec_closed);
    }

    @Override
    public void shareDec() {
        // Build the public link for this decision
        String sharingLink = Constants.PUBLIC_LINK + mViewState.dec.getSeed();
        // Create sharing intent
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mViewState.dec.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_dig_body) + sharingLink);
        // Launch sharing intent
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_chooser_title)));
    }

    // ==========================================================================
    // Alert dialogs
    // ==========================================================================

    @Override
    public void onDialogPositiveClick(int requestCode) {
        if (requestCode == DELETE_DEC_RC) {
            mPresenter.onDeleteDecConfirmed();
        } else if (requestCode == EXIT_DEC_RC) {
            mPresenter.onExitDecConfirmed();
        } else if (requestCode == SEND_PREF_RC) {
            mPresenter.onSendPrefsConfirmed();
        } else if (requestCode == JOIN_AND_SEND_PREF_RC) {
            mPresenter.onJoinAndSendPrefsConfirmed();
        } else if (requestCode == CLOSE_DEC_RC) {
            mPresenter.onCloseDecConfirmed();
        } else if (requestCode == REGISTER_RC) {
            mPresenter.onRegisterConfirmed();
        }
    }

    @Override
    public void onDialogNegativeClick(int requestCode) {
        // no-op
    }
}