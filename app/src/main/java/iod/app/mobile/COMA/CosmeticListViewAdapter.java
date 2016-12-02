package iod.app.mobile.COMA;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by dnjsd on 2016-12-02.
 */

public class CosmeticListViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<CosmeticDatas> cosmeticDatasList = null;
    private ArrayList<CosmeticDatas> arraylist;

    public CosmeticListViewAdapter(Context context, List<CosmeticDatas> cosmeticDatasList) {
        this.context = context;
        this.cosmeticDatasList = cosmeticDatasList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<CosmeticDatas>();
        this.arraylist.addAll(cosmeticDatasList);
    }

    public class ViewHolder {
        TextView name;
        TextView brand;
        int cosmeticID;

    }

    @Override
    public int getCount() {
        return cosmeticDatasList.size();
    }

    @Override
    public CosmeticDatas getItem(int position) {
        return cosmeticDatasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.cosmetic_name_list);
            holder.brand = (TextView) view.findViewById(R.id.cosmetic_brand_list);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(cosmeticDatasList.get(position).getCosmeticName());
        holder.brand.setText(cosmeticDatasList.get(position).getCosmeticBrand() + " : ");
        holder.cosmeticID = cosmeticDatasList.get(position).getCosmeticID();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailReviewActivity.class);
                intent.putExtra("cosmetic_id",""+holder.cosmeticID);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        cosmeticDatasList.clear();
        if (charText.length() == 0) {
            //cosmeticDatasList.addAll(arraylist);
        } else {
            for (CosmeticDatas wp : arraylist) {
                if (wp.getCosmeticName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    cosmeticDatasList.add(wp);
                }else if(wp.getCosmeticBrand().toLowerCase(Locale.getDefault()).contains(charText)) {
                    cosmeticDatasList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
