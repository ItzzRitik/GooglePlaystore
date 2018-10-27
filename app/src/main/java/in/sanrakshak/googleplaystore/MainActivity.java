package in.sanrakshak.googleplaystore;


import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;
import in.sanrakshak.googleplaystore.adapters.ViewPagerAdapter;
import in.sanrakshak.googleplaystore.fragments.main.HomeFragment;
import in.sanrakshak.googleplaystore.viewPager.CustomViewPager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton g_sign;
    RelativeLayout g_sign_pane,g_sign_pane2;
    ImageView icon_green;
    CircleImageView profileImageView;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
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




        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);
        g_sign_pane=findViewById(R.id.g_sign_pane);
        if(GoogleSignIn.getLastSignedInAccount(this)==null){
            icon_green=findViewById(R.id.icon_green);
            g_sign_pane2=findViewById(R.id.g_sign_pane2);
            g_sign=findViewById(R.id.g_sign);
            g_sign_pane.setVisibility(View.VISIBLE);
            g_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cx = g_sign_pane.getWidth()/2;
                    int cy = g_sign_pane.getHeight()-dptopx(130);
                    Animator animator =ViewAnimationUtils.createCircularReveal(g_sign_pane2, cx, cy, g_sign.getWidth(),g_sign_pane.getHeight());
                    animator.setDuration(300);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override public void onAnimationStart(Animator animator) {}
                        @Override public void onAnimationCancel(Animator animator) {}
                        @Override public void onAnimationRepeat(Animator animator) {}
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            getWindow().setStatusBarColor(Color.WHITE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            }
                            g_sign.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                            g_sign.setImageDrawable(getResources().getDrawable(R.drawable.google_mono, MainActivity.this.getTheme()));

                            new Handler().postDelayed(new Runnable() {@Override public void run() {
                                Animation anim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_trans);
                                anim.setDuration(550);icon_green.startAnimation(anim);
                                new Handler().postDelayed(new Runnable() {@Override public void run() {
                                    Intent signInIntent = client.getSignInIntent();
                                    startActivityForResult(signInIntent, 0);
                                }},500);
                            }},500);

                        }
                    });
                    g_sign_pane2.setVisibility(View.VISIBLE);animator.start();


                }
            });
        }
        else{
            g_sign_pane.setVisibility(View.GONE);
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
    public int dptopx(float dp)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public int pxtodp(float px)
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Glide.with(this)
                        .load(account.getPhotoUrl())
                        .into(profileImageView);

                Toast.makeText(this, account.getEmail()+"\n"+account.getDisplayName(), Toast.LENGTH_SHORT).show();
                int cx = g_sign_pane.getWidth()/2;
                int cy = g_sign_pane.getHeight()-dptopx(130);
                Animator animator =ViewAnimationUtils.createCircularReveal(g_sign_pane, cx, cy, g_sign_pane.getHeight(),0);
                animator.setDuration(300);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        g_sign_pane.setBackgroundColor(Color.WHITE);
                        g_sign_pane.setVisibility(View.GONE);
                    }
                });
                animator.start();
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
            catch (Exception e) {
            }
        }
    }
}
