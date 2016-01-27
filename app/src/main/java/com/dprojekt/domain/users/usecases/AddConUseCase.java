package com.dprojekt.domain.users.usecases;

import com.dprojekt.domain.common.rx.PostExecutionThread;
import com.dprojekt.domain.common.rx.ThreadExecutor;
import com.dprojekt.domain.common.UseCase;
import com.dprojekt.domain.users.UserRepository;
import com.dprojekt.presentation.common.di.PerActivity;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case to
 * add a user as a contact.
 */
@PerActivity
public class AddConUseCase extends UseCase {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    private long mUserId;
    private String mName;
    private String mLastName;
    private final UserRepository mUserRepository;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    @Inject
    public AddConUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mUserRepository = userRepository;
    }

    // ==========================================================================
    // Public methods
    // ==========================================================================

    /** Start asynchronous execution of this use case
     *
     * @param userId ID of the User we are adding as contact.
     * @param name name of the User we are adding as contact.
     * @param lastName last name of the User we are adding as contact.
     * @param useCaseSubscriber subscriber to receive notifications from Observables.
     */
    public void execute(long userId, String name, String lastName, Subscriber useCaseSubscriber) {
        mUserId = userId;
        mName = name;
        mLastName = lastName;
        super.execute(useCaseSubscriber);
    }
    // ==========================================================================
    // UseCase methods
    // ==========================================================================

    @Override
    protected Observable buildUseCaseObservable() {
        return mUserRepository.addCon(mUserId, mName, mLastName);
    }
}
