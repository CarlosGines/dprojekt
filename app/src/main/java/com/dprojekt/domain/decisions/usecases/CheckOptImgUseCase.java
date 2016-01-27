package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * check if the image of a {@link OptModel} needs to be updated.
 */
@PerActivity
public class CheckOptImgUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mOptId;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public CheckOptImgUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param optId ID of the Option whose image we are checking.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long optId, Subscriber useCaseSubscriber) {
        mOptId = optId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.checkOptImg(mOptId);
    }
}
