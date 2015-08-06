package jp.anddev68.procon26;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by anddev68 on 15/04/19.
 */
public class GuideActivity extends Activity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
    }

    private int[] images = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4,
            R.drawable.slide5,
            R.drawable.slide6,
            R.drawable.slide7,
    };

    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(GuideActivity.this).inflate(R.layout.viewpager_item_1,null);
            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ""+position;
        }
    }


}
