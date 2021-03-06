package iod.app.mobile.COMA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import iod.app.mobile.COMA.DBStaticValue.REVIEW;

public class AddAndModifyReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private MySqliteOpenHelper db;
    private TextView cosmeticBrand;
    private TextView cosmeticName;
    private TextView cosmeticIngredient;
    private RatingBar cosmeticRating;
    private RatingBar userRating;
    private Spinner cosmeticDurationSpinner;
    private ServerManager server;
    private TextView nickname;
    private View header;
    private Intent userData;
    private NavigationView navigationView;
    private EditText reviewGood;
    private EditText reviewBad;
    private EditText reviewTip;
    private BootstrapButton modifyBtn;
    private Boolean modifyFlag = false;
    private int cosmetic_id;
    private ImageView cosmeticImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        server = ServerManager.getInstance();
        //다른데서 넘어올때 intent에 값을 넣어서 보내주었기 때문에 여기서 intent를 받아주고
        setContentView(R.layout.activity_add_and_modify_review);
        reviewGood = (EditText)findViewById(R.id.cosmetic_review_good);
        reviewBad = (EditText)findViewById(R.id.cosmetic_review_bad);
        reviewTip = (EditText)findViewById(R.id.cosmetic_review_tip);
        modifyBtn = (BootstrapButton)findViewById(R.id.cosmetic_modify_review);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        nickname = (TextView)header.findViewById(R.id.nav_nickname);
        cosmeticImage = (ImageView)findViewById(R.id.cosmetic_image_add_review);
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
        cosmeticBrand = (TextView)findViewById(R.id.cosmetic_brand_review);
        cosmeticName = (TextView)findViewById(R.id.cosmetic_name_review);
        cosmeticIngredient = (TextView)findViewById(R.id.cosmetic_ingredient_review);
        cosmeticRating = (RatingBar)findViewById(R.id.cosmetic_rating_review);
        cosmeticDurationSpinner = (Spinner)findViewById(R.id.cosmetic_duration_spinner);
        userRating = (RatingBar)findViewById(R.id.cosmetic_rating_add_review);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cosmetic_duration, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cosmeticDurationSpinner.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //intent를 통해 넘어온 값이 화장품ID이므로 그걸 꺼내줌
        cosmetic_id = Integer.valueOf(userData.getStringExtra("cosmetic_id"));
        try {
            JSONObject obj = new JSONObject(server.getAllCosmetic_withReview());
            JSONArray jsonArray = (JSONArray) obj.get("data");
            for(int i = 0; i < jsonArray.length(); i++) {
                final JSONObject temp = jsonArray.getJSONObject(i);
                if(temp.getInt("cosmetic_id") == cosmetic_id) {
                    cosmeticBrand.setText(temp.getString("cosmetic_brand_name"));
                    cosmeticName.setText(temp.getString("cosmetic_name"));
                    cosmeticIngredient.setText(temp.getString("cosmetic_ingredient"));
                    cosmeticRating.setRating(Float.valueOf(temp.getString("cosmetic_rank")));
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {    // 오래 거릴 작업을 구현한다
                            // TODO Auto-generated method stub
                            try{
                                URL url = new URL(temp.getString("cosmetic_image"));
                                InputStream is = url.openStream();
                                final Bitmap bm = BitmapFactory.decodeStream(is);
                                cosmeticImage.setImageBitmap(bm);
                            } catch(Exception e){
                            }
                        }
                    });
                    thread.start();
                    JSONObject reviewData = temp.getJSONObject("r_data");
                    if(reviewData != null) {
                        JSONArray reviews = reviewData.getJSONArray("c_reviews");
                        if(reviews != null) {
                            for(int j = 0; j < reviews.length(); j++) {
                                JSONObject review = reviews.getJSONObject(j);
                                if(review.getString("review_name").equals(userData.getStringExtra("userNickname"))) {
                                    Toast.makeText(getApplicationContext(),"이미 리뷰를 등록한 화장품 입니다. \n리뷰 수정으로 이동됩니다.",Toast.LENGTH_SHORT).show();
                                    String goodAndBadAndTip[] = review.getString("review_content").split("/");
                                    reviewGood.setText(goodAndBadAndTip[0]);
                                    reviewBad.setText(goodAndBadAndTip[1]);
                                    reviewTip.setText(goodAndBadAndTip[2]);
                                    cosmeticDurationSpinner.setSelection(review.getInt("cosmetic_duration"));
                                    userRating.setRating(Float.valueOf(review.getString("cosmetic_rank")));
                                    modifyFlag = true;
                                }
                            }
                        }
                    }
                }else {
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        //DB에서 그 ID에 부합하는 화장품의 데이터를 꺼내옴
        String data = db.getCosmeticData(cosmetic_id);
        //꺼내온 데이터를 편집하는 과정
        String cosmeticData[] = data.split("/");
        //화면에 보이는 값들 브랜드, 이름, 별점, 성분들을 받아온 데이터로 변경을 해줌
        cosmeticBrand.setText(cosmeticData[0]);
        cosmeticName.setText(cosmeticData[1]);
        cosmeticRating.setRating(Float.valueOf(cosmeticData[2]));
        cosmeticIngredient.setText(cosmeticData[3]);
        */

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modifyFlag) {
                    ReviewDatas review = new ReviewDatas(cosmeticBrand.getText().toString(), cosmeticName.getText().toString(), userData.getStringExtra("userNickname"),reviewGood.getText() + "/" + reviewBad.getText() + "/" + reviewTip.getText(),
                            cosmeticDurationSpinner.getSelectedItemPosition(), userRating.getRating(),Integer.valueOf(userData.getStringExtra("cosmetic_id")));
                    server.modify_reveiw(review);
                    Intent intent = new Intent(AddAndModifyReviewActivity.this, MainActivity.class);
                    intent.putExtra("cosmetic_id",""+cosmetic_id);
                    intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                    intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                    intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                    startActivity(intent);
                    finish();
                }else {
                    ReviewDatas review = new ReviewDatas(cosmeticBrand.getText().toString(), cosmeticName.getText().toString(), userData.getStringExtra("userNickname"),reviewGood.getText() + "/" + reviewBad.getText() + "/" + reviewTip.getText(),
                            cosmeticDurationSpinner.getSelectedItemPosition(), userRating.getRating(),Integer.valueOf(userData.getStringExtra("cosmetic_id")));
                    server.store_review(review);
                    Intent intent = new Intent(AddAndModifyReviewActivity.this, MainActivity.class);
                    intent.putExtra("cosmetic_id",""+cosmetic_id);
                    intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                    intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                    intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddAndModifyReviewActivity.this, MainActivity.class);
        intent.putExtra("cosmetic_id",""+cosmetic_id);
        intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
        intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
        intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Intent intent = new Intent(AddAndModifyReviewActivity.this, MainActivity.class);
        intent.putExtra("cosmetic_id",""+cosmetic_id);
        intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
        intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
        intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my) {
            Intent intent = new Intent(AddAndModifyReviewActivity.this, MainActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(AddAndModifyReviewActivity.this, CosmeticReviewAndRankingActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(AddAndModifyReviewActivity.this, SettingsActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
