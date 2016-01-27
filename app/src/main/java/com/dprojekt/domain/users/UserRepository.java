package com.dprojekt.domain.users;

import android.support.annotation.NonNull;

import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.images.ImgModel;
import com.dprojekt.domain.users.models.ConModel;

import rx.Observable;

/**
 * Interface that represents a Repository to manage {@link ConModel} related data.
 */
public interface UserRepository {

    /**
     * Get an {@link rx.Observable} which will emit a {@link ImgModel} of the user image.
     *
     * @param userId The user ID used to retrieve its data.
     */
    Observable<ImgModel> checkUserImg(long userId);

    /**
     * Get an {@link rx.Observable} which will add an user as a contact and only emit a termination
     * notification.
     *
     * @param userId The ID of the user to be added as contact
     * @param name The name of the user to be added as contact
     * @param lastName The last name of the user to be added as contact
     */
    Observable addCon(long userId, String name, String lastName);

    /**
     * Get an {@link rx.Observable} which will block an user and only emit a termination notification.
     *
     * @param userId The ID of the user to be blocked
     */
    Observable blockUser(long userId);
}