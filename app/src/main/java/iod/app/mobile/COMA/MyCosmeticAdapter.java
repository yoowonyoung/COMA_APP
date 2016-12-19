package iod.app.mobile.COMA;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dnjsd on 2016-11-22.
 */

public class MyCosmeticAdapter extends RecyclerView.Adapter<MyCosmeticAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> nameAndType;
    boolean visibleflag = false;
    boolean deleteflag = false;
    private MySqliteOpenHelper db;
    private Intent userData;
    private ServerManager server;

    public MyCosmeticAdapter(Context context, ArrayList<HashMap<String,String>> nameAndType, Intent userData) {
        this.context = context;
        this.nameAndType = nameAndType;
        db = MySqliteOpenHelper.getInstance(context);
        this.userData = userData;
        server = ServerManager.getInstance();
    }
    public void updateDataa() {
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cosmetic_item,null);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String,String> cosmeticItem = nameAndType.get(position);
        holder.cosmetic_name.setText(cosmeticItem.get("name"));
        String temp[] = cosmeticItem.get("type").split(":");
        holder.cosmetic_type.setText(temp[0]);
        holder.cosmetic_id = Integer.valueOf(temp[1]);
        try {
            JSONObject obj = new JSONObject(server.get_cosmetic_data());
            JSONArray jsonArray = (JSONArray) obj.get("data");
            for(int i = 0; i < jsonArray.length(); i++) {
                final JSONObject temp2 = jsonArray.getJSONObject(i);
                if(temp2.getInt("cosmetic_id") == holder.cosmetic_id) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                URL url = new URL(temp2.getString("cosmetic_image"));
                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);
                                holder.cosmetic_image.setImageBitmap(bm);
                            } catch(Exception e){
                            }
                        }
                    });
                    t.start();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(visibleflag) {
            holder.cosmetic_check.setVisibility(View.VISIBLE);
        }else {
            holder.cosmetic_check.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return this.nameAndType.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        TextView cosmetic_name;
        TextView cosmetic_type;
        CheckBox cosmetic_check;
        ImageView cosmetic_image;
        int cosmetic_id;
        CardView cv;

        public ViewHolder(final View v) {
            super(v);
            cosmetic_name = (TextView)v.findViewById(R.id.cosmetic_name);
            cosmetic_type = (TextView)v.findViewById(R.id.cosmetic_type);
            cosmetic_check = (CheckBox)v.findViewById(R.id.cosmetic_checkbox);
            cosmetic_image = (ImageView)v.findViewById(R.id.cosmetic_image);
            cosmetic_check.setVisibility(View.INVISIBLE);
            cv = (CardView) v.findViewById(R.id.cosmetic_cardview);
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
            cosmetic_check.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            removeAt(getAdapterPosition(),this);
        }
        public void removeAt(int position,ViewHolder viewHolder) {
            nameAndType.remove(position);
            notifyItemRemoved(position);
            db.deleteMyCosmetic(cosmetic_id);
            Toast.makeText(context,"삭제되었습니다",Toast.LENGTH_SHORT).show();
        }
    }
}
