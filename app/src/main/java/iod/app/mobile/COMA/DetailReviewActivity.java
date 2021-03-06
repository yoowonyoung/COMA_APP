package iod.app.mobile.COMA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class DetailReviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Intent intent;
    private MySqliteOpenHelper db;
    private TextView cosmeticBrand;
    private TextView cosmeticName;
    private TextView cosmeticIngredient;
    private RatingBar cosmeticRating;
    private BootstrapButton addReview;
    private BootstrapButton modifyReview;
    private BootstrapButton deleteReview;
    private TextView nickname;
    private View header;
    private Intent userData;
    private NavigationView navigationView;
    private ServerManager server;
    private Boolean reviewedFlag = false;
    private ImageView cosmeticImage;
    private TextView reviewText1;
    private TextView reviewText2;
    private TextView reviewText3;
    private RatingBar reviewRating1;
    private RatingBar reviewRating2;
    private RatingBar reviewRating3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MySqliteOpenHelper.getInstance(getApplicationContext());
        //다른데서 넘어올때 intent에 값을 넣어서 보내주었기 때문에 여기서 intent를 받아주고
        intent = getIntent();
        setContentView(R.layout.activity_detail_review);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        nickname = (TextView)header.findViewById(R.id.nav_nickname);
        userData = getIntent();
        nickname.setText(userData.getStringExtra("userNickname"));
        server = ServerManager.getInstance();
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
        cosmeticImage = (ImageView)findViewById(R.id.cosmetic_image_review);
        addReview = (BootstrapButton)findViewById(R.id.cosmetic_add_review);
        modifyReview = (BootstrapButton)findViewById(R.id.cosmetic_modify_review_);
        deleteReview = (BootstrapButton)findViewById(R.id.cosmetic_delete_review);
        reviewText1 = (TextView)findViewById(R.id.review_text_1);
        reviewText2 = (TextView)findViewById(R.id.review_text_2);
        reviewText3 = (TextView)findViewById(R.id.review_text_3);
        reviewRating1 = (RatingBar)findViewById(R.id.review_rating_1);
        reviewRating2 = (RatingBar)findViewById(R.id.review_rating_2);
        reviewRating3 = (RatingBar)findViewById(R.id.review_rating_3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //intent를 통해 넘어온 값이 화장품ID이므로 그걸 꺼내줌
        final int cosmetic_id = Integer.valueOf(intent.getStringExtra("cosmetic_id"));
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
                                if(j == 0) {
                                    String reviewContent = review.getString("review_content");
                                    if(reviewContent.length() > 0) {
                                        Log.e("Here",reviewContent);
                                        reviewText1.setText(reviewContent.split("/")[0]);
                                        reviewRating1.setRating((float)review.getDouble("cosmetic_rank"));
                                    }
                                }
                                if(j == 1) {
                                    String reviewContent = review.getString("review_content");
                                    if(reviewContent.length() > 0) {
                                        reviewText2.setText(reviewContent.split("/")[0]);
                                        reviewRating2.setRating((float)review.getDouble("cosmetic_rank"));
                                    }
                                }
                                if(j == 2) {
                                    String reviewContent = review.getString("review_content");
                                    if(reviewContent.length() > 0) {
                                        reviewText3.setText(reviewContent.split("/")[0]);
                                        reviewRating3.setRating((float)review.getDouble("cosmetic_rank"));
                                    }
                                }
                                if(review.getString("review_name").equals(userData.getStringExtra("userNickname"))) {
                                    reviewedFlag = true;
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

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailReviewActivity.this, AddAndModifyReviewActivity.class);
                intent.putExtra("cosmetic_id",""+cosmetic_id);
                intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                startActivity(intent);
            }
        });
        modifyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewedFlag) {
                    Intent intent = new Intent(DetailReviewActivity.this, AddAndModifyReviewActivity.class);
                    intent.putExtra("cosmetic_id",""+cosmetic_id);
                    intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
                    intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
                    intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
                    intent.putExtra("mode",new String("modify"));
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"등록된 리뷰가 없습니다!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        deleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reviewedFlag) {
                    ReviewDatas review = new ReviewDatas(cosmeticBrand.getText().toString(), cosmeticName.getText().toString(), userData.getStringExtra("userNickname")," ",
                            1, (double)cosmeticRating.getRating(),Integer.valueOf(userData.getStringExtra("cosmetic_id")));
                    server.delete_review(review);
                    Toast.makeText(getApplicationContext(),"삭제 되었습니다!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"등록된 리뷰가 없습니다!",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_review) {
            Intent intent = new Intent(DetailReviewActivity.this, CosmeticReviewAndRankingActivity.class);
            intent.putExtra("userNickname",userData.getStringExtra("userNickname"));
            intent.putExtra("userProfilImage",userData.getStringExtra("userProfilImage"));
            intent.putExtra("userThumbnailImage",userData.getStringExtra("userThumbnailImage"));
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(DetailReviewActivity.this, SettingsActivity.class);
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
