package fav.status;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

/**
 * Created by Nithin on 2/2/2018.
 */

public class QuoteFragmentAdapter extends BaseAdapter {
    ArrayList<String> quotes;
    private Context mContext;
    ArrayList<String> bgurls;
    private InterstitialAd mInterstitialAd;
    int pos;


    public QuoteFragmentAdapter(Context context, ArrayList<String> quoteFragmentModels, InterstitialAd mInterstitialAd, ArrayList<String> bgurls) {
        this.mContext = context;
        this.quotes = quoteFragmentModels;
        this.mInterstitialAd = mInterstitialAd;
        this.bgurls = bgurls;
    }

    @Override
    public int getCount() {
        return quotes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_list_item, parent, false);
        }

        //load interstitial ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        String item = (String) getItem(position);
        pos = position;

        final TextView quotesTextView = convertView.findViewById(R.id.frag_item_text);
        ImageView editImageView = convertView.findViewById(R.id.edit);
        ImageView shareImageView = convertView.findViewById(R.id.share);

        quotesTextView.setText(item);
        editImageView.setTag(position);
        shareImageView.setTag(position);

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("quote_detail", quotes.get(pos));
                bundle.putStringArrayList("bgs",bgurls);
                FragmentManager manager = ((Activity) mContext).getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = new DetailQuoteFragment();
                transaction.add(R.id.fragment_quote, fragment);
                transaction.addToBackStack(null);
                fragment.setArguments(bundle);
                transaction.commit();

                //show interstitial ad
                //.......

            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String body = quotes.get(pos);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"WTF! - status & quotes");
                shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                mContext.startActivity(Intent.createChooser(shareIntent,"Share status"));
            }
        });

        return convertView;
    }
}
