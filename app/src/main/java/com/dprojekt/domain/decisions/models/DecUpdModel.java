package com.dprojekt.domain.decisions.models;

/**
 * Domain layer model for a decision update.
 */
public class DecUpdModel {

	// ==========================================================================
	// Constants
	// ==========================================================================

	// Decision update actions
	public static final int PREF_SENT_ACTION = 1;
	public static final int DEC_CLOSED_ACTION = 2;
	public static final int PAR_EXITED_ACTION = 3;

	// ==========================================================================
	// Member variables
	// ==========================================================================

	/** ID of the Decision Update */
	private long id;

	/** Typ of action of this update: PREF_SENT, DEC_CLOSED, PAR_EXITED */
	private long action;

	/** ID of the Decision this Update belongs to */
	private long decisionId;

	/** ID of the Participant whi created this Updatd */
	private long parId;

	/** Whether the user has read this update */
	private boolean unread;

	/** Timestamp in seconds of this update */
	private int updTime;

	// ==========================================================================
	// Getters & Setters
	// ==========================================================================

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAction() {
		return action;
	}

	public void setAction(long action) {
		this.action = action;
	}

	public long getDecisionId() {
		return decisionId;
	}

	public void setDecisionId(long decisionId) {
		this.decisionId = decisionId;
	}

	public long getParId() {
		return parId;
	}

	public void setParId(long parId) {
		this.parId = parId;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public int getUpdTime() {
		return updTime;
	}

	public void setUpdTime(int updTime) {
		this.updTime = updTime;
	}

	// ==========================================================================
	// Object methods
	// ==========================================================================

	@Override
	public String toString() {
		return "id:" + id + " action:" + action + " decisionId:" + decisionId
				+ " parId:" + parId + " unread:" + unread + " updTime: "
				+ updTime;
	}
}
