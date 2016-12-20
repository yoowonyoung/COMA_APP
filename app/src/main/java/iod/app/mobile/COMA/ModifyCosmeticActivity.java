package iod.app.mobile.COMA;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModifyCosmeticActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private DrawerLayout drawer;
    private static EditText cosmeticOpenDay;
    private static EditText cosmeticExpriyDay;
    private BootstrapButton addCosmetic;
    private BootstrapButton week1Btn;
    private BootstrapButton week2Btn;
    private BootstrapButton month1Btn;
    private BootstrapButton month2Btn;
    private BootstrapButton month3Btn;
    private BootstrapButton openBtn;
    private ImageView image;
    private Calendar calendar = Calendar.getInstance();
    private SearchView mSearchView;
    private String[] cosmeticNameList;
    private ArrayList<CosmeticDatas> arraylist = new ArrayList<CosmeticDatas>();
    private ListView cosmeticNameListView;
    private AddCosmeticListAdapter cosmeticSearchListadapter;
    private MySqliteOpenHelper db;
    private AlarmManager mManager;
    private CosmeticDatas AddItem;
    private ServerManager server;
    private Spinner cosmetic_tpye_spinner;
    private TextView cosmeticName;
    private TextView cosmeticVolume;
    private TextView nickname;
    private View header;
    private Intent userData;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_cosmetic);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        nickname = (TextView)header.findViewById(R.id.nav_nickname);
        userData = getIntent();
        nickname.setText(userData.getStringExtra("userNickname"));
        final Handler handler = new Handler();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    final CircularImageView iv = (CircularImageView)header.findViewById(R.id.nav_thumbnail);
                    URL url = new URL(userData.getStringExtra("userThumbnailImage"));
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setImageBitmap(bm);
                        }
                    });
                    iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch(Exception e){
                }
            }
        });
        t.start();
        image = (ImageView)findViewById(R.id.cosmetic_image_add);
        week1Btn = (BootstrapButton) findViewById(R.id.alarm_1week_btn);
        week2Btn = (BootstrapButton)findViewById(R.id.alarm_2week_btn);
        month1Btn = (BootstrapButton)findViewById(R.id.alarm_1month_btn);
        month2Btn = (BootstrapButton)findViewById(R.id.alarm_2month_btn);
        month3Btn = (BootstrapButton)findViewById(R.id.alarm_3month_btn);
        openBtn = (BootstrapButton)findViewById(R.id.cosmetic_open);
        openBtn.setBackgroundColor(00000000);
        openBtn.setTextColor(Color.BLACK);
        server = ServerManager.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        cosmeticOpenDay = (EditText)findViewById(R.id.cosmetic_open_day);
        cosmeticExpriyDay = (EditText)findViewById(R.id.cosmetic_expriy_date);
        cosmeticExpriyDay.setFocusable(false);
        cosmeticOpenDay.setFocusable(false);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        cosmeticName = (TextView)findViewById(R.id.cosmetic_name_add);
        cosmeticVolume = (TextView)findViewById(R.id.cosmetic_volume_add);

        mSearchView = (SearchView)findViewById(R.id.add_search);
        mSearchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        mSearchView.setQueryHint("등록할 화장품 이름 검색");
        cosmeticNameList = db.getCosmeticNameAndVoluemAndType().split("\n");
        cosmeticNameListView = (ListView) findViewById(R.id.cosmetic_listview);
        try {
            JSONObject obj = new JSONObject(server.get_cosmetic_data());
            JSONArray jsonArray = (JSONArray) obj.get("data");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                CosmeticDatas cosmeticDatas = new CosmeticDatas(temp.getString("cosmetic_name"),temp.getString("cosmetic_brand_name"),temp.getInt("cosmetic_id"),temp.getInt("cosmetic_volume"),temp.getString("cosmetic_type"));
                cosmeticDatas.setCosmeticImageSrc(temp.getString("cosmetic_image"));
                arraylist.add(cosmeticDatas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Pass results to CosmeticListViewAdapter Class
        cosmeticSearchListadapter = new AddCosmeticListAdapter(this,arraylist);
        cosmetic_tpye_spinner = (Spinner) findViewById(R.id.cosmetic_type_spinner);
        ArrayAdapter<CharSequence> coemecit_type_adapter = ArrayAdapter.createFromResource(this, R.array.cosmetic_type, android.R.layout.simple_spinner_item);
        coemecit_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cosmetic_tpye_spinner.setAdapter(coemecit_type_adapter);
        int id = Integer.valueOf(userData.getStringExtra("cosmetic_id"));
        for(int i = 0; i < cosmeticSearchListadapter.getCount(); i++) {
            final CosmeticDatas data = cosmeticSearchListadapter.getItem(i);
            if(data.getCosmeticID() == id) {
                AddItem = data;
                cosmeticName.setText(data.getCosmeticBrand() + " " + data.getCosmeticName());
                cosmeticVolume.setText(data.getCosmeticVolume() +" ml");
                cosmetic_tpye_spinner.setSelection(data.getCosmeticTypeIndex());
                Thread tempThread = new Thread(new Runnable() {
                    @Override
                    public void run() {    // 오래 거릴 작업을 구현한다
                        // TODO Auto-generated method stub
                        try{
                            URL url = new URL(data.getCosmeticImageSrc());
                            InputStream is = url.openStream();
                            final Bitmap bm = BitmapFactory.decodeStream(is);
                            image.setImageBitmap(bm);
                        } catch(Exception e){
                        }
                    }
                });
                tempThread.start();
            }

        }
        // Binds the Adapter to the ListView
        cosmeticNameListView.setAdapter(cosmeticSearchListadapter);
        cosmeticNameListView.setBackgroundColor(Color.WHITE);
        cosmeticNameListView.setVisibility(View.INVISIBLE);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cosmeticNameListView.setVisibility(View.INVISIBLE);
                return false;
            }
        });


        addCosmetic = (BootstrapButton)findViewById(R.id.cosmetic_add);
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Spinner spinner = (Spinner) findViewById(R.id.cosmetic_expiry_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expriy_date_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) {
                    cosmeticExpriyDay.setFocusable(true);
                    cosmeticExpriyDay.setFocusableInTouchMode(true);
                }else if(i == 2){
                    if(cosmeticOpenDay.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(),"개봉일이 입력되지 않았습니다!",Toast.LENGTH_SHORT).show();
                        cosmeticOpenDay.requestFocus();
                    }else {
                        String date = cosmeticOpenDay.getText().toString();
                        String[] temp = date.split("/");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
                        calendar.add(Calendar.MONTH,6);
                        cosmeticExpriyDay.setText(calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DATE));
                    }

                }else if(i == 3){
                    if(cosmeticOpenDay.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(),"개봉일이 입력되지 않았습니다!",Toast.LENGTH_SHORT).show();
                        cosmeticOpenDay.requestFocus();
                    }else {
                        String date = cosmeticOpenDay.getText().toString();
                        String[] temp = date.split("/");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
                        calendar.add(Calendar.MONTH,12);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        if(month == 0) {
                            year--;
                            month = 12;
                        }
                        cosmeticExpriyDay.setText(year + "/" + month + "/" + calendar.get(Calendar.DATE));
                    }

                }else if(i == 4){
                    if(cosmeticOpenDay.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(),"개봉일이 입력되지 않았습니다!",Toast.LENGTH_SHORT).show();
                        cosmeticOpenDay.requestFocus();
                    }else {
                        String date = cosmeticOpenDay.getText().toString();
                        String[] temp = date.split("/");
                        calendar.set(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
                        calendar.add(Calendar.MONTH,24);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        if(month == 0) {
                            year--;
                            month = 12;
                        }
                        cosmeticExpriyDay.setText(year + "/" + month + "/" + calendar.get(Calendar.DATE));
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addCosmetic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int index = db.getLastItemIndexFromMyItem();
                if(cosmeticOpenDay.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "개봉일이 입력되지 않았습니다!", Toast.LENGTH_SHORT).show();
                    cosmeticOpenDay.requestFocus();
                }else if(cosmeticExpriyDay.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "유통기한이 입력되지 않았습니다!", Toast.LENGTH_SHORT).show();
                    cosmeticExpriyDay.requestFocus();
                }else {
                    if(!db.checkMyCosmetic(AddItem.getCosmeticID())){
                        //db.insertCosmetic(AddItem.getCosmeticBrand(),AddItem.getCosmeticName(),cosmetic_tpye_spinner.getSelectedItem().toString(),AddItem.getCosmeticImageSrc(),AddItem.getCosmeticID());
                        Intent intent = new Intent(ModifyCosmeticActivity.this, MainActivity.class);
                        intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                        intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                        intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                        startActivity(intent);

                        //알림기능 사용
                        Intent alarmIntent = new Intent(ModifyCosmeticActivity.this, MyBroadcastReciver.class);
                        //Pending Intent 이용해서 알림을 등록 하기 위함
                        PendingIntent sender = PendingIntent.getBroadcast(ModifyCosmeticActivity.this, 0, alarmIntent, 0);
                        //DB에서 알림 시각을 꺼내옴
                        String data = db.getAlarmData();
                        String time[] = null;
                        if(!data.equals(new String("NoData"))) {
                            String temp[] = data.split("/");
                            time = temp[2].split(":");
                        }
                        if(week1Btn.isSelected()) {//각 버튼 별 알림 유무 확인 - 그니까 1주일전에 알림을 줄것인지, 2주일전에 줄것인지...
                            calendar.add(Calendar.DATE,-7);//calendar에 유통기한 날짜가 저장이 되어 있는데, 거기서 7일을 뺌
                            //알림할 년,월,일,시,분,초 설정
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
                            //알림매니저를 이용해서 알림을 설정 하는 기능
                            mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                        if(week2Btn.isSelected()) {
                            calendar.add(Calendar.DATE,-14);
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
                            mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                        if(month1Btn.isSelected()) {
                            calendar.add(Calendar.MONTH,-1);
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
                            mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                        if(month2Btn.isSelected()) {
                            calendar.add(Calendar.MONTH,-2);
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
                            mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                        if(month3Btn.isSelected()) {
                            calendar.add(Calendar.MONTH,-3);
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
                            mManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                        }
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"등록되어있지 않은 화장품 입니다\n 자동으로 저장됩니다.",Toast.LENGTH_SHORT).show();
                        db.insertCosmetic(AddItem.getCosmeticBrand(),AddItem.getCosmeticName(),cosmetic_tpye_spinner.getSelectedItem().toString(),AddItem.getCosmeticImageSrc(),AddItem.getCosmeticID());
                        Intent intent = new Intent(ModifyCosmeticActivity.this, MainActivity.class);
                        intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                        intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                        intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                        startActivity(intent);
                    }
                }

            }
        });
    }
    /*
    @Override
    public void onBackPressed() {
        cosmeticNameListView.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my) {
            Intent intent = new Intent(ModifyCosmeticActivity.this, MainActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(ModifyCosmeticActivity.this, CosmeticReviewAndRankingActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(ModifyCosmeticActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Intent intent = new Intent(ModifyCosmeticActivity.this, MainActivity.class);
        startActivity(intent);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cosmeticNameListView.bringToFront();
        String text = newText;
        cosmeticSearchListadapter.filter(text);
        cosmeticNameListView.setVisibility(View.VISIBLE);
        return false;
    }

    public class AddCosmeticListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        private List<CosmeticDatas> cosmeticDatasList = null;
        private ArrayList<CosmeticDatas> arraylist;

        public AddCosmeticListAdapter(Context context, List<CosmeticDatas> cosmeticDatasList) {
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
            int cosmeticVolume;
            String cosmeticType;
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
            final AddCosmeticListAdapter.ViewHolder holder;
            if (view == null) {
                holder = new AddCosmeticListAdapter.ViewHolder();
                view = inflater.inflate(R.layout.list_view_items, null);
                // Locate the TextViews in listview_item.xml
                holder.name = (TextView) view.findViewById(R.id.cosmetic_name_list);
                holder.brand = (TextView) view.findViewById(R.id.cosmetic_brand_list);
                view.setTag(holder);
            } else {
                holder = (AddCosmeticListAdapter.ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.name.setText(cosmeticDatasList.get(position).getCosmeticName());
            holder.brand.setText(cosmeticDatasList.get(position).getCosmeticBrand() + " : ");
            holder.cosmeticID = cosmeticDatasList.get(position).getCosmeticID();
            holder.cosmeticVolume = cosmeticDatasList.get(position).getCosmeticVolume();
            holder.cosmeticType = cosmeticDatasList.get(position).getCosmeticType();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cosmeticNameListView.setVisibility(View.INVISIBLE);
                    AddItem = new CosmeticDatas(cosmeticDatasList.get(position).getCosmeticName(),
                            cosmeticDatasList.get(position).getCosmeticBrand(),
                            holder.cosmeticID, holder.cosmeticVolume, holder.cosmeticType);
                    AddItem.setCosmeticImageSrc(cosmeticDatasList.get(position).getCosmeticImageSrc());
                    cosmeticName.setText(AddItem.getCosmeticBrand() + " " + AddItem.getCosmeticName());
                    cosmeticVolume.setText(AddItem.getCosmeticVolume() +"ml");
                    cosmetic_tpye_spinner.setSelection(AddItem.getCosmeticTypeIndex());
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                URL url = new URL(cosmeticDatasList.get(position).getCosmeticImageSrc());
                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);
                                image.setImageBitmap(bm);
                            } catch(Exception e){
                            }
                        }
                    });
                    t.start();
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


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int syear;
        int smonth;
        int sday;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            syear = year;
            smonth = month;
            sday = day;
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            cosmeticOpenDay.setText(year + "/" + (month+1) + "/" + day);
        }
    }
}
