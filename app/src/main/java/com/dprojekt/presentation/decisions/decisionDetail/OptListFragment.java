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
import com.dprojekt.domain.decisions.models.PrefValue;

/**
 * Fragment for the options page on {@link DecDetActivity}
 */
public class OptListFragment extends ListFragment {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** Activity containing this fragment */
    private DecDetActivity mActivity;

    // ==========================================================================
    // Fragment Lifecycle methods
    // ==========================================================================

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (DecDetActivity) getActivity();
        mActivity.setOptListFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View optListFragmentView = inflater.inflate(R.layout.fragment_opt_list, container, false);
        return optListFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Render details if available
        DecModel dec = mActivity.getViewState().dec;
        if (dec != null) {
            renderOptList(dec);
        }
    }

    // ==========================================================================
    // ListFragment methods
    // ==========================================================================

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Show pref setting view input
        ((PrefSetterView) v).showPrefInput(true);
        super.onListItemClick(l, v, position, id);
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Render the options list */
    public void renderOptList(DecModel dec) {
        // Render the ListView, keeping the scroll if existing.
        if (this.isVisible()) {
            ListView optListView = getListView();
            if (optListView != null) {
                // Save first ListView item index and top position
                int index = optListView.getFirstVisiblePosition();
                View v = optListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                // Set the custom adapter
                OptListViewAdapter adapter = new OptListViewAdapter((DecDetActivity) getActivity(),
                        dec.getOptList(), dec.getState() != DecModel.CLOSED_STATE);
                setListAdapter(adapter);
                // Restore scroll from first ListView item index and position
                optListView.setSelectionFromTop(index, top);
            }
        }
    }

    /** Set the pref setter view for an option to render the given PrefValue */
    public void setPrefViewValue(int optPos, PrefValue prefValue) {
        if (this.isVisible()) {
            // Get the corresponding pref setter view
            ListView optListView = getListView();
            PrefSetterView psv = (PrefSetterView) optListView.getChildAt(optPos -
                    optListView.getFirstVisiblePosition());
            if (psv != null) {
                // Render the given PrefValue
                psv.setPrefRender(prefValue);
            }
        }
    }
}
