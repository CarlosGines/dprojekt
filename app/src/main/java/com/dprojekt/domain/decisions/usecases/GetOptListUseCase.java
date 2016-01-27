package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.presentation.common.di.PerActivity;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * retrieve data related to the {@link List <OptModel>} of a specific {@link DecModel}.
 */
@PerActivity
public class GetOptListUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mDecId;
    private String mDecSeed;
    private boolean mIsPreview;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public GetOptListUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================


    /** Start asynchronous execution of this use case
     *
     * @param decId ID of the Decision we are retrieving the Options List from.
     * @param decSeed seed of the Decision we are retrieving the Options List from.
     * @param isPreview whether the decision is a preview accessed form a public link.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long decId, String decSeed, boolean isPreview, Subscriber useCaseSubscriber) {
        mDecId = decId;
        mDecSeed = decSeed;
        mIsPreview = isPreview;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.getOptList(mDecId, mIsPreview, mDecSeed);
    }
}
