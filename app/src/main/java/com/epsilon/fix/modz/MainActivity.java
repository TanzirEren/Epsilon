package com.epsilon.fix.modz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup content = findViewById(R.id.content);
        Fx.systemUi(this, content);

        // Glass blur + drifting background
        Fx.blur(findViewById(R.id.blob_layer), 70f);
        Fx.drift(findViewById(R.id.blob1), 40f, 30f, 9000);
        Fx.drift(findViewById(R.id.blob2), 50f, 40f, 12000);
        Fx.drift(findViewById(R.id.blob3), 35f, 45f, 10500);

        // Lists
        Fx.rows(getLayoutInflater(), (LinearLayout) findViewById(R.id.find_list),
                getResources().getStringArray(R.array.find_items));
        Fx.rows(getLayoutInflater(), (LinearLayout) findViewById(R.id.rules_list),
                getResources().getStringArray(R.array.rule_items));

        // Buttons
        final String url = getString(R.string.channel_url);
        View join = findViewById(R.id.btn_join);
        View connect = findViewById(R.id.connect_card);
        View privacy = findViewById(R.id.btn_privacy);
        View share = findViewById(R.id.btn_share);

        join.setOnClickListener(v -> Fx.openUrl(this, url));
        connect.setOnClickListener(v -> Fx.openUrl(this, url));
        privacy.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out_left);
        });
        share.setOnClickListener(v ->
                Fx.share(this, getString(R.string.share_text), getString(R.string.share_chooser)));

        Fx.press(join);
        Fx.press(connect);
        Fx.press(privacy);
        Fx.press(share);

        // Animations
        Fx.hero(findViewById(R.id.logo), findViewById(R.id.glow), findViewById(R.id.ring));
        Fx.sheen(join, findViewById(R.id.sheen));

        // Stagger every block except the hero logo, which has its own pop-in
        Fx.stagger(content, 100, 85);
    }
}
