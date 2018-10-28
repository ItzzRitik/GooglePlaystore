package in.sanrakshak.googleplaystore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class VizActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.splash_out, R.anim.splash_in);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viz);
        setTitle("Visualize Play Store");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
