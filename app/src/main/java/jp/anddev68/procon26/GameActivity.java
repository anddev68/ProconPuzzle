package jp.anddev68.procon26;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;

import jp.anddev68.procon26.block.Block;
import jp.anddev68.procon26.gui.CommonTouchListener;
import jp.anddev68.procon26.gui.DummyFooterView;
import jp.anddev68.procon26.gui.FieldView;
import jp.anddev68.procon26.gui.FooterView2;
import jp.anddev68.procon26.util.Display;

/**
 * Created by kano on 2015/04/16.
 */
public class GameActivity extends Activity implements View.OnClickListener,View.OnDragListener{


    LinearLayout footerLayout;      //画面下部のアイテムリスト
    FieldView fieldView;           //パズル盤面
    RelativeLayout mainLayout;      //親のレイアウト
    TextView scoreView;             //点数

    GestureDetector gestureDetector;

    FooterView2 selectedFooterView; //選択されているアイテム
    DummyFooterView dummyFooterView;    //Rootに付け直した時に下においておくダミー


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game);

        footerLayout = (LinearLayout) findViewById(R.id.linearLayout);
        fieldView = (FieldView) findViewById(R.id.fieldView);
        mainLayout = (RelativeLayout) findViewById(R.id.frameLayout);
        scoreView = (TextView) findViewById(R.id.scoreView);


        //  10個ブロックランダム生成
        for(int i=0; i<10; i++){
            FooterView2 view = new FooterView2(this);
            view.setBlock(new Block(3, 3));
            //view.setOnTouchListener(this);
            footerLayout.addView(view,FooterView2.createFooterLayoutParams(getResources()));

        }
        footerLayout.invalidate();  //  ここで更新しておけばいいのかな？

        //mainLayout.setOnDragListener(this); //  FooterViewの移動キャンセル検知
        fieldView.setOnDragListener(this);  //  FooterViewの移動検知

        gestureDetector = new GestureDetector(this,new GameActivityGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()){
            case DragEvent.ACTION_DROP:
                Log.d("","DROP "+v.getClass().getName());
                float x = event.getX() - selectedFooterView.getWidth() /2;
                float y = event.getY() - selectedFooterView.getHeight() /2;
                //Log.d("",x+","+y);
                return endDragFooterView((int)x,(int)y);    //  失敗した場合falseを返す

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("","EXISTED "+v.getClass().getName());
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d("","LOCATION "+v.getClass().getName());
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("","DRAG_END "+v.getClass().getName());
                setFooterViewToFooter();
                break;

        }

        return true;
    }



    /**
     * FooterLayoutから取り外して
     * 指定した座標を初期位置としてルートに配置する
     */
    public void setFooterViewToRoot(FooterView2 v,int x,int y){
        //Log.d("setSelectedBlock()",x+","+y);
        selectedFooterView = v;
        //selectedFooterView.setOnDragListener(this);
        dummyFooterView = new DummyFooterView(this);    //ダミーの作成
        ViewGroup parent = (ViewGroup)selectedFooterView.getParent();
        int index = parent.indexOfChild(selectedFooterView);    //元あった場所を記憶
        parent.removeView(selectedFooterView);  //選択されたものを外す
        parent.addView(dummyFooterView, index,FooterView2.createFooterLayoutParams(getResources()));    //ダミーの配置
        mainLayout.addView(selectedFooterView, FooterView2.createRelativeLayoutParams(getResources(), x, y));   //Rootに付け加える
    }


    /**
     * FooterViewを元の位置に戻す
     */
    public void setFooterViewToFooter(){
        final String tag = "/setFooterViewToFooter";
        if(selectedFooterView==null){
            Log.e(tag,"何も選択されていない");
            return;
        }
        if(dummyFooterView==null){
            Log.e(tag,"ダミーが配置されてない");
            return;
        }
        mainLayout.removeView(selectedFooterView);  //rootから外して
        int index = footerLayout.indexOfChild(dummyFooterView); //ダミーの位置を取得
        footerLayout.removeView(dummyFooterView);   //ダミーを削除して
        footerLayout.addView(selectedFooterView,index,FooterView2.createFooterLayoutParams(getResources()));    //追加する

        selectedFooterView.setOnDragListener(null);
        selectedFooterView = null;  //未選択に戻す
        dummyFooterView = null;

    }


    /**
     * FooterViewを破棄する
     */
    public void removeFooterView(){
        final String tag = "/removeFooterView";
        if(selectedFooterView==null){
            Log.e(tag,"何も選択されていない");
            return;
        }
        if(dummyFooterView==null){
            Log.e(tag,"ダミーが配置されてない");
            return;
        }
        mainLayout.removeView(selectedFooterView);  //rootから外して
        footerLayout.removeView(dummyFooterView);   //footerから外す
        selectedFooterView.setOnDragListener(null);
        selectedFooterView = null;  //未選択に戻す
        dummyFooterView = null;

    }


    /**
     * FooterViewがクリックされた時に呼ばれる
     * FooterViewから呼ばれる
     */
    public void startDragFooterView(FooterView2 v,int x,int y){
        if(selectedFooterView==null){   //  未選択なら選択する
            setFooterViewToRoot(v,x,y);
            mainLayout.startDrag(null,new View.DragShadowBuilder(selectedFooterView),(Object)selectedFooterView,0);
        }
    }



    /**
     * FrameLayout以下のFooterViewがドラッグ終了した時に呼ばれる
     * FooterViewが離された座標とブロックの形をFieldViewに渡す
     */
    public boolean endDragFooterView(int x,int y){
        if( !fieldView.addBlock( selectedFooterView.getBlock(), x, y) ){
            //  無事に追加されたらFrameLayoutのViewを破棄
            removeFooterView();
            fieldView.invalidate();
            //  スコアを取得
            scoreView.setText(fieldView.getScoreString());
            scoreView.invalidate();
            return true;    //  成功
        }else {
            //元に戻す
            setFooterViewToFooter();
        }
        return false;   //  失敗
    }


    /**
     * メニューダイアログを開く
     */
    private void openMenuDialog(){
        AlertDialogPro.Builder builder = new AlertDialogPro.Builder(this);
        String[] str = {"1手前に戻す","ゲームを中断する","ゲームを終了する"};
        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,str);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                TextView selectView = (TextView) view;
                for(int i=0; i<listView.getChildCount(); i++){
                    TextView textView = (TextView) listView.getChildAt(i);
                    if( textView.equals(selectView) ){
                        textView.setBackgroundColor(Color.rgb(0xB2,0xEB,0xF2));
                    }else{
                        //textView.setBackgroundColor(Color.rgb(0x3F,0x51,0xB5));
                        textView.setBackgroundColor(Color.rgb(0x00,0xBC,0xD4));
                    }
                }
                listView.invalidate();
            }
        });

        builder.setView(listView).
                setTitle("Pause").
                //setMessage("Choose Action...").
                setPositiveButton("決定", null).
                setNegativeButton("キャンセル", null).
                show();
    }











    private class GameActivityGestureListener implements GestureDetector.OnGestureListener {

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
            Log.d("Main","Scroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Main","OnLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("Main","Fling");
            if(e1==null) return true;
            if(e2==null) return true;
            if(Math.abs(e1.getX()-e2.getX())>20){
                openMenuDialog();
            }

            return true;
        }
    }






}
