package iod.app.mobile.COMA;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddCosmeticActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private DrawerLayout drawer;
    private static EditText cosmeticOpenDay;
    private static EditText cosmeticExpriyDay;
    private BootstrapButton addCosmetic;
    private BootstrapButton week1Btn;
    private BootstrapButton week2Btn;
    private BootstrapButton month1Btn;
    private BootstrapButton month2Btn;
    private BootstrapButton month3Btn;
    private Calendar calendar = Calendar.getInstance();
    private SearchView mSearchView;
    private String[] cosmeticNameList;
    private ArrayList<CosmeticDatas> arraylist = new ArrayList<CosmeticDatas>();
    private ListView cosmeticNameListView;
    private AddCosmeticListAdapter cosmeticSearchListadapter;
    private MySqliteOpenHelper db;
    private AlarmManager mManager;
    private CosmeticDatas AddItem;
    Spinner cosmetic_tpye_spinner;
    TextView cosmeticName;
    TextView cosmeticVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cosmetic);
        week1Btn = (BootstrapButton) findViewById(R.id.alarm_1week_btn);
        week2Btn = (BootstrapButton)findViewById(R.id.alarm_2week_btn);
        month1Btn = (BootstrapButton)findViewById(R.id.alarm_1month_btn);
        month2Btn = (BootstrapButton)findViewById(R.id.alarm_2month_btn);
        month3Btn = (BootstrapButton)findViewById(R.id.alarm_3month_btn);
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
        for (int i = 0; i < cosmeticNameList.length; i++) {
            String temp[] = cosmeticNameList[i].split("/");
            CosmeticDatas cosmeticDatas = new CosmeticDatas(temp[0],temp[1],Integer.valueOf(temp[2]),Integer.valueOf(temp[3]),temp[4]);
            // Binds all strings into an array
            arraylist.add(cosmeticDatas);
        }
        // Pass results to CosmeticListViewAdapter Class
        cosmeticSearchListadapter = new AddCosmeticListAdapter(this,arraylist);
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
        cosmetic_tpye_spinner = (Spinner) findViewById(R.id.cosmetic_type_spinner);
        ArrayAdapter<CharSequence> coemecit_type_adapter = ArrayAdapter.createFromResource(this, R.array.cosmetic_type, android.R.layout.simple_spinner_item);
        coemecit_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cosmetic_tpye_spinner.setAdapter(coemecit_type_adapter);
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
                if(db.checkMyCosmetic(AddItem.getCosmeticID())){
                    db.insertCosmetic(AddItem.getCosmeticBrand(),AddItem.getCosmeticName(),cosmetic_tpye_spinner.getSelectedItem().toString(),"asd",AddItem.getCosmeticID());
                    Intent intent = new Intent(AddCosmeticActivity.this, MainActivity.class);
                    startActivity(intent);
                    Intent alarmIntent = new Intent(AddCosmeticActivity.this, MyBroadcastReciver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(AddCosmeticActivity.this, 0, alarmIntent, 0);
                    //알람시간 calendar에 set해주기
                    String data = db.getAlarmData();
                    String time[] = null;
                    if(!data.equals(new String("NoData"))) {
                        String temp[] = data.split("/");
                        time = temp[2].split(":");
                    }
                    if(week1Btn.isSelected()) {
                        calendar.add(Calendar.DATE,-7);
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(time[0]), Integer.valueOf(time[1]), 0);
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
                    Toast.makeText(getApplicationContext(),"이미 등록되어있는 화장품 입니다",Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(AddCosmeticActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(AddCosmeticActivity.this, CosmeticReviewAndRankingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(AddCosmeticActivity.this, SettingsActivity.class);
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
        Intent intent = new Intent(AddCosmeticActivity.this, MainActivity.class);
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
                    cosmeticName.setText(AddItem.getCosmeticBrand() + " " + AddItem.getCosmeticName());
                    cosmeticVolume.setText(AddItem.getCosmeticVolume() +"ml");
                    cosmetic_tpye_spinner.setSelection(AddItem.getCosmeticTypeIndex());
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
