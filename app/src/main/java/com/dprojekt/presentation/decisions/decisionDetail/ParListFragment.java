package com.dprojekt.presentation.decisions.decisionDetail;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dprojekt.R;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.users.models.ConModel;

/**
 * Fragment for the participants page on {@link DecDetActivity}
 */
public class ParListFragment extends ListFragment {

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
        mActivity.setParListFragment(this);
        mPresenter = mActivity.getPresenter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView parListFragmentView = (ListView) inflater.inflate(R.layout.fragment_par_list,
                container, false);
        registerForContextMenu(parListFragmentView);
        return parListFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Render details if available
        DecModel dec = mActivity.getViewState().dec;
        if (dec != null) {
            renderParList(dec);
        }
    }

    // ==========================================================================
    // User input
    // ==========================================================================

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mPresenter.onParItemClick(position);
    }

    // ==========================================================================
    // Context menu
    // ==========================================================================

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        // Change menu depending on the participants contact role
        ParModel par = (ParModel) getListAdapter().getItem(info.position);
        // If it is the user o a blocked user, don't show at all
        if (!(par.getConRole() == ConModel.IS_ME_ROLE) &&
                !(par.getConRole() == ConModel.I_BLOCKED_ROLE)) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.participants_list_ctx_menu, menu);
            // If it is a contact, don't show contact role update actions
            if (par.getConRole() == ConModel.IS_CONTACT_ROLE) {
                menu.removeItem(R.id.menu_add_contacts);
                menu.removeItem(R.id.menu_block);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        ParModel par = (ParModel) getListAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.menu_view_profile:
                mPresenter.onParViewProfClick(par);
                return true;
            case R.id.menu_add_contacts:
                mPresenter.onParAddConClick(par);
                return true;
            case R.id.menu_block:
                mPresenter.onParBlockClick(par);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Render the participants list */
    public void renderParList(DecModel dec) {
        // Render the ListView, keeping the scroll if existing.
        if (this.isVisible()) {
            ListView parListView = getListView();
            if (parListView != null) {
                // Save first ListView item index and top position
                int index = parListView.getFirstVisiblePosition();
                View v = parListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                // Set the custom adapter
                ParListViewAdapter adapter = new ParListViewAdapter((DecDetActivity) getActivity(),
                        dec.getParList(), dec.getAdminId());
                setListAdapter(adapter);
                // Restore scroll from first ListView item index and position
                parListView.setSelectionFromTop(index, top);
            }
        }
    }

    /** Update the view of a single participant */
    public void updateParRender(int pos) {
        // Update a single row from adapter
        if(this.isVisible()) {
            ListView parListView = getListView();
            if (parListView != null) {
                int fvp = parListView.getFirstVisiblePosition();
                if (pos >= fvp && pos <= parListView.getLastVisiblePosition()) {
                    View view = parListView.getChildAt(pos - fvp);
                    parListView.getAdapter().getView(pos, view, parListView);
                }
            }
        }
    }
}
