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
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to a specific {@link DecModel} from a public link.
 */
@PerActivity
public class PreviewDecDetUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mMyUserId;
    private String mDecSeed;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public PreviewDecDetUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param decSeed seed of the Decision we are retrieving the data from.
     * @param myUserId ID of the current user trying to retrieve a decision.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(String decSeed, long myUserId, Subscriber useCaseSubscriber) {
        mDecSeed = decSeed;
        mMyUserId = myUserId;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.previewDecDet(mDecSeed, mMyUserId);
    }
}
