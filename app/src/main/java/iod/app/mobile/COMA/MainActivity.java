package iod.app.mobile.COMA;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener  {
    private DrawerLayout drawer;
    private BackPressCloseHandler backPressCloseHandler;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;
    private LinearLayoutManager mLinearLayoutManager;
    private MySqliteOpenHelper db;
    private MyCosmeticAdapter adapter;
    private RecyclerView rv;
    private ActionBarDrawerToggle toggle;
    private SearchView mSearchView;
    private String[] cosmeticNameList;
    private ArrayList<CosmeticDatas> arraylist = new ArrayList<CosmeticDatas>();
    private ListView cosmeticNameListView;
    private CosmeticListViewAdapter cosmeticSearchListadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        backPressCloseHandler = new BackPressCloseHandler(this);
        toolbar.setTitle("");
        mSearchView = (SearchView)findViewById(R.id.main_search);
        mSearchView.setLayoutParams(new Toolbar.LayoutParams(Gravity.RIGHT));
        mSearchView.setQueryHint("화장품 이름을 입력해 주세요");
        //db 초기화
        //db.myclear();
        cosmeticNameList = db.getCosmeticNameAndBrand().split("\n");
        cosmeticNameListView = (ListView) findViewById(R.id.cosmetic_listview);
        for (int i = 0; i < cosmeticNameList.length; i++) {
            String temp[] = cosmeticNameList[i].split("/");
            CosmeticDatas cosmeticDatas = new CosmeticDatas(temp[0],temp[1],Integer.valueOf(temp[2]));
            // Binds all strings into an array
            arraylist.add(cosmeticDatas);
        }
        // Pass results to CosmeticListViewAdapter Class
        cosmeticSearchListadapter = new CosmeticListViewAdapter(this, arraylist);
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
        setSupportActionBar(toolbar);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv = (RecyclerView)findViewById(R.id.recycler_main);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
        String data = db.getMyCosmetic();
        String values[] = data.split("\n");
        final ArrayList<HashMap<String,String>> testList = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < values.length; i++) {
            HashMap<String,String> posts = new HashMap<String,String>();
            String temp[] = values[i].split(",");
            posts.put("name",temp[0]);
            posts.put("type",temp[1]);
            testList.add(posts);
        }

        adapter = new MyCosmeticAdapter(getApplicationContext(),testList);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabAdd = (FloatingActionButton)findViewById(R.id.floating_add);
        fabDelete = (FloatingActionButton)findViewById(R.id.floating_delete);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCosmeticActivity.class);
                startActivity(intent);
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.flag) {
                    adapter.flag = false;
                }else {
                    adapter.flag = true;
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed() {
        rv.bringToFront();
        cosmeticNameListView.setVisibility(View.INVISIBLE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    protected void onResume() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        adapter.updateDataa();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my) {

        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(MainActivity.this, CosmeticReviewAndRankingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
