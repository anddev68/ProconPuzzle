package jp.anddev68.procon26.util;

import android.content.res.Resources;

/**
 * Created by anddev68 on 15/04/17.
 */
public class Display {



    public static int dpiToPx(Resources res,float dp){
        // density (比率)を取得する
        float density = res.getDisplayMetrics().density;
        // dp を pixel に変換する ( dp × density + 0.5f（四捨五入) )
        int px = (int) (dp * density + 0.5f);
        return px;
    }

}
