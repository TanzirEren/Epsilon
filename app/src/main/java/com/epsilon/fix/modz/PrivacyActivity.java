package com.epsilon.fix.modz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PrivacyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        final ViewGroup content = findViewById(R.id.content);
        Fx.systemUi(this, content);

        Fx.blur(findViewById(R.id.blob_layer), 70f);
        Fx.drift(findViewById(R.id.blob1), 40f, 30f, 9500);
        Fx.drift(findViewById(R.id.blob2), 50f, 40f, 12500);
        Fx.drift(findViewById(R.id.blob3), 35f, 45f, 11000);

        Fx.rows(getLayoutInflater(), (LinearLayout) findViewById(R.id.privacy_list),
                getResources().getStringArray(R.array.privacy_items));

        View back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> onBackPressed());
        Fx.press(back);

        Fx.stagger(content, 80, 90);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out_right);
    }
}
