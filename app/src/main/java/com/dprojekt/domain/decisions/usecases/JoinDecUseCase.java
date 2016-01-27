package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for the user to
 * join as {@link ParModel} a {@link DecModel} he/she is previewing.
 */
@PerActivity
public class JoinDecUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private DecModel mDec;
    private long mMyUserId;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public JoinDecUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param dec the Decision we are trying to join.
     * @param myUserId ID of the current user trying to join a decision.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(DecModel dec, long myUserId, Subscriber useCaseSubscriber) {
        mDec = dec;
        mMyUserId = myUserId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable<DecModel> buildUseCaseObservable() {
        if(!mDec.getParIdList().contains(mMyUserId)){
            // Join user into the decision
            return mDecRepository.joinDec(mDec, mMyUserId);
        } else {
            return Observable.error(new Exception("Error joining decision: User already is participant"));
        }
    }
}
