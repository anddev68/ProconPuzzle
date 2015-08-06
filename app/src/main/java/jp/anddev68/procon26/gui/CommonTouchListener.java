package jp.anddev68.procon26.gui;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 共通のタッチリスナー
 * これを使うことで異なるViewGroup間でのViewの受け渡しを可能にする
 */
public class CommonTouchListener implements View.OnTouchListener{

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("","DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("","UP");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }


        return true;
    }






}
