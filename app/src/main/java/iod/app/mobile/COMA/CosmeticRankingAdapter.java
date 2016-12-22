package iod.app.mobile.COMA;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dnjsd on 2016-11-22.
 */

public class CosmeticRankingAdapter extends RecyclerView.Adapter<CosmeticRankingAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> nameAndType;
    Intent userData;
    ServerManager server;

    public CosmeticRankingAdapter(Context context, ArrayList<HashMap<String,String>> nameAndType,Intent userData) {
        this.context = context;
        this.nameAndType = nameAndType;
        this.userData = userData;
        server = ServerManager.getInstance();
    }
    public void updateDataa() {
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cosmetic_ranking_item,null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String,String> cosmeticItem = nameAndType.get(position);
        String name = cosmeticItem.get("name");
        String nameData[] = name.split(",");
        holder.cosmetic_name_rank.setText(nameData[1]);
        holder.cosmetic_brand_rank.setText(nameData[0]);
        String data = cosmeticItem.get("data");
        String tempData[] = data.split(",");
        holder.cosmetic_rating_rank.setRating(Float.valueOf(tempData[0]));
        holder.cosmetic_review_count.setText("("+ tempData[2] + ")");
        holder.cosmetic_volume_rank.setText(tempData[1]+"ml");
        holder.cosmetic_id = Integer.valueOf(tempData[3]);

        try {
            JSONObject obj = new JSONObject(server.getAllCosmetic_withReview());
            JSONArray jsonArray = (JSONArray) obj.get("data");
            for(int i = 0; i < jsonArray.length(); i++) {
                final JSONObject temp = jsonArray.getJSONObject(i);
                if(temp.getInt("cosmetic_id") == holder.cosmetic_id) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                URL url = new URL(temp.getString("cosmetic_image"));
                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);
                                holder.cosmetic_item_image.setImageBitmap(bm);
                            } catch(Exception e){
                            }
                        }
                    });
                    thread.start();
                }else {
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return this.nameAndType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cosmetic_name_rank;
        TextView cosmetic_brand_rank;
        TextView cosmetic_volume_rank;
        TextView cosmetic_review_count;
        RatingBar cosmetic_rating_rank;
        ImageView cosmetic_item_image;
        CardView cv;
        int cosmetic_id;

        public ViewHolder(final View v) {
            super(v);
            cosmetic_name_rank = (TextView)v.findViewById(R.id.cosmetic_name_rank);
            cosmetic_brand_rank = (TextView)v.findViewById(R.id.cosmetic_brand_rank);
            cosmetic_volume_rank = (TextView)v.findViewById(R.id.cosmetic_volume_rank);
            cosmetic_review_count = (TextView)v.findViewById(R.id.cosmetic_review_count);
            cosmetic_rating_rank = (RatingBar)v.findViewById(R.id.cosmetic_rating_rank);
            cosmetic_item_image = (ImageView)v.findViewById(R.id.cosmetic_item_image);
            cv = (CardView) v.findViewById(R.id.cosmetic_cardview_rank);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailReviewActivity.class);
                    intent.putExtra("cosmetic_id",""+cosmetic_id);
                    intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                    intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                    intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                    v.getContext().startActivity(intent);
                }
            });
        }

    }
}
