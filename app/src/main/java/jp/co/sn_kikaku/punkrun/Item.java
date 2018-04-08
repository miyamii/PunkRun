package jp.co.sn_kikaku.punkrun;

import android.graphics.Bitmap;

/**
 * アイテム座標・表示画像管理用クラス
 */

public class Item {
    private float itemX;
    private float itemY;
    private Bitmap itemBitmap;
    private String itemName;
    private boolean hitFlag;

    public float getItemX(){
        return itemX;
    }
    public void setItemX(float itemX){
        this.itemX = itemX;
    }

    public float getItemY(){
        return itemY;
    }
    public void setItemY(float itemY){
        this.itemY = itemY;
    }

    public Bitmap getItemBitmap(){
        return itemBitmap;
    }
    public void setItemBitmap(Bitmap itemBitmap){
        this.itemBitmap = itemBitmap;
    }

    public String getItemName(){
        return itemName;
    }
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public boolean getHitFlag(){
        return hitFlag;
    }
    public void setHitFlag(boolean hitFlag){
        this.hitFlag = hitFlag;
    }
}
