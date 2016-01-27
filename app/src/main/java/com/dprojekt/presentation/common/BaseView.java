package com.dprojekt.presentation.common;

/**
 * Base view class (from Model-View-Presenter) for every Activity in this application.
 */
public interface BaseView {
    /**
     * Show the default error message
     * @param e
     */
    void showErrorMessage(Throwable e);
    /**
     * Show or hide the loading view
     */
    void setLoading(boolean isLoading);
    /**
     * Show or hide the no connection view
     */
    void setNoConnection(boolean isNoConnection);
    /**
     * Show the dialog of app version out of date
     */
    void showAppOutOfDate ();
}
