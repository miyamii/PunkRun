package jp.co.sn_kikaku.punkrun;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.TextView;

public class ExitActivity extends Activity implements View.OnClickListener{
    private Button retake;
    private Button exit;
//    private SoundPool soundPool;
//    private int soundResId;
    private int score;
    private float CH;
    private TextView titleScore;
    private TextView printScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        retake = findViewById(R.id.retake);
        retake.setOnClickListener(this);
        retake.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);
        exit.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        printScore = findViewById(R.id.totalScore);
        titleScore = findViewById(R.id.titleScore);
        printScore.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        titleScore.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // titleScore.getText();
                Intent intent = getIntent();
                score = intent.getIntExtra("score" , 0);
                CH = intent.getFloatExtra("CH" , 40);
                titleScore.setPadding(0,(int)(CH - (CH / 4)),0,0);
                printScore.setPadding(0,(int)(CH * 1.5),0,0);
                printScore.setText(String.valueOf(score));
                ObjectAnimator animator = ObjectAnimator.ofFloat(titleScore, "alpha", 0f, 1.0f );
                animator.setDuration(2000);
                animator.start();
            }
        }, 1000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(printScore, "alpha", 0f, 1.0f );
                animator.setDuration(2000);
                animator.start();
            }
        }, 4000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(retake, "alpha", 0f, 1.0f );
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(exit, "alpha", 0f, 1.0f );
                animator1.setDuration(2000);
                animator2.setDuration(2000);
                animator1.start();
                animator2.start();
            }
        }, 6500);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.retake) {
//            soundPool.play(soundResId,1.0f,1.0f,0,0,1.0f);
            intent = new Intent(this, TitleActivity.class);
            startActivity(intent);
            this.finish();
        }else if(view.getId() == R.id.exit){
//            soundPool.play(soundResId,1.0f,1.0f,0,0,1.0f);
            // アプリを終了させる
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.layout2).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            findViewById(R.id.layout2).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            findViewById(R.id.layout2).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
//        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM,0);
//        soundResId = soundPool.load(this,R.raw.click,1);
    }

    @Override
    protected void onPause(){
        super.onPause();
//        soundPool.release();
    }
}
