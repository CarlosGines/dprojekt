package com.dprojekt.domain.decisions.usecases;

import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.decisions.DecRepository;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.domain.decisions.models.PrefValue;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * set the {@link PrefValue} of the current user for an {@link OptModel}. This preference is
 * private to the user until {@link SendPrefsUseCase} is executed.
 */
public class SetPrefUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mDecId;
    private boolean mIsPreview;
    private long mOptId;
    private PrefValue mPrefValue;
    private final DecRepository mDecRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public SetPrefUseCase(DecRepository decRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mDecRepository = decRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param decId ID of the Decision we are setting preferences for.
     * @param isPreview whether the decision is a preview accessed form a public link.
     * @param optId ID of the Option we are setting preferences for.
     * @param prefValue the preference value we are setting.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long decId, boolean isPreview, long optId, PrefValue prefValue,
                        Subscriber useCaseSubscriber) {
        mDecId = decId;
        mIsPreview = isPreview;
        mOptId = optId;
        mPrefValue = prefValue;
        super.execute(useCaseSubscriber);
    }

    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mDecRepository.setPref(mDecId, mIsPreview, mOptId, mPrefValue);
    }
}
