package jp.anddev68.procon26.block;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by kano on 2015/04/17.
 */
public class BlockTable {

    private int x_num = 8;
    private int y_num = 8;

    private int[][] field;

    public BlockTable(){
        field = new int[y_num][x_num];
    }

    /**
     * 領域サイズを指定してテーブルを初期化
     * @param x
     * @param y
     */
    public BlockTable(int x,int y){
        x_num = x;
        y_num = y;
        field = new int[y_num][x_num];
    }

    /**
     * ブロックテーブル（碁盤の目を書く）
     * @param c
     * @param top
     * @param left
     * @param width
     * @param height
     */
    public void draw(Canvas c,int top,int left,int width,int height){
        Paint p0 = new Paint(); p0.setColor(Color.rgb(0xA4,0xA4,0xA4)); p0.setStyle(Paint.Style.FILL);
        Paint p1 = new Paint(); p1.setColor(Color.rgb(0x42,0x42,0x42)); p1.setStyle(Paint.Style.FILL);
        Paint p2 = new Paint(); p2.setColor(Color.BLUE); p2.setStyle(Paint.Style.FILL);

        for(int i=0; i<y_num; i++){
            int chipWidth = width/x_num;
            int chipHeight = height/y_num;

            for(int j=0; j<x_num; j++){
                Rect r = new Rect();
                r.top =  top+chipHeight*i;
                r.left = left+chipWidth*j;
                r.right = r.left + chipWidth;
                r.bottom = r.top + chipHeight;
                if( (i+j) %2 == 0 ) {
                    c.drawRect(r,p0);
                }else{
                    c.drawRect(r,p1);
                }
                //  ブロックがある場合はそのまま描画
                if( field[i][j] == 1){
                    c.drawRect(r.left+2,r.top+2,r.right-2,r.bottom-2,p2);
                }

            }
        }

    }


    /**
     * ブロックを追加する
     */
    public boolean addBlock(Block b,int x,int y,int left,int top,int width,int height){
        //  ブロックインデックスに変換
        int X = (x-left) / (width/x_num);
        int Y = (y-top) / (height/y_num);

        Log.d("BlockTable",String.format("Try To %d,%d,(%d,%d)",x,y,X,Y));
        if( !contains(X,Y) ){
            Log.d("BlockTable", "タッチ座標が不正です");
            return true;
        }

        for(int i=0; i<b.getRaw().length; i++){
            for(int j=0; j<b.getRaw()[i].length; j++){
                //  テーブルにおけるかどうかチェック
                int tX = X+j;
                int tY = Y+i;

                //  空ブロックをおいた場合は無視
                if( b.getRaw()[i][j] == 0) {
                    continue;
                }

                //  一つでもはみ出したら終了
                if( !contains(tX,tY) ){
                    Log.d("BlockTable","Block out of range.");
                    replaceTable(2,0);
                    return true;
                }


                //  置こうとした場所に既にブロックがあった場合
                if( existBlock(tX,tY)) {
                    //  失敗
                    Log.d("BlockTable","Block Exist.");
                    replaceTable(2, 0);
                    return true;
                }

                //  一時的におく
                field[tY][tX] = 2;
            }
        }

        //  おいたブロックを確定する
        Log.d("BlockTable","Drop Block.");
        replaceTable(2,1);

        return false;

    }

    /**
     * ブロック数を取得
     */
    public int getBlockNum(int flag){
        int num = 0;
        for(int i=0; i<y_num; i++){
            for(int j=0; j<x_num; j++){
                if( field[i][j] == flag){
                    num++;
                }
            }
        }
        return num;
    }


    private void replaceTable(int from,int to){
        for(int i=0; i<y_num; i++){
            for(int j=0; j<x_num; j++){
                if( field[i][j] == from){
                    field[i][j] = to;
                }
            }
        }
    }


    private boolean contains(int X,int Y){
        if( X<0 || X>=x_num || Y<0 || Y>=y_num){
            return false;
        }
        return true;
    }


    private boolean existBlock(int X,int Y){
        if(field[Y][X]==1) return true;
        return false;
    }




}
