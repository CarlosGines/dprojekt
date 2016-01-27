package com.dprojekt.presentation.decisions.decisionDetail;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.ParModel;

import java.util.Collections;
import java.util.List;

/**
 * Custom {@link ArrayAdapter} of {@link ParModel}s for the ListView on {@link ParListFragment}
 */
public class ParListViewAdapter extends ArrayAdapter<ParModel> {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Activity using this array adapter */
    private DecDetActivity mActivity;

    /** Inflater to create the views from XML */
    private LayoutInflater mInflater;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    public ParListViewAdapter(DecDetActivity activity, List<ParModel> parList, long adminId) {
        super(activity, 0, parList);
        // The admin must be the first participant in the list
        checkAdminFirstPar(parList, adminId);
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    /** Set the admin as the first participant in the list if not so */
    private void checkAdminFirstPar(List<ParModel> parList, long adminId) {
        for (int i = 0; i < parList.size(); i++) {
            if (parList.get(i).getUserId() == adminId) {
                if (i != 0) {
                    Collections.swap(parList, 0, i);
                }
                break;
            }
        }
    }

    // ==========================================================================
    // ArrayAdapter<ParModel> methods
    // ==========================================================================

    // Creates the View for every item of the list
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // View to show
        View resultView;

        // Used recycled view if possible for smoother scrolling (View Holder
        // pattern).
        if (convertView == null) {
            resultView = mInflater.inflate(R.layout.row_par, parent, false);
        } else {
            resultView = convertView;
        }

        // Is the admin (the first view)
        if (position == 0) {
            resultView.findViewById(R.id.is_admin).setVisibility(View.VISIBLE);
        } else {
            resultView.findViewById(R.id.is_admin).setVisibility(View.GONE);
        }

        // Item to show
        ParModel par = getItem(position);

        // Set participation state icon
        ImageView partStateIcon = (ImageView) resultView.findViewById(R.id.part_status_icon);
        switch (par.getParStatus()) {
            case ParModel.NOT_RECEIVED_STATUS:
                partStateIcon.setImageResource(R.drawable.ic_schedule_grey600_18dp);
                break;
            case ParModel.NEW_STATUS:
                partStateIcon.setImageResource(R.drawable.ic_markunread_grey600_18dp);
                break;
            case ParModel.READ_STATUS:
                partStateIcon.setImageResource(R.drawable.ic_drafts_grey600_18dp);
                break;
            case ParModel.PREFS_SENT_STATUS:
                // Preferences are read
                if (par.isPrefsUnread()) {
                    partStateIcon.setImageResource(R.drawable.prefs_new);
                } else {
                    partStateIcon.setImageResource(R.drawable.prefs_read);
                }
                break;
        }

        // Set the participant full name
        TextView text = (TextView) resultView.findViewById(R.id.participant_name);
        text.setText(par.getFullName());

        // Change the view if participant left the decision
        Resources r = mActivity.getResources();
        if (!par.isExited()) {
            text.setTextColor(r.getColor(android.R.color.black));
        } else {
            text.setTextColor(r.getColor(android.R.color.darker_gray));
        }

        // Load the image and check if update form server needed
        UpdatableImageView uiv_par_img = (UpdatableImageView) resultView.findViewById(R.id.participant_image);
        uiv_par_img.loadAndCheckParImg(par, mActivity.getPresenter().onCheckUserImg());

        return resultView;
    }
}