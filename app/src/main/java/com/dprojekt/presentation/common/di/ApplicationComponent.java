package com.dprojekt.presentation.common.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dprojekt.connectivity.notifications.GcmIntentService;
import com.dprojekt.data.common.DbHelper;
import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.users.UserRepository;
import com.dprojekt.presentation.common.BaseActivity;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@PerUser // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs:

    Context context();

    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();

    DecRepository decRepository();
    UserRepository userRepository();
}
