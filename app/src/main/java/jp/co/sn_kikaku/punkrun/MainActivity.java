package jp.co.sn_kikaku.punkrun;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{
    private MySurfaceView surfaceView;
    // 座標用リスト
    private ArrayList<Item> item = new ArrayList<>();
    private LinearLayout playArea;
    private Button up;
    private Button down;
    private TextView headingTime;
    private TextView headingScore;

    private float width;
    private float height;
    private float playAreaWidth;
    private float playAreaHeight;
    private float appearAreaX;
    private float appearAreaY;
    private float cellWidth;
    private float cellHeight;
    private float readyX;
    private float readyY;
    private float finishX;
    private float finishY;
    private float ctrX;
    private float bgY;

    // 主人公初期位置
    float playerX = 0f;
    float playerY = 0f;

    public float getCellWidth() {
        return cellWidth;
    }
    public void setCellWidth(float cellWidth){
        this.cellWidth = cellWidth;
    }

    public float getCellHeight() {
        return cellHeight;
    }
    public void setCellHeight(float cellHeight){
        this.cellHeight = cellHeight;
    }

    public float getWidth() {
        return width;
    }
    public void setWidth(float width){
        this.width = width;
    }

    public float getHeight() {
        return height;
    }
    public void setHeight(float height){
        this.height = height;
    }

    public float getBgY() {
        return bgY;
    }
    public void setBgY(float bgY){
        this.bgY = bgY;
    }

    public float getCtrX() {
        return ctrX;
    }
    public void setCtrX(float ctrX){
        this.ctrX = ctrX;
    }

    public float getPlayerX() {
        return playerX;
    }
    public void setPlayerX(float playerX){
        this.playerX = playerX;
    }

    public float getPlayerY() {
        return playerY;
    }
    public void setPlayerY(float playerY){
        this.playerY = playerY;
    }

    public float getReadyX() {
        return readyX;
    }
    public void setReadyX(float readyX){
        this.readyX = readyX;
    }

    public float getReadyY() {
        return readyY;
    }
    public void setReadyY(float readyY){
        this.readyY = readyY;
    }

    public float getFinishX() {
        return finishX;
    }
    public void setFinishX(float finishX){
        this.finishX = finishX;
    }

    public float getFinishY() {
        return finishY;
    }
    public void setFinishY(float finishY){
        this.finishY = finishY;
    }

    public float getAppearAreaX() {
        return appearAreaX;
    }
    public void setAppearAreaX(float appearAreaX){
        this.appearAreaX = appearAreaX;
    }

    public float getAppearAreaY() {
        return appearAreaY;
    }
    public void setAppearAreaY(float appearAreaY){
        this.appearAreaY= appearAreaY;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //画面サイズ取得
        windowSize();
        calc();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(new MySurfaceView(this), params);

        // コントローラーマッピング
        up = findViewById(R.id.up);
        up.setOnClickListener(this);
        down = findViewById(R.id.down);
        down.setOnClickListener(this);
        headingTime = findViewById(R.id.headingTime);
        headingScore = findViewById(R.id.headingScore);

        headingTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        headingScore.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 横のバー消す
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            findViewById(R.id.layout1).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            findViewById(R.id.layout1).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            findViewById(R.id.layout1).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //timer.cancel();
        //timer = null;
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    public void onClick(View view) {

            if (view.getId() == R.id.up && (playerY - cellHeight) >= 0) {
                // 主人公座標記憶用変数へ移動先の値を代入
                playerY -= cellHeight;
                //Log.v("tag", "↑"+ playerY);
            } else if (view.getId() == R.id.down && (playerY + cellHeight) < playAreaHeight) {
                // 主人公座標記憶用変数へ移動先の値を代入
                playerY += cellHeight;
                //Log.v("tag", "↓"+ playerY);
            }

    }

    // 今のところ強制終了する
    @Override
    protected void onPause() {
        super.onPause();
        //mediaPlayer.pause();
        // 頭からmsecまで(これいる？)
        //mediaPlayer.seekTo(0);
    }
    // 画面解像度を取得するメソッド
    public void windowSize(){
        //画面サイズ取得の準備
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        // AndroidのAPIレベルによって画面サイズ取得方法が異なるので条件分岐
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
            width = disp.getWidth();
            height = disp.getHeight();

        } else {
            Point size = new Point();
            disp.getRealSize(size);
            width = size.x;
            height = size.y;
        }
    }

    // 1マス当たりの幅と高さを計算するメソッド
    public void calc(){
        playAreaWidth = (width / 10) * 9;
        playAreaHeight = (height / 24) * 16;
        ctrX = (width / 10) * 1;
        bgY = (height / 24) * 5;

        // 1マスあたりの幅と高さ
        cellWidth = playAreaWidth / 9;
        cellHeight = playAreaHeight / 4;

        playerX = ctrX;
        playerY = bgY;

        appearAreaX = ctrX + (cellWidth * 8);
        appearAreaY = bgY;

        readyX = (width / 2) - (cellWidth * 2);
        readyY = (height / 2) - (cellHeight * 2);

        finishX = (width / 2) - (cellWidth * 3);
        finishY= (height / 2) - (cellHeight * (float)1.5);

        // ★数値確認用
        //Log.v("tag", "画面のサイズ"+ width +","+ height);
        //Toast.makeText(this, String.valueOf(cellWidth) + "," + String.valueOf(cellHeight) +"," + String.valueOf(ctrX)+"," + String.valueOf(bgY), Toast.LENGTH_LONG).show();
    }
}
