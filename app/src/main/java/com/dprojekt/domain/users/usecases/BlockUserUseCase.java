package com.dprojekt.domain.users.usecases;

import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.users.UserRepository;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * block an user.
 */
@PerActivity
public class BlockUserUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mUserId;
    private final UserRepository mUserRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public BlockUserUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mUserRepository = userRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param userId ID of the User we are blocking.
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
        return mUserRepository.blockUser(mUserId);
    }
}
