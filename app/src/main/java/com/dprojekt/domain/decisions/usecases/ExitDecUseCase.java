package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case of the user
 * leaving a {@link DecModel}
 */
@PerActivity
public class ExitDecUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mDecId;
    private boolean mIsPreview;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public ExitDecUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param decId ID of the Decision we are exiting.
     * @param isPreview whether the decision is a preview accessed form a public link.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long decId, boolean isPreview, Subscriber useCaseSubscriber) {
        mDecId = decId;
        mIsPreview = isPreview;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.exitDec(mDecId, mIsPreview);
    }
}
