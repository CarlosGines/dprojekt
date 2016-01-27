package com.dprojekt.presentation.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dprojekt.BuildConfig;
import com.dprojekt.R;
import com.dprojekt.data.common.ServerResponseException;
import com.dprojekt.presentation.common.di.ActivityModule;
import com.dprojekt.presentation.common.di.ApplicationComponent;

import org.json.JSONException;

/**
 * Base {@link android.app.Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private boolean mSendScreenTracking = true;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================


    public void setSendScreenTracking(boolean sendScreenTracking) {
        mSendScreenTracking = sendScreenTracking;
    }

    // ==========================================================================
    // Activity lifecycle methods
    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Google Analytics Screen tracking
        if (mSendScreenTracking) {
            TrackingUtils.sendActivityScreen(this);
        }
    }

    // ==========================================================================
    // Custom methods
    // ==========================================================================

    // Initializes the toolbar for this activity
    protected void initToolbar() {
        // Set AppCompat action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // ==========================================================================
    // BaseView implementation
    // ==========================================================================

    @Override
    public void showErrorMessage(Throwable e) {
        Throwable cause = e.getCause();
        if (BuildConfig.DEBUG) {
            // Custom RuntimeExceptions must have explicit debug message set as the  detail message.
            Toast.makeText(this, "Debug: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            String errorHint = "";
            if (cause instanceof ServerResponseException) {
                errorHint = " (" + ((ServerResponseException) cause).getErrorCode() + ")";
            } else if (cause instanceof JSONException) {
                errorHint = " (JSON)";
            }
            Toast.makeText(this, getString(R.string.unexpected_error) + errorHint,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showAppOutOfDate() {
        AppOutOfDateDialogFragment appOodDialog = new AppOutOfDateDialogFragment();
        appOodDialog.show(getFragmentManager(), AlertDialogFragment.TAG);
    }

    // ==========================================================================
    // Dialogs
    // ==========================================================================

    /** Generic dialog to be shown when the application is out of date according to the server */
    public static class AppOutOfDateDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.out_of_date_title);
            builder.setMessage(R.string.out_of_date_msg);
            // Set button to redirect to Google Play for app update
            builder.setPositiveButton(R.string.update,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.dprojekt");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
            return builder.create();
        }
    }

    // ==========================================================================
    // Injection helper methods
    // ==========================================================================

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((DprojektApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}