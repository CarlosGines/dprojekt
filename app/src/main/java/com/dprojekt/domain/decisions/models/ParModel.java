package com.dprojekt.domain.decisions.models;

import com.dprojekt.domain.images.ImgModel;

import java.util.List;

/**
 * Domain layer model for a participant within a decision.
 */
public class ParModel {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // Participation status types
    public static final int NOT_RECEIVED_STATUS = 1;
    public static final int NEW_STATUS = 2;
    public static final int READ_STATUS = 3;
    public static final int PREFS_SENT_STATUS = 4;

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** ID of the participant */
    private long id;

    /** ID of the user corresponding to this participant */
    private long userId;

    /** Name of this participant */
    private String name;

    /** Last name of this participant */
    private String lastName;

    /** Image of this participant */
    private ImgModel img;

    /** Contact role of this participant towards the user */
    private int conRole;

    /** Participation status of this participant in the decision */
    private int parStatus;

    /** List of preferences sent by this participant */
    private List<PrefModel> prefList;

    /** Timestamp in seconds when the participant sent preferences */
    private int prefsUpdTime;

    /** Whether the user has read the preferences of this participant */
    private boolean prefsUnread;

    /** Whether this participant has left this decision */
    private boolean exited;

    // ==========================================================================
    // Constructor
    // ==========================================================================

    public ParModel(long id) {
        this.id = id;
    }

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public long getId() {
        return id;
    }

    public void setId(long userId) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ImgModel getImg() {
        return img;
    }

    public void setImg(ImgModel img) {
        this.img = img;
    }

    public int getConRole() {
        return conRole;
    }

    public void setConRole(int conRole) {
        this.conRole = conRole;
    }

    public int getParStatus() {
        return parStatus;
    }

    public void setParStatus(int parStatus) {
        this.parStatus = parStatus;
    }

    public List<PrefModel> getPrefList() {
        return prefList;
    }

    public void setPrefList(List<PrefModel> prefList) {
        this.prefList = prefList;
    }

    public int getPrefsUpdTime() {
        return prefsUpdTime;
    }

    public void setPrefsUpdTime(int prefsUpdTime) {
        this.prefsUpdTime = prefsUpdTime;
    }

    public boolean isPrefsUnread() {
        return prefsUnread;
    }

    public void setPrefsUnread(boolean prefsUnread) {
        this.prefsUnread = prefsUnread;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    // ==========================================================================
    // Helper methods
    // ==========================================================================

    public String getFullName() {
        return name + (lastName != null ? (" " + lastName) : "");

    }

    // ==========================================================================
    // Object methods
    // ==========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParModel parModel = (ParModel) o;

        return id == parModel.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}