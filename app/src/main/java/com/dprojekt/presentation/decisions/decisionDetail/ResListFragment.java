package com.dprojekt.presentation.decisions.decisionDetail;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.DecModel;

/**
 * Fragment for the results page on {@link DecDetActivity}
 */
public class ResListFragment extends ListFragment {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Activity containing this fragment */
    private DecDetActivity mActivity;

    /** Presenter (Model-View-Presenter) for Decision Detail view */
    private DecDetPresenter mPresenter;

    // ==========================================================================
    // Fragment Lifecycle methods
    // ==========================================================================

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (DecDetActivity) getActivity();
        mActivity.setResFragment(this);
        mPresenter = mActivity.getPresenter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View optListFragmentView = inflater.inflate(R.layout.fragment_res_list, container, false);
        return optListFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Render details if available
        DecModel dec = mActivity.getViewState().dec;
        if (dec != null) {
            renderRes(dec);
        }
    }

    // ==========================================================================
    // User input
    // ==========================================================================

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mPresenter.onResItemClick(position);
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Render the results list */
    public void renderRes(DecModel dec) {
        // Render the ListView, keeping the scroll if existing.
        if (this.isVisible()) {
            ListView resListView = getListView();
            if (resListView != null) {
                // Save first ListView item index and top position
                int index = resListView.getFirstVisiblePosition();
                View v = resListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                // Set the custom adapter
                ResListViewAdapter adapter = new ResListViewAdapter(mActivity, dec.getResList());
                setListAdapter(adapter);
                // Restore scroll from first ListView item index and position
                resListView.setSelectionFromTop(index, top);
            }
        }
    }
}
