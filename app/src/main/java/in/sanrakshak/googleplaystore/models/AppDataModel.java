package in.sanrakshak.googleplaystore.models;

public class AppDataModel {

    private String appName,appCategory,appSize,appRating,appReviews,appInstalls,appType,appPrice;
    private String appContentRating,appGenres,appLastUpdated,appCurrentVer,appAndroidVer;
    public AppDataModel(String appName, String appCategory, String appSize, String appRating, String appReviews,
                        String appInstalls, String appType, String appPrice, String appContentRating,String appGenres,
                        String appLastUpdated,String appCurrentVer,String appAndroidVer) {
        this.appName = appName;
        this.appCategory = appCategory;
        this.appSize = appSize;
        this.appRating = appRating;
        this.appReviews = appReviews;
        this.appInstalls = appInstalls;
        this.appType = appType;
        this.appPrice = appPrice;
        this.appContentRating = appContentRating;
        this.appGenres = appGenres;
        this.appLastUpdated = appLastUpdated;
        this.appCurrentVer = appCurrentVer;
        this.appAndroidVer = appAndroidVer;
    }

    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppCategory() {
        return appCategory;
    }
    public void setAppCategory(String appCategory) {
        this.appCategory = appCategory;
    }

    public String getAppSize() {
        return appSize;
    }
    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppRating() {
        return appRating;
    }
    public void setAppRating(String appRating) {
        this.appRating = appRating;
    }

    public String getAppReviews() {
        return appReviews;
    }
    public void setAppReviews(String appReviews) {
        this.appReviews = appReviews;
    }

    public String getAppInstalls() {
        return appInstalls;
    }
    public void setAppInstalls(String appInstalls) {
        this.appInstalls = appInstalls;
    }

    public String getAppType() {
        return appType;
    }
    public void setAppType(String appType) { this.appType = appType; }
}
