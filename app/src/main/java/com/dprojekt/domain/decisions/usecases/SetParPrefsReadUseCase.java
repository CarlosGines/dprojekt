package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * set the preferences of a participant as read.
 */
@PerActivity
public class SetParPrefsReadUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mDecId;
    private long mParId;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public SetParPrefsReadUseCase(DecRepository decRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param decId ID of the Decision the preferences belong to.
     * @param parId ID of the Participant we are setting the Preferences as read.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long decId, long parId, Subscriber useCaseSubscriber) {
        mDecId = decId;
        mParId = parId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.setParPrefsRead(mDecId, mParId);
    }
}
