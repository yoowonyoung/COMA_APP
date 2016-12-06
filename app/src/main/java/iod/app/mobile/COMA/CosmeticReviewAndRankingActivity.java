package iod.app.mobile.COMA;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CosmeticReviewAndRankingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private BackPressCloseHandler backPressCloseHandler;
    private CosmeticRankingAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView rv;
    private DrawerLayout drawer;
    private MySqliteOpenHelper db;
    private ServerManager server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        backPressCloseHandler = new BackPressCloseHandler(this);
        setContentView(R.layout.activity_cosmetic_review_and_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        server = ServerManager.getInstance();
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv = (RecyclerView)findViewById(R.id.recycler_ranking);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);
        //db.insertSampleRanking();
        String data = db.getCosmeticRanking();
        String values[] = data.split("\n");
        ArrayList<HashMap<String,String>> testList = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject obj = new JSONObject(server.get_rank_cosmetic_info());
            JSONArray jsonArray = (JSONArray) obj.get("data");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject temp = jsonArray.getJSONObject(i);
                HashMap<String,String> posts = new HashMap<String,String>();
                posts.put("name",temp.getString("cosmetic_brand_name") + "," + temp.getString("cosmetic_name"));
                posts.put("data",temp.getDouble("cosmetic_rank") + "," + temp.getInt("cosmetic_volume") + "," + temp.getInt("cosmetic_review_count") + "," + temp.getInt("cosmetic_id"));
                testList.add(posts);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        for(int i = 0; i < values.length; i++) {
            HashMap<String,String> posts = new HashMap<String,String>();
            String temp[] = values[i].split(":");
            posts.put("name",temp[0]);
            posts.put("data",temp[1]);
            testList.add(posts);
        }*/
        adapter = new CosmeticRankingAdapter(getApplicationContext(),testList);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Intent intent = new Intent(CosmeticReviewAndRankingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my) {
            Intent intent = new Intent(CosmeticReviewAndRankingActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(CosmeticReviewAndRankingActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
