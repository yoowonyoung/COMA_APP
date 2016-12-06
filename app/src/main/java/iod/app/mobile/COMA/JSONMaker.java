package iod.app.mobile.COMA;

/**
 * Created by dnjsd on 2016-12-06.
 */
import org.json.JSONException;
import org.json.JSONObject;
import iod.app.mobile.COMA.DBStaticValue.REVIEW;


public class JSONMaker {
    private static JSONMaker jsonmaker = null;
    private JSONObject obj;

    public void makeJson_store_review(String cosmetic_brand,String cosmetic_name, String reviewer_id, String review_content,
                                      int duration, double  rank,int  cosmetic_id){
        obj = new JSONObject();
        try {
            obj.put(REVIEW.brand_name,cosmetic_brand);
            obj.put(REVIEW.name,cosmetic_name);
            obj.put(REVIEW.reviewer_name,reviewer_id);
            obj.put(REVIEW.content,review_content);
            obj.put(REVIEW.duration,duration);
            obj.put(REVIEW.rank,rank);
            obj.put(REVIEW.id,cosmetic_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getJsonObject(){
        return obj;
    }

    public static synchronized JSONMaker getInstance(){
        if(jsonmaker==null){
            jsonmaker = new JSONMaker();
        }
        return jsonmaker;
    }

}
