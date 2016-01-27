package com.dprojekt.domain.users.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.users.models.ConModel;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * check if the image of an user ({@link ConModel} or {@link ParModel}) needs to be updated.
 */
@PerActivity
public class CheckParImgUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mUserId;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public CheckParImgUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param userId ID of the User whose image we are checking.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long userId, Subscriber useCaseSubscriber) {
        mUserId = userId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.checkParImg(mUserId);
    }
}
