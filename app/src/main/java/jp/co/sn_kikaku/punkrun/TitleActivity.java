package jp.co.sn_kikaku.punkrun;

import android.content.Intent;
import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class TitleActivity extends Activity implements View.OnClickListener{
    private Button gameStart;
    private Button manual;
//    private SoundPool soundPool;
//    private int soundResId;
    private PopupWindow popupManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        gameStart = findViewById(R.id.gameStart);
        gameStart.setOnClickListener(this);
        gameStart.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        //manual = findViewById(R.id.manual);
        //manual.setOnClickListener(this);
        //manual.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.gameStart) {
//            soundPool.play(soundResId,1.0f,1.0f,0,0,1.0f);
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }/*else if(view.getId() == R.id.manual){
            popupManual = new PopupWindow(this);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.layout0).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            findViewById(R.id.layout0).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            findViewById(R.id.layout0).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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
