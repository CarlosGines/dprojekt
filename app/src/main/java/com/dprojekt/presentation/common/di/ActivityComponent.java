package com.dprojekt.presentation.common.di;

import android.app.Activity;

import dagger.Component;

/**
 * A base component upon which activities' components may depend.
 * Activity-level components should extend this component.
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link PerActivity}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    //Exposed to sub-graphs.
    Activity activity();
}
