package com.dprojekt.presentation.decisions.decisionDetail;

import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.domain.decisions.models.ResModel;

import java.util.List;

/**
 * Custom {@link ArrayAdapter} of {@link ResModel}s for the ListView on {@link ResListFragment}
 */
public class ResListViewAdapter extends ArrayAdapter<OptModel> {

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

    public ResListViewAdapter(DecDetActivity activity, List<OptModel> optList) {
        super(activity, 0, optList);
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    // ==========================================================================
    // ArrayAdapter<OptModel> methods
    // ==========================================================================

    // Creates the View for every item of the list
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // View to show
        View resultView;

        // Used recycled view if possible for smoother scrolling (View Holder pattern).
        if (convertView == null || convertView.getTag() != null) {
            resultView = mInflater.inflate(R.layout.row_res, parent, false);
        } else {
            resultView = convertView;
        }

        // Item to show
        OptModel opt = getItem(position);

        // Set the title
        TextView text = (TextView) resultView.findViewById(android.R.id.text1);
        text.setText(opt.getTitle());

        // Load the image and check if update form server needed
        UpdatableImageView uiv_opt_img = (UpdatableImageView) resultView.findViewById(R.id.option_image);
        uiv_opt_img.loadAndCheckOptImg(opt, mActivity.getPresenter().onCheckOptImg());
        // Set position as tag for image
        uiv_opt_img.setTag(position);
        // Set on click handler
        uiv_opt_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show option image in custom dialog
                OptImgDialogFragment idf = OptImgDialogFragment.newInstance((int) v.getTag(),
                        OptImgDialogFragment.RES_IMG);
                idf.show(mActivity.getFragmentManager(), OptImgDialogFragment.TAG);
            }
        });

        // Ranking position text view
        TextView posView = (TextView) resultView.findViewById(R.id.pos);
        posView.setText(" " + (position + 1) + ".");

        // Ranking position background
        ShapeDrawable posBgDrawable = new ShapeDrawable(new OvalShape());
        View posBadgeView = resultView.findViewById(R.id.pos_badge);
        posBadgeView.setBackgroundDrawable(posBgDrawable);

        // Set special colors for ranking position 1.
        Resources r = mActivity.getResources();
        if (position == 0) {
            posView.setTextColor(r.getColor(android.R.color.white));

            ResModel res = opt.getResult();
            if (res.getRejects() > 0) {
                posBgDrawable.getPaint().setColor(r.getColor(R.color.red_pref));
            } else if (res.getFavs() > 0) {
                posBgDrawable.getPaint().setColor(r.getColor(R.color.yellow_pref));
            } else {
                posBgDrawable.getPaint().setColor(r.getColor(R.color.green_pref));
            }
        } else {
            posView.setTextColor(text.getTextColors());
            posBgDrawable.getPaint().setColor(r.getColor(R.color.grey_100));
        }

        return resultView;
    }
}