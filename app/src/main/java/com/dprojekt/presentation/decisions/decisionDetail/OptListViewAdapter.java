package com.dprojekt.presentation.decisions.decisionDetail;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.domain.decisions.models.PrefValue;

import java.util.List;

/**
 * Custom {@link ArrayAdapter} of {@link OptModel}s for the ListView on {@link OptListFragment}
 */
public class OptListViewAdapter extends ArrayAdapter<OptModel> implements
        PrefSetterView.OnPrefSetListener {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Activity using this array adapter */
    private DecDetActivity mActivity;

    /** Inflater to create the views from XML */
    private LayoutInflater mInflater;

    /** Whether the decision is open */
    private boolean mIsOpen;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    public OptListViewAdapter(DecDetActivity activity, List<OptModel> optList, boolean isOpen) {
        super(activity, 0, optList);
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mIsOpen = isOpen;
    }

    // ==========================================================================
    // ArrayAdapter<OptModel> methods
    // ==========================================================================

    // Creates the View for every item of the list
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // View to show
        View resultView;
        // Item to show
        OptModel opt = getItem(position);

        // Used recycled view if possible for smoother scrolling (View Holder pattern).
        if (convertView == null) {
            resultView = mInflater.inflate(R.layout.row_opt, parent, false);
        } else {
            resultView = convertView;
        }

        PrefSetterView psv = (PrefSetterView) resultView.findViewById(R.id.pref_area);
        // Set event listener for the pref setter
        psv.setOnPrefSetListener(this);
        // Hide input
        psv.showPrefInput(false);
        // Set position as tag
        psv.setTag(position);
        // Render pref value
        psv.setPrefRender(opt.getUserPrefValue());

        if (!mIsOpen) {
            // If closed, update row view accordingly
            Resources r = mActivity.getResources();
            resultView.setBackgroundColor(r.getColor(android.R.color.darker_gray));
            // And disable pref setting
            psv.setPrefSettingEnabled(false);
        }

        // Set the title
        TextView text = (TextView) resultView.findViewById(android.R.id.text1);
        text.setText(opt.getTitle());

        // Load the image and check if update form server needed
        UpdatableImageView uiv_opt_img = (UpdatableImageView) psv.findViewById(R.id.option_image);
        uiv_opt_img.loadAndCheckOptImg(opt, mActivity.getPresenter().onCheckOptImg());
        // Set position as tag for image too
        uiv_opt_img.setTag(position);
        // Set on click handler
        uiv_opt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show option image in custom dialog
                OptImgDialogFragment idf = OptImgDialogFragment.newInstance((int) v.getTag(),
                        OptImgDialogFragment.OPT_IMG);
                idf.show(mActivity.getFragmentManager(), OptImgDialogFragment.TAG);
            }
        });

        return resultView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    // ==========================================================================
    // OnPrefSetListener implementation
    // ==========================================================================

    @Override
    public int getCurrentTutStep() {
        return mActivity.getViewState().prefSetterTutStep;
    }

    @Override
    public void onNewTutStep(int step) {
        mActivity.getViewState().prefSetterTutStep = step;
        mActivity.getSharedPreferences(Constants.DEFAULT_PREFS, Activity.MODE_PRIVATE).edit()
                .putInt(DecDetActivity.KEY_PREF_SETTER_TUT_STEP, step).apply();
    }

    @Override
    public void onPrefSet(PrefSetterView psv, PrefValue prefValue) {
        int pos = (Integer) psv.getTag();
        mActivity.getPresenter().onPrefSet(pos, prefValue);
    }

    @Override
    public void onPrefRender(PrefSetterView psv, PrefValue prefValue) {
    }
}
