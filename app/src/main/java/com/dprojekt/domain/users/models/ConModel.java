package com.dprojekt.domain.users.models;

import com.dprojekt.domain.images.ImgModel;

/**
 * Data model for an user as contact
 */
public class ConModel {

	// ==========================================================================
	// Constants
	// ==========================================================================

	// Contact roles
	public static final int IS_ME_ROLE = 0;
	public static final int IS_CONTACT_ROLE = 1;
	public static final int NOT_CONTACT_ROLE = 2;
	public static final int I_BLOCKED_ROLE = 3;


	// ==========================================================================
	// Member variables
	// ==========================================================================

	/** ID of the user */
	private long id;

	/** Name of the user */
	private String name;

	/** Last name of the user */
	private String lastName;

	/** Image of the user */
	private ImgModel img;

	/** ISO 3 Country Code of the country of the user */
	private String countryIso3;

	/** City of the user */
	private String city;

	/** Contact role of the user */
	private int role;

	// ==========================================================================
	// Getters & setters
	// ==========================================================================

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public ImgModel getImage() {
		return img;
	}

	public void setImage(ImgModel img) {
		this.img = img;
	}

	public String getCountryIso3() {
		return countryIso3;
	}

	public void setCountryIso3(String countryIso3) {
		this.countryIso3 = countryIso3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	// ==========================================================================
	// Constructors
	// ==========================================================================

	public ConModel() {}

	public ConModel(long id) {
		this.id = id;
	}

	// ==========================================================================
	// Utils
	// ==========================================================================

    /** @retrun name + last name */
	public String getFullName() {
		return name + " " + lastName;
	}

	/** Get the localized name of the country of this contact */
	public String getCountryName() {
		return Utils.getCountryName(countryIso3);
	}
}
