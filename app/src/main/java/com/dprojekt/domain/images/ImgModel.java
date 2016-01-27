package com.dprojekt.domain.images;

/**
 * Domain layer model for an image.
 */
public class ImgModel {

    // ==========================================================================
    // Member variables
    // ==========================================================================

    /** URI or URL of this image */
    private String imgUri;

    /** Timestamp in seconds of the last time this image was updated */
    private int updTime;

    // ==========================================================================
    // Getters & Setters
    // ==========================================================================

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public int getUpdTime() {
        return updTime;
    }

    public void setUpdTime(int updTime) {
        this.updTime = updTime;
    }
}
