package com.dprojekt.presentation.common.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dprojekt.data.common.DbHelper;
import com.dprojekt.data.common.JobExecutor;
import com.dprojekt.data.decisions.DecDataRepository;
import com.dprojekt.data.users.UserDataRepository;
import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.users.UserRepository;
import com.dprojekt.presentation.common.rx.UIThread;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the mApplication lifecycle.
 */
@Module
public class ApplicationModule {
    private final Context mApplicationCtx;

    public ApplicationModule(Context applicationCtx) {
        mApplicationCtx = applicationCtx;
    }

    @Provides
    @PerUser
    Context provideApplicationContext() {
        return mApplicationCtx;
    }

    @Provides
    @PerUser
    // Provides implementation of ThreadExecutor from the data layer.
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @PerUser
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @PerUser
    // Provides implementation of DecRepository from the data layer.
    DecRepository provideDecRepository(DecDataRepository decDataRepository) {
        return decDataRepository;
    }

    @Provides
    @PerUser
    // Provides implementation of UserRepository from the data layer.
    UserRepository provideUserRepository(UserDataRepository userDataRepository) {
        return userDataRepository;
    }
}
