package iod.app.mobile.COMA;

/**
 * Created by dnjsd on 2016-12-06.
 */

public class ReviewDatas {
    String cosmetic_brand_name;
    String cosmetic_name;
    String review_name;
    String review_content;
    int cosmetic_duration;
    double cosmetic_Rank;
    int comestic_id;

    public String getCosmetic_brand_name() {
        return cosmetic_brand_name;
    }

    public void setCosmetic_brand_name(String cosmetic_brand_name) {
        this.cosmetic_brand_name = cosmetic_brand_name;
    }

    public String getCosmetic_name() {
        return cosmetic_name;
    }

    public void setCosmetic_name(String cosmetic_name) {
        this.cosmetic_name = cosmetic_name;
    }

    public String getReview_name() {
        return review_name;
    }

    public void setReview_name(String review_name) {
        this.review_name = review_name;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public int getCosmetic_duration() {
        return cosmetic_duration;
    }

    public void setCosmetic_duration(int cosmetic_duration) {
        this.cosmetic_duration = cosmetic_duration;
    }

    public double getCosmetic_Rank() {
        return cosmetic_Rank;
    }

    public void setCosmetic_Rank(double cosmetic_Rank) {
        this.cosmetic_Rank = cosmetic_Rank;
    }

    public int getComestic_id() {
        return comestic_id;
    }

    public void setComestic_id(int comestic_id) {
        this.comestic_id = comestic_id;
    }

    public ReviewDatas(String cosmetic_brand_name, String comestic_name, String review_name, String review_content, int cometic_duration, double cometic_Rank, int comestic_id){
        this.comestic_id = comestic_id;
        this.cosmetic_brand_name = cosmetic_brand_name;
        this.review_name = review_name;
        this.review_content = review_content;
        this.cosmetic_duration = cometic_duration;
        this.cosmetic_Rank = cosmetic_Rank;
        this.cosmetic_name = comestic_name;
    }

}
