package com.epsilon.fix.modz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** Small helper toolkit: system bars, glass blur, animations, links. */
final class Fx {

    private Fx() {}

    static float dp(Context c, float v) {
        return v * c.getResources().getDisplayMetrics().density;
    }

    /** Edge-to-edge light status bar; pushes content below the status bar. */
    static void systemUi(Activity a, View contentToPad) {
        Window w = a.getWindow();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 23) flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        if (Build.VERSION.SDK_INT >= 26) flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        w.getDecorView().setSystemUiVisibility(flags);
        w.setStatusBarColor(0x00000000);

        int resId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int status = resId > 0 ? a.getResources().getDimensionPixelSize(resId) : (int) dp(a, 24);
        contentToPad.setPadding(contentToPad.getPaddingLeft(),
                contentToPad.getPaddingTop() + status,
                contentToPad.getPaddingRight(),
                contentToPad.getPaddingBottom());
    }

    /** Real blur on Android 12+; older versions rely on the soft radial gradients. */
    static void blur(View v, float radiusDp) {
        if (Build.VERSION.SDK_INT >= 31) {
            float r = dp(v.getContext(), radiusDp);
            v.setRenderEffect(RenderEffect.createBlurEffect(r, r, Shader.TileMode.CLAMP));
        }
    }

    /** Slowly drifting background blobs. */
    static void drift(View v, float dx, float dy, long duration) {
        Context c = v.getContext();
        ObjectAnimator ax = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, -dp(c, dx), dp(c, dx));
        ObjectAnimator ay = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, dp(c, dy), -dp(c, dy));
        ObjectAnimator sx = ObjectAnimator.ofFloat(v, View.SCALE_X, 0.9f, 1.15f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(v, View.SCALE_Y, 0.9f, 1.15f);
        for (ObjectAnimator o : new ObjectAnimator[]{ax, ay, sx, sy}) {
            o.setRepeatCount(ObjectAnimator.INFINITE);
            o.setRepeatMode(ObjectAnimator.REVERSE);
            o.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        set.playTogether(ax, ay, sx, sy);
        set.start();
    }

    /** Fade + slide up each child of the container, one after another. */
    static void stagger(ViewGroup container, long firstDelay, long step) {
        float shift = dp(container.getContext(), 56);
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setAlpha(0f);
            child.setTranslationY(shift);
            child.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(firstDelay + i * step)
                    .setDuration(560)
                    .setInterpolator(new DecelerateInterpolator(1.7f))
                    .start();
        }
    }

    /** Pop-in, then endless float for the logo; glow pulse; dashed ring spin. */
    static void hero(View logo, View glow, View ring) {
        Context c = logo.getContext();

        logo.setScaleX(0.4f);
        logo.setScaleY(0.4f);
        logo.setAlpha(0f);
        logo.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setStartDelay(150).setDuration(750)
                .setInterpolator(new OvershootInterpolator(1.6f)).start();

        ObjectAnimator floatY = ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, -dp(c, 7), dp(c, 7));
        floatY.setDuration(2600);
        floatY.setRepeatCount(ObjectAnimator.INFINITE);
        floatY.setRepeatMode(ObjectAnimator.REVERSE);
        floatY.setInterpolator(new AccelerateDecelerateInterpolator());
        floatY.setStartDelay(900);
        floatY.start();

        ObjectAnimator gx = ObjectAnimator.ofFloat(glow, View.SCALE_X, 0.88f, 1.12f);
        ObjectAnimator gy = ObjectAnimator.ofFloat(glow, View.SCALE_Y, 0.88f, 1.12f);
        ObjectAnimator ga = ObjectAnimator.ofFloat(glow, View.ALPHA, 0.55f, 1f);
        for (ObjectAnimator o : new ObjectAnimator[]{gx, gy, ga}) {
            o.setRepeatCount(ObjectAnimator.INFINITE);
            o.setRepeatMode(ObjectAnimator.REVERSE);
            o.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        AnimatorSet pulse = new AnimatorSet();
        pulse.setDuration(2200);
        pulse.playTogether(gx, gy, ga);
        pulse.start();

        ObjectAnimator spin = ObjectAnimator.ofFloat(ring, View.ROTATION, 0f, 360f);
        spin.setDuration(16000);
        spin.setRepeatCount(ObjectAnimator.INFINITE);
        spin.setInterpolator(new LinearInterpolator());
        spin.start();
    }

    /** Light sweep across the primary button. */
    static void sheen(final View button, final View sheen) {
        button.post(() -> {
            float from = -dp(button.getContext(), 90);
            float to = button.getWidth() + dp(button.getContext(), 20);
            sheen.setRotation(18f);
            ObjectAnimator sweep = ObjectAnimator.ofFloat(sheen, View.TRANSLATION_X, from, to);
            sweep.setDuration(2800);
            sweep.setRepeatCount(ObjectAnimator.INFINITE);
            sweep.setInterpolator((TimeInterpolator) t -> t < 0.55f ? t / 0.55f : 1f);
            sweep.start();
        });
    }

    /** Springy press feedback. */
    static void press(View v) {
        v.setOnTouchListener((view, e) -> {
            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(110).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(260)
                            .setInterpolator(new OvershootInterpolator(2.4f)).start();
                    break;
                default:
                    break;
            }
            return false; // let the click through
        });
    }

    /** Fill a container with "emoji|text" rows. */
    static void rows(LayoutInflater inflater, LinearLayout container, String[] items) {
        for (String item : items) {
            int bar = item.indexOf('|');
            View row = inflater.inflate(R.layout.item_row, container, false);
            ((TextView) row.findViewById(R.id.row_icon)).setText(item.substring(0, bar));
            ((TextView) row.findViewById(R.id.row_text)).setText(item.substring(bar + 1));
            container.addView(row);
        }
    }

    static void openUrl(Context c, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(c, "No app found to open the link", Toast.LENGTH_SHORT).show();
        }
    }

    static void share(Context c, String text, String chooserTitle) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooser = Intent.createChooser(i, chooserTitle);
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(chooser);
    }
}
