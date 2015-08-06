package jp.anddev68.procon26.block;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by kano on 2015/04/16.
 */
public class Block {

    private int[][] block;
    private int x_num = 4;
    private int y_num = 4;

    public Block(){
        block = new int[y_num][x_num];
    }

    public Block(Block b){
        for(int i=0; i<b.block.length; i++){
            for(int j=0; j<b.block[i].length; j++){
                this.block[i][j] = b.block[i][j];
            }
        }

    }

    public Block(String fileName){
        FileReader fileReader;
        BufferedReader bufferedReader;
        String line;
        int ySize = -1;
        int xSize = -1;
        int yCnt = 0;

        try{
            fileReader = new FileReader(new File(fileName));
            bufferedReader = new BufferedReader(fileReader);
            while((line=bufferedReader.readLine())!=null){
                if(line.isEmpty()) continue;

                if(line.charAt(0)=='#'){
                    //  コマンド行
                    continue;
                }
                if(line.charAt(0)=='\''){
                    //  コメント行
                    continue;
                }
                //  行をパース
                String[] params = line.split(",");
                if(ySize==-1 || xSize==1){  //  初回のコンマはx,y個数の指定
                    if( params.length!=2){
                        Log.e("", "BlockFile format error.");
                        Log.e("", "NotFound x,y count.");
                        block = null;
                        break;
                    }
                    xSize = Integer.parseInt(params[0]);
                    ySize = Integer.parseInt(params[1]);
                    block = new int[ySize][xSize];
                    continue;
                }
                //  列の数チェック
                if(xSize!=params.length){
                    Log.e("", "BlockFile format error.");
                    Log.e("", "Size X error.");
                    block = null;
                    break;
                }
                //  行の数があっているかをチェック
                if(ySize<=yCnt){
                    Log.e("", "BlockFile format error.");
                    Log.e("", "Size Y error.");
                    block = null;
                    break;
                }

                for(int i=0; i<params.length; i++){
                    block[yCnt][i] = Integer.parseInt(params[i]);
                }
                yCnt++;


            }
            Log.d("","Block File Read OK.");
            bufferedReader.close();
        }catch(Exception e){
            e.printStackTrace();
        }



    }

    /**
     * サイズを指定してブロックをランダム生成
     * @param x
     * @param y
     */
    public Block(int x,int y){
        x_num = x;
        y_num = y;

        block = new int[y_num][x_num];

        for(int i=0; i<block.length; i++){
            for(int j=0; j<block[i].length; j++){
                if( Math.random() < 0.5 ){
                    block[i][j] = 1;
                }

            }
        }


    }


    public void turnRight(){
        //  0からnまですべて同じ大きさで家庭している
        int[][] tmp = new int[x_num][y_num];
        for (int i = 0; i < x_num; i++) {
            for (int j = 0; j < y_num; j++) {
                tmp[i][j] = block[y_num-j-1][i];
            }
        }
        block = tmp;
    }

    public void draw(Canvas c,int top,int left,int width,int height){
        Paint p0 = new Paint(); p0.setColor(Color.rgb(0xA4, 0xA4, 0xA4)); p0.setStyle(Paint.Style.FILL);
        Paint p1 = new Paint(); p1.setColor(Color.rgb(0x42,0x42,0x42)); p1.setStyle(Paint.Style.FILL);

        for(int i=0; i<y_num; i++){
            int chipWidth = width/x_num;
            int chipHeight = height/y_num;

            for(int j=0; j<x_num; j++){
                Rect r = new Rect();
                r.top =  top+chipHeight*i -1;
                r.left = left+chipWidth*j -1;
                r.right = r.left + chipWidth -1;
                r.bottom = r.top + chipHeight -1;
                if(block[i][j]==1){
                    c.drawRect(r,p0);
                }

            }
        }

    }


    public int[][] getRaw(){ return block; }

}
