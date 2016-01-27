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
 * This class is an implementation of {@link UseCase} that represents a use case to
 * check if the image of a {@link DecModel} needs to be updated.
 */
@PerActivity
public class CheckDecImgUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mDecId;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public CheckDecImgUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param decId ID of the Decision whose image we are checking.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long decId, Subscriber useCaseSubscriber) {
        mDecId = decId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.checkDecImg(mDecId);
    }
}
