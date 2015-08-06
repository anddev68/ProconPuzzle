package jp.anddev68.procon26.gui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jp.anddev68.procon26.GameActivity;
import jp.anddev68.procon26.block.Block;
import jp.anddev68.procon26.util.Display;

/**
 * Footerに追加するViewです
 *
 */
public class FooterView2 extends View {

    //  ブロックデータ
    Block block;

    GestureDetector gestureDetector;
    int oldX,oldY;


    public FooterView2(Context context) {
        super(context);
        init();
    }

    public FooterView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onDragEvent(DragEvent event){
        switch(event.getAction()){
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("FooterView/Existed",event.getX()+","+event.getY());
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d("FooterView/Location",event.getX()+","+event.getY());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                //Log.d("Drag End",event.getX()+","+event.getY());
                break;
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("","DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("","MOVE");
                /*
                int oldX = e.getRawX();
                int oldY = e.getRawY();
                float x = oldX -e.getX();
                float y = oldY -e.getY();

                */
                break;
            case MotionEvent.ACTION_UP:
                Log.d("","UP");
                break;
        }

        return gestureDetector.onTouchEvent(event);
        //return false;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(block!=null)
            block.draw(canvas,5,5,getWidth()-10,getHeight()-10);

    }



    /**
     * 初期化コード
     */
    private void init(){
        gestureDetector = new GestureDetector(getContext(),new MyGestureListener());

        this.setBackgroundColor(Color.rgb(0xF2, 0xF2, 0xF2));
    }


    /**
     * ロングタップから回転とバイブレート
     */
    public void turnRight(){
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {80,300};
        vibrator.vibrate(pattern, -1);
        block.turnRight();
        invalidate();
    }



    public void setBlock(Block block){
        this.block = block;
    }
    public Block getBlock(){ return this.block; }










    /**
     * FooterLayoutにセットするためのパラメータ
     */
    public static LinearLayout.LayoutParams createFooterLayoutParams(Resources res){
        int size = Display.dpiToPx(res, 100);
        int margin = Display.dpiToPx(res,10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
        layoutParams.setMargins(margin, 0, margin, 0);
        return layoutParams;
    }


    /**
     * RelativeLayoutにセットするためのパラメータ
     * @param x 親からの相対位置px
     * @param y
     */
    public static RelativeLayout.LayoutParams createRelativeLayoutParams(Resources res,int x,int y){
        int size = Display.dpiToPx(res, 100);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size,size);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutParams.topMargin = x;
        layoutParams.leftMargin = y;
        return layoutParams;
    }



    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("FooterView","Scroll");

            float x = e2.getRawX();
            float y = e2.getRawY();

            Log.d("",""+x+","+y);
            ((GameActivity)getContext()).startDragFooterView(FooterView2.this,(int)x,(int)y);

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("FooterView","OnLongPress");
            turnRight();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("FooterView","Fling");
            return true;
        }


    }



}
