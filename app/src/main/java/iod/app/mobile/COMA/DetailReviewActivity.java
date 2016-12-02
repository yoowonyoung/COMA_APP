package iod.app.mobile.COMA;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Intent intent;
    private MySqliteOpenHelper db;
    private TextView cosmeticBrand;
    private TextView cosmeticName;
    private TextView cosmeticIngredient;
    private RatingBar cosmeticRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        //다른데서 넘어올때 intent에 값을 넣어서 보내주었기 때문에 여기서 intent를 받아주고
        intent = getIntent();
        setContentView(R.layout.activity_detail_review);
        cosmeticBrand = (TextView)findViewById(R.id.cosmetic_brand_review);
        cosmeticName = (TextView)findViewById(R.id.cosmetic_name_review);
        cosmeticIngredient = (TextView)findViewById(R.id.cosmetic_ingredient_review);
        cosmeticRating = (RatingBar)findViewById(R.id.cosmetic_rating_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //intent를 통해 넘어온 값이 화장품ID이므로 그걸 꺼내줌
        int cosmetic_id = Integer.valueOf(intent.getStringExtra("cosmetic_id"));
        //DB에서 그 ID에 부합하는 화장품의 데이터를 꺼내옴
        String data = db.getCosmeticData(cosmetic_id);
        //꺼내온 데이터를 편집하는 과정
        String cosmeticData[] = data.split("/");
        //화면에 보이는 값들 브랜드, 이름, 별점, 성분들을 받아온 데이터로 변경을 해줌
        cosmeticBrand.setText(cosmeticData[0]);
        cosmeticName.setText(cosmeticData[1]);
        cosmeticRating.setRating(Float.valueOf(cosmeticData[2]));
        cosmeticIngredient.setText(cosmeticData[3]);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        Intent intent = new Intent(DetailReviewActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my) {
            Intent intent = new Intent(DetailReviewActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(DetailReviewActivity.this, CosmeticReviewAndRankingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DetailReviewActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
