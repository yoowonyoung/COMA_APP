package iod.app.mobile.COMA;

/**
 * Created by dnjsd on 2016-12-06.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import iod.app.mobile.COMA.DBStaticValue.*;

public class ServerManager {
    private static ServerManager serverManager;
    private String path;
    private String url = "http://kossi.iptime.org:2000/cosmeticserver/";
    ArrayList<CosmeticDatas> cosmeticDatases = new ArrayList<CosmeticDatas>();


    public static synchronized ServerManager getInstance() {
        if (serverManager == null) {
            serverManager = new ServerManager();
        }
        return serverManager;
    }

    public String get_cosmetic_data(){
        get_cosmetic_Async async = new get_cosmetic_Async();
        try {
            return async.execute(url+"getall_cosmetic_info").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "null";
    }

    public void store_review(ReviewDatas review){
        JSONMaker.getInstance().makeJson_store_review(review.getCosmetic_brand_name(), review.getCosmetic_name(), review.getReview_name(), review.getReview_content(), review.getCosmetic_duration(), review.getCosmetic_Rank(), review.getComestic_id());
        store_review_async async = new store_review_async();
        async.execute(url+"store_review");
    }

    public String getAllCosmetic_withReview(){
        get_all_cosmetic_with_review async = new get_all_cosmetic_with_review();
        try {
            return async.execute(url+"getall_cosmetic_review").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String get_rank_cosmetic_info(){
        get_rank_cosmetic_Async async = new get_rank_cosmetic_Async();
        try {
            return async.execute(url+"get_rank_cosmetic_info").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }


    private class get_cosmetic_Async extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...url) {
            // URL 연결이 구현될 부분
            URL url1;
            String response = "";
            Log.w("화장품 데이터 가져오기","화장품 데이터");
            ArrayList<CosmeticDatas> temp = new ArrayList<CosmeticDatas>();
            try {
                System.out.println("보냄?");
                URL object = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Accept", "*/*");
                con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                con.setRequestMethod("POST");

            /*
            JSONObject obj = JsonMaker.getInstance().makeJson();
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            System.out.println(obj.toString());
            if(obj!=null) wr.write("data=" + obj.toString());
            wr.flush();
            wr.close();
            */

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    System.out.println("http 요청 됬을때");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    //System.out.println("" + sb.toString());
                    response = sb.toString();
                } else {
                    System.out.println(con.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

            try {
                JSONObject response_json = new JSONObject(response);
                JSONArray jarr = response_json.getJSONArray("data");

                for(int i=0;i<jarr.length();i++){
                    JSONObject cosmetic_jobj = jarr.getJSONObject(i);
                    String brand_name = cosmetic_jobj.getString(COSMETIC.brand_name);
                    String cosmetic_name = cosmetic_jobj.getString(COSMETIC.name);
                    int cosmetic_id = cosmetic_jobj.getInt(COSMETIC.id);

                    temp.add(new CosmeticDatas(cosmetic_name,brand_name,cosmetic_id));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // UI 업데이트가 구현될 부분
            System.out.println("result값 : "+result);
            try {
                JSONObject response_json = new JSONObject(result);
                JSONArray jarr = response_json.getJSONArray("data");

                for(int i=0;i<jarr.length();i++){
                    JSONObject cosmetic_jobj = jarr.getJSONObject(i);
                    String brand_name = cosmetic_jobj.getString(COSMETIC.brand_name);
                    String cosmetic_name = cosmetic_jobj.getString(COSMETIC.name);
                    int cosmetic_id = cosmetic_jobj.getInt(COSMETIC.id);

                    cosmeticDatases.add(new CosmeticDatas(cosmetic_name,brand_name,cosmetic_id));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*
                 cosmetcDatases 이용하면 됨.
             */
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("d", "WaitAndDrawAsyncTask on Cancelled");

        }


    }

    private class store_review_async extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...url) {
            // URL 연결이 구현될 부분
            URL url1;
            String response = "";
            Log.w("화장품 데이터 가져오기","화장품 데이터");

            try {
                System.out.println("보냄?");
                URL object = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Accept", "*/*");
                con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                con.setRequestMethod("POST");


                JSONObject obj = JSONMaker.getInstance().getJsonObject();

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                System.out.println(obj.toString());
                if(obj!=null) wr.write("data=" + obj.toString());

                wr.flush();
                wr.close();


                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    System.out.println("http 요청 됬을때");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    //System.out.println("" + sb.toString());
                    response = sb.toString();
                } else {
                    System.out.println(con.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }



            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // UI 업데이트가 구현될 부분
            System.out.println("result값 : "+result);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("d", "WaitAndDrawAsyncTask on Cancelled");

        }


    }

    private class get_all_cosmetic_with_review extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...url) {
            // URL 연결이 구현될 부분
            URL url1;
            String response = "";
            Log.w("화장품 데이터 가져오기","화장품 데이터");

            try {
                System.out.println("보냄?");
                URL object = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Accept", "*/*");
                con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                con.setRequestMethod("POST");

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    System.out.println("http 요청 됬을때");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    response = sb.toString();
                } else {
                    System.out.println(con.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }



            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // UI 업데이트가 구현될 부분
            System.out.println("result값 : "+result);
            ArrayList<CosmeticDatas> cosmeticDatases = new ArrayList<>();
            ArrayList<ReviewDatas[]> reviews = new ArrayList<>();
            try {
                JSONObject response_json = new JSONObject(result);
                JSONArray jarr = response_json.getJSONArray("data");

                for(int i=0;i<jarr.length();i++){
                    JSONObject cosmetic_jobj = jarr.getJSONObject(i);

                    String brand_name = cosmetic_jobj.getString(COSMETIC.brand_name);
                    String cosmetic_name = cosmetic_jobj.getString(COSMETIC.name);
                    int cosmetic_id = cosmetic_jobj.getInt(COSMETIC.id);
                    String c_ingredient = cosmetic_jobj.getString(COSMETIC.ingredient);
                    String c_type = cosmetic_jobj.getString(COSMETIC.type);
                    int c_volume = cosmetic_jobj.getInt(COSMETIC.volume);
                    String image = cosmetic_jobj.getString(COSMETIC.imgae);
                    double rank = cosmetic_jobj.getDouble(COSMETIC.rank);



                    String reviews_string = cosmetic_jobj.getString("r_data");
                    System.out.println(reviews_string);
                    ReviewDatas[] c_reviews = new ReviewDatas[3];
                    if(!reviews_string.equals("{}")){
                        JSONObject review_json = new JSONObject(reviews_string);
                        JSONArray review_arr =  review_json.getJSONArray("c_reviews");
                        for(int j=0;j<review_arr.length();j++){
                            JSONObject review_obj = review_arr.getJSONObject(j);
                            String review_b_name = review_obj.getString(REVIEW.brand_name);
                            String review_c_name = review_obj.getString(REVIEW.name);
                            String reviewer_id = review_obj.getString(REVIEW.reviewer_name);
                            String content = review_obj.getString(REVIEW.content);
                            int duration = review_obj.getInt(REVIEW.duration);
                            double review_rank = review_obj.getDouble(REVIEW.rank);
                            int review_cos_id = review_obj.getInt(REVIEW.id);

                            c_reviews[j] = new ReviewDatas(review_b_name,review_c_name,reviewer_id,content,duration,review_rank,review_cos_id);

                        }

                    }
                    reviews.add(c_reviews);
                    cosmeticDatases.add(new CosmeticDatas(cosmetic_name,brand_name,cosmetic_id));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* 테스트용
            for(int i=0;i<cosmeticDatases.size();i++){
                System.out.println(cosmeticDatases.get(i).getCosmeticName());
            }
            for(int i=0;i<reviews.size();i++){
                Review[] re = reviews.get(i);
                for(int j=0;j<re.length;j++){
                  System.out.println(re[i].cosmetic_name);
                }
            }*/



            /*
                 cosmetcDatases 이용하면 됨.
                 reviews 이용하면 됨.
                 cosmetcDatases[0]의 리뷰는 reviews[0]에 있는 리뷰배열임.
                 만약 댓글이 없는 경우 reivews 배열은 텅텅 비어있음 ㅇㅇ
             */
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("d", "WaitAndDrawAsyncTask on Cancelled");

        }


    }

    private class get_rank_cosmetic_Async extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...url) {
            // URL 연결이 구현될 부분
            URL url1;
            String response = "";
            Log.w("화장품 데이터 가져오기","화장품 데이터");

            try {
                System.out.println("보냄?");
                URL object = new URL(url[0]);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Accept", "*/*");
                con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                con.setRequestMethod("POST");

            /*
            JSONObject obj = JsonMaker.getInstance().makeJson();
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            System.out.println(obj.toString());
            if(obj!=null) wr.write("data=" + obj.toString());
            wr.flush();
            wr.close();
            */

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    System.out.println("http 요청 됬을때");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    //System.out.println("" + sb.toString());
                    response = sb.toString();
                } else {
                    System.out.println(con.getResponseMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }



            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            // UI 업데이트가 구현될 부분
            System.out.println("result값 : "+result);
            ArrayList<CosmeticDatas> cosmeticDatases = new ArrayList<>();
            try {
                JSONObject response_json = new JSONObject(result);
                JSONArray jarr = response_json.getJSONArray("data");

                for(int i=0;i<jarr.length();i++){
                    JSONObject cosmetic_jobj = jarr.getJSONObject(i);
                    String cosmetic_name = cosmetic_jobj.getString(RANKING.name);
                    String cosmetic_brand_name = cosmetic_jobj.getString(RANKING.brand_name);
                    int cosmetc_id = cosmetic_jobj.getInt(RANKING.id);
                    int count = cosmetic_jobj.getInt(RANKING.review_count);
                    double star = cosmetic_jobj.getDouble(RANKING.rank);
                    int volume = cosmetic_jobj.getInt(RANKING.volume);

                    /*
                        해당 클래스가 없어서 알아서 데이터를 사용하길
                    */

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("d", "WaitAndDrawAsyncTask on Cancelled");

        }

    }
}
