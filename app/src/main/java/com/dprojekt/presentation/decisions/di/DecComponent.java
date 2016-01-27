package com.dprojekt.presentation.decisions.di;

import com.dprojekt.presentation.common.di.ActivityComponent;
import com.dprojekt.presentation.common.di.ActivityModule;
import com.dprojekt.presentation.common.di.ApplicationComponent;
import com.dprojekt.presentation.common.di.PerActivity;
import com.dprojekt.presentation.decisions.decisionDetail.DecDetActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects decision specific activities.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface DecComponent extends ActivityComponent {
    void inject(DecDetActivity decDetActivity);
}