package fav.status;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class FactsViewPagerAdapter extends PagerAdapter{
    Context mContext;
    ArrayList<ImageCardModel> mImageCardModel;

    public FactsViewPagerAdapter(Context context, ArrayList<ImageCardModel> imageCardModel) {
        this.mImageCardModel = imageCardModel;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mImageCardModel.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = (View) layoutInflater.inflate(R.layout.weird_facts_list_item,container,false);
        TextView tagText = view.findViewById(R.id.fact_description);
        tagText.setText(mImageCardModel.get(position).getTag());
        SimpleDraweeView factImage = view.findViewById(R.id.weird_item_image);
        factImage.setImageURI(mImageCardModel.get(position).getImageUrl());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
