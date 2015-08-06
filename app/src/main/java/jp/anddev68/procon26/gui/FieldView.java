package jp.anddev68.procon26.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import jp.anddev68.procon26.R;
import jp.anddev68.procon26.block.Block;
import jp.anddev68.procon26.block.BlockTable;

/**
 * Created by kano on 2015/04/16.
 */
public class FieldView extends View {

    //  枠画像の設定
    int MAX_PADDING = 0;    //  ウインドウからのパディング
    int width;
    int height;
    int padding_top;    //  ウインドウからのパディング
    int padding_left;

    BlockTable blockTable;


    public FieldView(Context context) {
        super(context);
        init();
    }

    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FieldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        blockTable = new BlockTable();
    }


    /**
     * 画面情報の更新
     */
    private void initScreen(){
        /* 縦横サイズを統一する */
        width = Math.min(getWidth(), getHeight()) - MAX_PADDING * 2;
        height = width;

        /* 横向き */
        if(getWidth() > getHeight()){
            padding_top = MAX_PADDING;
            padding_left = (getWidth() - width) /2;
        }else{
            padding_top = (getHeight() - height) /2;
            padding_left = MAX_PADDING;
        }

        Log.d("","width:"+width);
        Log.d("","height:"+height);

    }


    /**
     * ブロックの追加
     * @param block
     * @param x 押された相対座標
     * @param y 押された相対座標
     * @return true:失敗 false:成功
     */
    public boolean addBlock(Block block,int x,int y){
        return blockTable.addBlock(block,x,y,padding_left,padding_top,width,height);
    }


    /**
     * スコアの取得
     */
    public String getScoreString(){
        //  埋まっているブロックの数を取得する
        int num = blockTable.getBlockNum(1);

        //  "xxx"の形に置き換える
        if(num>=100) return ""+num;
        if(num>9) return "0"+num;
        return "00"+num;

    }



    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);
        //  ブロックを描画
        blockTable.draw(c,this.padding_top,this.padding_left,this.width,this.height);

    }


/*
    @Override
    public boolean onDragEvent(DragEvent event) {
        switch(event.getAction()){
            case DragEvent.ACTION_DROP:
                Log.d("drop",event.getX()+","+event.getY());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("end",event.getX()+","+event.getY());
                break;
        }
        return true;
    }
*/


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initScreen();
        invalidate();
    }


}
