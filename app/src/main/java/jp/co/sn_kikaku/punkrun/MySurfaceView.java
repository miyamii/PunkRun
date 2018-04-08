package jp.co.sn_kikaku.punkrun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 動的な描画関係のSurfaceViewクラス
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private Random rd = new Random();
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SurfaceHolder holder;
    private Thread thread;
    private Context context;
    private Paint paint = new Paint();
    private String[] items = {"bronze","silver","gold","donut","trap"};
    // 座標用リスト
    private Item itemBean;
    private ArrayList<Item> item = new ArrayList<>();
    // 幅と高さは不変だからfinalでok
    float CW;
    float CH;
    // アイテム出現マス数
    private final int ITEM_CELL = 16;
    private int cellNum;
    private String name;
    private float cellItemX;
    private float cellItemY;
    private float playerX;
    private float playerY;
    private float appearAreaX;
    private float appearAreaY;
    private float itemX;
    private float itemY;
    private float readyX;
    private float readyY;
    private float finishX;
    private float finishY;
    private String itemName;
    private boolean hitFlag;
    // 制限時間
    private int time = 60;
    // 残り時間表示用変数
    private TextView showTime;
    private int score = 0;
    private TextView showScore;
//    private SoundPool soundPool;
//    private int soundResId1;
//    private int soundResId2;
//    private int soundResId3;
//    private int soundResId4;
//    private int soundResId5;
//    private Resources res = this.getContext().getResources();
    private Bitmap player;
    private Bitmap countDown;
    private Bitmap fin;
    private Bitmap imgPlayer0 = BitmapFactory.decodeResource(getResources(),R.drawable.player0);
    private Bitmap imgPlayer1 = BitmapFactory.decodeResource(getResources(),R.drawable.player1);
    private Bitmap imgPlayer2 = BitmapFactory.decodeResource(getResources(),R.drawable.player2);
    private Bitmap imgPlayer3 = BitmapFactory.decodeResource(getResources(),R.drawable.player3);
    private Bitmap imgPlayer4 = BitmapFactory.decodeResource(getResources(),R.drawable.player4);
    private Bitmap imgBronze = BitmapFactory.decodeResource(getResources(),R.drawable.bronze);
    private Bitmap imgSilver = BitmapFactory.decodeResource(getResources(),R.drawable.silver);
    private Bitmap imgGold = BitmapFactory.decodeResource(getResources(),R.drawable.gold);
    private Bitmap imgDonut = BitmapFactory.decodeResource(getResources(),R.drawable.donut);
    private Bitmap imgTrap = BitmapFactory.decodeResource(getResources(),R.drawable.trap);
    private Bitmap imgThree = BitmapFactory.decodeResource(getResources(),R.drawable.three);
    private Bitmap imgTwo = BitmapFactory.decodeResource(getResources(),R.drawable.two);
    private Bitmap imgOne = BitmapFactory.decodeResource(getResources(),R.drawable.one);
    private Bitmap imgGo = BitmapFactory.decodeResource(getResources(),R.drawable.zero);
    private Bitmap imgFinish = BitmapFactory.decodeResource(getResources(),R.drawable.finish);

    private boolean finishFlag = false;
    boolean isCountdown = true;

    public int getScore() {
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }

    public boolean getFinishFlag() {
        return finishFlag;
    }
    public void setFinishFlag(boolean finishFlag){
        this.finishFlag = finishFlag;
    }

    // コンストラクタ
    public MySurfaceView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        CW = ((MainActivity)getContext()).getCellWidth();
        CH = ((MainActivity)getContext()).getCellHeight();
        setZOrderOnTop(true);
        holder = getHolder();
        holder.addCallback(this);
        // 背景透過
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }


    public void itemChoice(){
        int itemPer;
        int itemNum = 0;
        // アイテム抽選
        itemPer = rd.nextInt(100);
        // 出現確率(通常)
        if(itemPer < 40){
            itemNum = 0;
        }else if(itemPer < 65){
            itemNum = 1;
        }else if(itemPer < 80){
            itemNum = 2;
        }else if(itemPer < 90){
            itemNum = 3;
        }else if(itemPer < 100){
            itemNum = 4;
        }
        name = items[itemNum];
    }

    public void cellChoice(float cellHeight){
        // アイテムが落ちる座標を抽選
        cellNum = rd.nextInt(ITEM_CELL);

        //if(cellNum % 4 == 0){
        cellItemX = appearAreaX;
        //}else if(cellNum % 4 == 1){
        //    cellItemX = appearAreaX + cellWidth;
        //}else if(cellNum % 4 == 2){
        //   cellItemX = appearAreaX + (cellWidth * 2);
        //}else if(cellNum % 4 == 3){
        //   cellItemX = appearAreaX + (cellWidth * 3);
        //}

        if(cellNum % 4 == 0){
            cellItemY = appearAreaY;
        }else if(cellNum % 4 == 1){
            cellItemY = appearAreaY + cellHeight;
        }else if(cellNum % 4 == 2){
            cellItemY = appearAreaY + (cellHeight * 2);
        }else if(cellNum % 4 == 3){
            cellItemY = appearAreaY + (cellHeight * 3);
        }
    }
    // 走るアニメーションのための画像差し替えロジック(パラパラ漫画式)
    public void playerAnimetion(){
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                handler.post( new Runnable() {
                    public void run() {
                        if(player == imgPlayer0 || player == imgPlayer4){
                            player = imgPlayer1;
                        }else if(player == imgPlayer1){
                            player = imgPlayer2;
                        }else if(player == imgPlayer2){
                            player = imgPlayer3;
                        }else{
                            player = imgPlayer4;
                        }
                    }
                });
            }
        },5000,400);
    }

    // SE読み込み
/*    public void seLoading(){
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM,0);
        soundResId1 = soundPool.load(getContext(),R.raw.countdown,1);
        soundResId2 = soundPool.load(getContext(),R.raw.normal,1);
        soundResId3 = soundPool.load(getContext(),R.raw.click,1);
        soundResId4 = soundPool.load(getContext(),R.raw.down,1);
        soundResId5 = soundPool.load(getContext(),R.raw.finish,1);
    }

    public void playBGM(){
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.bgm);
        mediaPlayer.setLooping(true);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        }, 4500);
    }
*/
    // ゲーム開始カウントダウン
    public void ready(){
        // loadingが終わってから
/*        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (0 == status && isCountdown) {
                    soundPool.play(soundResId1,1.0f,1.0f,0,0,1.0f);
                    isCountdown = false;
                }
            }
        });
*/
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                handler.post( new Runnable() {
                    public void run() {

                        if(countDown == imgThree){
                            countDown = imgTwo;
                        }else if(countDown == imgTwo){
                            countDown = imgOne;
                        }else if(countDown == imgOne){
                            countDown = imgGo;
                        }else{
                            countDown = null;
                        }
                    }
                });
            }
        },1000,1000);
    }

    // 画像リサイズ用メソッド
    public void imgResize(){
        imgPlayer0 = Bitmap.createScaledBitmap(imgPlayer0, (int) CW, (int) CH, false);
        imgPlayer1 = Bitmap.createScaledBitmap(imgPlayer1, (int) CW, (int) CH, false);
        imgPlayer2 = Bitmap.createScaledBitmap(imgPlayer2, (int) CW, (int) CH, false);
        imgPlayer3 = Bitmap.createScaledBitmap(imgPlayer3, (int) CW, (int) CH, false);
        imgPlayer4 = Bitmap.createScaledBitmap(imgPlayer4, (int) CW, (int) CH, false);
        imgBronze = Bitmap.createScaledBitmap(imgBronze, (int) CW, (int) CH, false);
        imgSilver = Bitmap.createScaledBitmap(imgSilver, (int) CW, (int) CH, false);
        imgGold = Bitmap.createScaledBitmap(imgGold, (int) CW, (int) CH, false);
        imgDonut = Bitmap.createScaledBitmap(imgDonut, (int) CW, (int) CH, false);
        imgTrap = Bitmap.createScaledBitmap(imgTrap, (int) CW, (int) CH, false);
        imgThree = Bitmap.createScaledBitmap(imgThree, (int) CW * 4, (int) CH * 4, false);
        imgTwo = Bitmap.createScaledBitmap(imgTwo, (int) CW * 4, (int) CH * 4, false);
        imgOne = Bitmap.createScaledBitmap(imgOne, (int) CW * 4, (int) CH * 4, false);
        imgGo = Bitmap.createScaledBitmap(imgGo, (int) CW * 4, (int) CH * 4, false);
        imgFinish = Bitmap.createScaledBitmap(imgFinish, (int) CW * 6, (int) CH * 3, false);
    }

    public void showItem(){
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                handler.post( new Runnable() {
                    public void run() {
                        // アイテムの種類を抽選
                        itemChoice();
                        cellChoice(CH);
                        // 画像表示と座標管理？
                        if(time > 0){
                            itemBean = new Item();
                            itemBean.setItemX(cellItemX);
                            itemBean.setItemY(cellItemY);
                            itemBean.setHitFlag(true);
                            if (name.equals("bronze")) {
                                itemBean.setItemBitmap(imgBronze);
                                itemBean.setItemName("bronze");
                            } else if (name.equals("silver")) {
                                itemBean.setItemBitmap(imgSilver);
                                itemBean.setItemName("silver");
                            } else if (name.equals("gold")) {
                                itemBean.setItemBitmap(imgGold);
                                itemBean.setItemName("gold");
                            } else if (name.equals("donut")) {
                                itemBean.setItemBitmap(imgDonut);
                                itemBean.setItemName("donut");
                            } else if (name.equals("trap")) {
                                itemBean.setItemBitmap(imgTrap);
                                itemBean.setItemName("trap");
                            }
                            // ArrayListに追加
                            item.add(itemBean);
                        }

                    }
                });
            }
        },4000,1200);
    }
    public void limiter(){
        // 残り時間タイマー用変数
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                handler.post( new Runnable() {
                    public void run() {
                        time--;
                        // 表示する時間(秒)
                        if (time > 0) {
                            // 何もしない
                        } else {
                            time = 0;
                        }
                    }
                });
            }
            // カウントダウン3秒＋スタート表示1秒
        },5000,1000);
    }
    //当たり判定
    public void judgeHit(){
        // 当たり範囲は調整
        if((((playerX + (CW - CW/4)) >= itemX + (CW/4)) && (playerX <= itemX + (CW/4))) && playerY == itemY && hitFlag){
            // アイテムごとの処理内容
            if (itemName.equals("bronze")) {
                score += 100;
//                soundPool.play(soundResId2,1.0f,1.0f,0,0,1.0f);
            } else if (itemName.equals("silver")) {
                score += 150;
//                soundPool.play(soundResId2,1.0f,1.0f,0,0,1.0f);
            } else if (itemName.equals("gold")) {
                score += 200;
//                soundPool.play(soundResId2,1.0f,1.0f,0,0,1.0f);
            } else if (itemName.equals("donut")) {
                time += 5;
                score += 10;
//                soundPool.play(soundResId3,1.0f,1.0f,0,0,1.0f);
            } else if (itemName.equals("trap")) {
                time -= 5;
//                soundPool.play(soundResId4,1.0f,1.0f,0,0,1.0f);
                if (time <= 0) {
                    time = 0;
                }
            }
            // 最初の描画時のみ加算されるようにフラグを設定
            hitFlag = false;
        }
    }

    public void finish(){
        // finish処理
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                fin = imgFinish;
                finishFlag = true;
            }
        }, 1500);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // 数値表示用マッピング
//        seLoading();
        showTime = ((Activity)context).findViewById(R.id.time);
        showScore = ((Activity)context).findViewById(R.id.score);
        showTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        showScore.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "cinecaption226.ttf"));
        // 開始時の残り時間とスコア表示
        showTime.setText(String.valueOf(time));
        showScore.setText(String.valueOf(score));
        imgResize();
//        playBGM();
        // delayのタイミングの関係上ここ
        player = imgPlayer0;
        playerAnimetion();
        limiter();
        countDown = imgThree;
        ready();
        // アイテムの種類と位置を抽選
        itemChoice();
        cellChoice(CH);
        showItem();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread = null;
    }

    // ここに描画処理書いてく
    @Override
    public void run() {
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    handler.post( new Runnable() {
                        public void run() {
                            playerX = ((MainActivity)getContext()).getPlayerX();
                            playerY = ((MainActivity)getContext()).getPlayerY() + CH;
                            appearAreaX = ((MainActivity)getContext()).getAppearAreaX();
                            appearAreaY = ((MainActivity)getContext()).getAppearAreaY();
                            readyX = ((MainActivity)getContext()).getReadyX();
                            readyY = ((MainActivity)getContext()).getReadyY();
                            finishX = ((MainActivity)getContext()).getFinishX();
                            finishY = ((MainActivity)getContext()).getFinishY();
                            float itemScroll = CW / 5;
                            Canvas canvas = holder.lockCanvas();
                            // 前回の描画を消去(これがないと前のが残ったままになる)
                            if(canvas != null){
                                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                            }

                            // ここで描画している
                            for(int i = 0; i < item.size(); i++){

                                Item cd = item.get(i);
                                Bitmap itemBitmap = cd.getItemBitmap();
                                itemX = cd.getItemX()-itemScroll;
                                itemY = cd.getItemY();
                                itemName = cd.getItemName();
                                hitFlag = cd.getHitFlag();
                                // コントロールエリアにきたら描画しない
                                if(itemX > ((MainActivity)getContext()).getCtrX()){
                                    if(canvas != null){
                                        canvas.drawBitmap(itemBitmap,itemX, itemY, paint);
                                    }
                                    // 当たり判定
                                    judgeHit();
//                                    Log.v("tag", "リスト中身確認"+ i + "," + itemX + "," +itemBitmap);
                                    // 残り時間が0になったら座標の移動を止める
                                    if(time > 0){
                                        // ArrayListにセット
                                        cd.setItemBitmap(itemBitmap);
                                        cd.setItemX(itemX);
                                        cd.setItemY(itemY);
                                        cd.setHitFlag(hitFlag);
                                        item.set(i,cd);
                                    }
                                }else{
                                    item.remove(i);
                                    i--;
                                }

                            }
                            if(canvas != null){
                                canvas.drawBitmap(player, playerX, playerY, paint);
                            }
                            // 残り時間表示
                            showTime.setText(String.valueOf(time));
                            // スコア表示
                            showScore.setText(String.valueOf(score));
                            if(countDown != null){
                                if(canvas != null){
                                    canvas.drawBitmap(countDown, readyX, readyY, paint);
                                }
                            }
                            if(time <= 0){
                                finish();
                            }
                            if(fin != null){
                                if(canvas != null){
                                    canvas.drawBitmap(fin, finishX, finishY, paint);
                                }
                            }
                            holder.unlockCanvasAndPost(canvas);
                            if(finishFlag){
                                timer.cancel();
//                               soundPool.play(soundResId5,1.0f,1.0f,0,0,1.0f);
//                               mediaPlayer.stop();
//                               mediaPlayer.release();
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getContext(), ExitActivity.class);
                                        intent.putExtra("score", score);
                                        intent.putExtra("CH", CH);
                                        getContext().startActivity(intent);
                                        ((MainActivity)getContext()).finish();
                                    }
                                }, 5000);
                            }
                        }
                    });
                }
            },0,100);

    }
}
