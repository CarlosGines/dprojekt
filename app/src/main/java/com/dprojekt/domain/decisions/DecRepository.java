package com.dprojekt.domain.decisions;

import com.dprojekt.domain.decisions.models.DecModel;
import com.dprojekt.domain.decisions.models.DecUpdModel;
import com.dprojekt.domain.decisions.models.OptModel;
import com.dprojekt.domain.decisions.models.ParModel;
import com.dprojekt.domain.decisions.models.PrefValue;
import com.dprojekt.domain.images.ImgModel;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository to manage {@link DecModel} related data.
 */
public interface DecRepository {

    /**
     * Get an {@link rx.Observable} which will emit a {@link DecModel}.
     *
     * @param decId    The decision ID used to retrieve its data.
     * @param myUserId The user id of the current user
     */
    Observable<DecModel> getDecDet(long decId, long myUserId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link DecModel} preview from a decision seed.
     *
     * @param decSeed  The decision seed used to identify it in public links.
     * @param myUserId The user ID of the current user.
     */
    Observable<DecModel> previewDecDet(String decSeed, long myUserId);

    /**
     * Get an {@link rx.Observable} which will update the {@link DecModel} last read time and only
     * emit termination notifications.
     *
     * @param decId     The decision ID used to update read time.
     * @param isPreview Whether the decision is a preview accessed form a public link.
     */
    Observable updateDecReadTime(long decId, boolean isPreview);

    /**
     * Get an {@link rx.Observable} which will make the user participant of a {@link DecModel} and
     * emit the {@link DecModel} with the user as participant.
     *
     * @param dec      The decision we are joining.
     * @param myUserId The current user ID.
     */
    Observable<DecModel> joinDec(DecModel dec, long myUserId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link ImgModel} of the decision image.
     *
     * @param decId The decision ID used to retrieve its data.
     */
    Observable<ImgModel> checkDecImg(long decId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link ImgModel} of the option image .
     *
     * @param optId The option ID used to retrieve its data.
     */
    Observable<ImgModel> checkOptImg(long optId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link ImgModel} of the participant image .
     *
     * @param userId The user ID used to retrieve its data.
     */
    Observable<ImgModel> checkParImg(long userId);

    /**
     * Get an {@link rx.Observable} which will set an option preference and only emit termination
     * notifications.
     *
     * @param decId     The decision ID used to set option preference value.
     * @param isPreview Whether the decision is a preview accessed form a public link.
     * @param optId     The option ID used to set option preference value.
     * @param prefValue The value used to set option preference value.
     */
    Observable setPref(long decId, boolean isPreview, long optId, PrefValue prefValue);

    /**
     * Get an {@link rx.Observable} which send preferences and will only emit termination
     * notifications.
     *
     * @param decId     The ID of the decision we are sending new prefs to.
     * @param isPreview Whether the decision is a preview accessed form a public link.
     * @param par       The user as participant with the preferences to send in it.
     */
    Observable sendPrefs(long decId, boolean isPreview, ParModel par);

    /**
     * Get an {@link rx.Observable} which will set prefs as read and will only emit termination
     * notifications.
     *
     * @param decIdLcl  The id of the decision
     * @param parUserId The user id of the participant read
     */
    Observable setParPrefsRead(long decIdLcl, long parUserId);

    /**
     * Get an {@link rx.Observable} which will exit the user from a decision and will only emit
     * termination notifications.
     *
     * @param decId The id of the decision
     * @param isPreview Whether the decision is a preview accessed form a public link.
     */
    Observable exitDec(long decId, boolean isPreview);

    /**
     * Get an {@link rx.Observable} which will delete a decision and will only emit termination
     * notifications.
     *
     * @param decId The id of the decision
     * @param isPreview Whether the decision is a preview accessed form a public link.
     */
    Observable deleteDec(long decId, boolean isPreview);

    /**
     * Get an {@link rx.Observable} which will close a decision and will only emit termination
     * notifications.
     *
     * @param decId The id of the decision
     * @param isPreview Whether the decision is a preview accessed form a public link.
     */
    Observable closeDec(long decId, boolean isPreview);

    /**
     * Get an {@link rx.Observable} which will emit a {@link List< OptModel >}.
     *
     * @param decId     The decision id used to retrieve options data.
     * @param isPreview Whether the decision is a preview accessed form a public link.
     * @param decSeed   The decision seed used to retrieve options data.
     */
    Observable<List<OptModel>> getOptList(long decId, boolean isPreview, String decSeed);
}