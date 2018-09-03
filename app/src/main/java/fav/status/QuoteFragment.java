package fav.status;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

/**
 * Created by Nithin on 1/25/2018.
 */

public class QuoteFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quote, container, false);
        ListView listView = view.findViewById(R.id.frag_list);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.debug_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        final ArrayList<String> sr = getArguments().getStringArrayList("put");
        final ArrayList<String> bgurls = getArguments().getStringArrayList("bgs");

        QuoteFragmentAdapter qfAdapter = new QuoteFragmentAdapter(getActivity(),sr,mInterstitialAd,bgurls);
        listView.setAdapter(qfAdapter);
        return view;
    }
}
