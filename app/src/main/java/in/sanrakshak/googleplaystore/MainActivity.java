package in.sanrakshak.googleplaystore;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sanrakshak.googleplaystore.adapters.ViewPagerAdapter;
import in.sanrakshak.googleplaystore.fragments.main.HomeFragment;
import in.sanrakshak.googleplaystore.viewPager.CustomViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton g_sign,viz;
    RelativeLayout g_sign_pane,g_sign_pane2,viz_pane,viz_main_pane;
    ImageView icon_green;
    TextView profile_name,profile_email;
    LinearLayout profile_cover;
    CircleImageView profileImageView;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
    GoogleSignInAccount account;
    double fabX,fabY;
    boolean isViz=false;
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isViz){
            int cx = viz_pane.getWidth()/2;
            int cy = viz_pane.getHeight()/2;
            int finalRadius = Math.max(viz_pane.getWidth(), viz_pane.getHeight());
            Animator animator=ViewAnimationUtils.createCircularReveal(viz_main_pane, cx, cy,finalRadius, viz.getWidth());
            animator.setDuration(300);
            animator.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {}
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}
                @Override public void onAnimationEnd(Animator animator) {
                    viz_main_pane.setVisibility(View.GONE);
                    float CurrentX = viz.getX();
                    float CurrentY = viz.getY();
                    Path path = new Path();
                    path.moveTo(CurrentX, CurrentY);
                    path.quadTo(CurrentX*3/7, (float)(CurrentY+fabY)*4/6, (float)fabX,(float) fabY);
                    Animator startAnim = ObjectAnimator.ofFloat(viz, View.X, View.Y, path);
                    startAnim.setDuration(300);
                    startAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    startAnim.start();
                }
            });
            animator.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingSearchView mSearchView = findViewById(R.id.search_view);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

            }
        });

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                null,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        profileImageView = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        mSearchView.attachNavigationDrawerToMenuButton(drawer);


        viz_pane=findViewById(R.id.viz_pane);
        viz_main_pane=findViewById(R.id.viz_main_pane);
        viz=findViewById(R.id.viz);
        viz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float CurrentX = viz.getX();
                float CurrentY = viz.getY();
                fabX=CurrentX;
                fabY=CurrentY;
                float FinalX = (viz_pane.getWidth()/2)-(viz.getWidth()/2);
                float FinalY = (viz_pane.getHeight()/2)-(viz.getHeight()/2);
                Path path = new Path();
                path.moveTo(CurrentX, CurrentY);
                path.quadTo(CurrentX*4/3, (CurrentY+FinalY)*2/5, FinalX, FinalY);
                Animator startAnim = ObjectAnimator.ofFloat(viz, View.X, View.Y, path);
                startAnim.setDuration(350);
                startAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                startAnim.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                    @Override public void onAnimationEnd(Animator animator) {
                        viz_main_pane.setVisibility(View.VISIBLE);
                        int cx = viz_pane.getWidth()/2;
                        int cy = viz_pane.getHeight()/2;
                        int finalRadius = Math.max(viz_pane.getWidth(), viz_pane.getHeight());
                        animator=ViewAnimationUtils.createCircularReveal(viz_main_pane, cx, cy, viz.getWidth(), finalRadius);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.setDuration(350);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override public void onAnimationStart(Animator animator) {}
                            @Override public void onAnimationCancel(Animator animator) {}
                            @Override public void onAnimationRepeat(Animator animator) {}
                            @Override public void onAnimationEnd(Animator animator) {
                                new Handler().postDelayed(new Runnable() {@Override public void run() {
                                    Intent intent=new Intent(MainActivity.this,VizActivity.class);
                                    startActivity(intent);isViz=true;
                                    overridePendingTransition(R.anim.splash_in, R.anim.splash_out);
                                }},800);
                            }
                        });
                        animator.start();
                    }
                });
                startAnim.start();
            }
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);

        g_sign_pane=findViewById(R.id.g_sign_pane);
        profile_name=navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        profile_email=navigationView.getHeaderView(0).findViewById(R.id.profile_email);
        profile_cover=navigationView.getHeaderView(0).findViewById(R.id.profile_cover);

        if(account==null){
            icon_green=findViewById(R.id.icon_green);
            g_sign_pane2=findViewById(R.id.g_sign_pane2);
            g_sign=findViewById(R.id.g_sign);
            g_sign_pane.setVisibility(View.VISIBLE);
            g_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cx = g_sign_pane.getWidth()/2;
                    int cy = g_sign.getBottom()-(g_sign.getHeight()/2);
                    Animator animator = ViewAnimationUtils.createCircularReveal(g_sign_pane2, cx, cy, g_sign.getWidth(),g_sign_pane.getHeight());
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setDuration(500);
                    g_sign_pane2.setVisibility(View.VISIBLE);animator.start();

                    new Handler().postDelayed(new Runnable() {@Override public void run() {
                        setStatusBarTextColor(false);
                        g_sign.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        g_sign.setImageDrawable(getResources().getDrawable(R.drawable.google_mono, MainActivity.this.getTheme()));

                        new Handler().postDelayed(new Runnable() {@Override public void run() {
                            Animation anim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                            anim.setDuration(550);icon_green.startAnimation(anim);
                            Intent signInIntent = client.getSignInIntent();
                            startActivityForResult(signInIntent, 0);
                        }},500);
                    }},400);
                }
            });
        }
        else{
            profile_name.setText(Objects.requireNonNull(account).getDisplayName());
            profile_email.setText(account.getEmail());
            Glide.with(MainActivity.this)
                    .load(account.getPhotoUrl())
                    .into(profileImageView);
            getCover(account.getId());
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new HomeFragment(), "Games");
        adapter.addFragment(new HomeFragment(), "Movies");
        adapter.addFragment(new HomeFragment(), "Books");
        adapter.addFragment(new HomeFragment(), "Music");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                assert account != null;
                profile_name.setText(account.getDisplayName());
                profile_email.setText(account.getEmail());
                Glide.with(MainActivity.this)
                        .load(account.getPhotoUrl())
                        .into(profileImageView);
                getCover(account.getId());

                int cx = g_sign_pane.getWidth()/2;
                int cy = g_sign.getBottom()-(g_sign.getHeight()/2);
                Animator animator = ViewAnimationUtils.createCircularReveal(g_sign_pane2, cx, cy, g_sign_pane.getHeight(),0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(500);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        g_sign_pane2.setVisibility(View.GONE);
                        g_sign.setVisibility(View.GONE);
                        g_sign_pane.animate().translationY(-g_sign_pane.getHeight()).setDuration(500);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                });
                animator.start();
                setStatusBarTextColor(true);
            }
            catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setStatusBarTextColor(final boolean light) {
        final int lFlags = getWindow().getDecorView().getSystemUiVisibility();
        getWindow().setStatusBarColor(light? getResources().getColor(R.color.colorPrimaryDark):Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(light ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        }
    }
    public void getCover(String ID){
        Request request = new Request.Builder().url("https://people.googleapis.com/v1/people/"+ID+"?personFields=coverPhotos&key=AIzaSyCmHrrjRt6ryGbnhM6zt4aR7FYornmTWw8").get()
                .addHeader("Content-Type", "application/json").build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.w("coverPic", e.getMessage());
                call.cancel();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String coverJSON = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful())
                {
                    int urlIndex=coverJSON.indexOf("\"url\": \"")+8;
                    final String coverUrl=coverJSON.substring(urlIndex,coverJSON.indexOf("\"",urlIndex));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this)
                                    .load(coverUrl)
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                                            profile_cover.setBackground(resource);
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }
    public void parseCSV()
    {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("appdata.csv")));
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                lineNumber++;
                Log.w("coverPic", nextLine[4]+" , "+lineNumber);
            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }
    }
}
