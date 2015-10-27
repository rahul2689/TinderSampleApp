package com.example.root.tindersampleapp.modules.tinderHome.model.data;


import com.example.root.tindersampleapp.application.model.data.AppData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductsData extends AppData implements Serializable {

    @SerializedName("post_title")
    private String mProductTitle;

    @SerializedName("post_content")
    private String mProductContent;

    @SerializedName("medium_image")
    private String mMedImgUrl;

    @SerializedName("full_image")
    private String mFullImgUrl;

    @SerializedName("large_image")
    private String mLargeImgUrl;

    @SerializedName("thumbnail_image")
    private String mThumbnailImg;

    public ProductsData() {
        this.mProductTitle = "";
        this.mProductContent = "";
        this.mMedImgUrl = "";
        this.mLargeImgUrl = "";
        this.mFullImgUrl = "";
    }

    public String getMedImgUrl() {
        return mMedImgUrl;
    }

    public void setMedImgUrl(String medImgUrl) {
        this.mMedImgUrl = medImgUrl;
    }

    public String getProductContent() {
        return mProductContent;
    }

    public void setProductContent(String productContent) {
        this.mProductContent = productContent;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public void setProductTitle(String productTitle) {
        this.mProductTitle = productTitle;
    }

    public String getFullImgUrl() {
        return mFullImgUrl;
    }

    public void setmFullImgUrl(String fullImgUrl) {
        this.mFullImgUrl = fullImgUrl;
    }

    public String getLargeImgUrl() {
        return mLargeImgUrl;
    }

    public void setmLargeImgUrl(String largeImgUrl) {
        this.mLargeImgUrl = largeImgUrl;
    }

    public String getThumbnailImg() {
        return mThumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.mThumbnailImg = thumbnailImg;
    }
}
