package in.sanrakshak.googleplaystore;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new HomeFragment(), "Games");
        adapter.addFragment(new HomeFragment(), "Movies");
        adapter.addFragment(new HomeFragment(), "Books");
        adapter.addFragment(new HomeFragment(), "Music");
        viewPager.setAdapter(adapter);
    }
}
