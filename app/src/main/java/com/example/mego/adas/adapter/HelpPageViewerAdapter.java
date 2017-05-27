package com.example.mego.adas.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mego.adas.R;

/**
 * Created by Mego on 2/27/2017.
 */

/**
 * Adapter used to show the help instructions
 */

public class HelpPageViewerAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mLayoutInflater;

   /* //list of help photos
    private int[] mResources = {
            R.drawable.help_start,
            R.drawable.car_help_zero,
            R.drawable.car_help_one,
            R.drawable.car_help_two,
            R.drawable.car_help_three,
            R.drawable.car_help_four

    };*/

    public HelpPageViewerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        int resId = 0;
        //set the help photo with current position
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        switch (position) {
            case 0:
                resId = R.drawable.help_start;
                break;
            case 1:
                resId = R.drawable.car_help_zero;
                break;
            case 2:
                resId = R.drawable.car_help_one;
                break;
            case 3:
                resId = R.drawable.car_help_two;
                break;
            case 4:
                resId = R.drawable.car_help_three;
                break;
            case 5:
                resId = R.drawable.car_help_four;
                break;
        }
        imageView.setImageResource(resId);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
