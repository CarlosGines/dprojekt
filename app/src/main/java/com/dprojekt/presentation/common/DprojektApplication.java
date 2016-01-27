package com.dprojekt.presentation.common;

import android.app.Application;

import com.dprojekt.presentation.common.di.ApplicationComponent;
import com.dprojekt.presentation.common.di.ApplicationModule;


/**
 * Android Main Application
 */
public class DprojektApplication extends Application {

    /** Main Application component for dependency injection. */
    private ApplicationComponent mApplicationComponent;

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    public ApplicationComponent getApplicationComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();        }
        return mApplicationComponent;
    }

    /**
     * Reset Main Application component for dependency injection.
     */
    public void resetApplicationComponent() {
        mApplicationComponent = null;
    }
}
