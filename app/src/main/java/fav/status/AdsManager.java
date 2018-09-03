package fav.status;

import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.DTBAdCallback;
import com.amazon.device.ads.DTBAdRequest;
import com.amazon.device.ads.DTBAdResponse;
import com.amazon.device.ads.DTBAdSize;
import com.amazon.device.ads.DTBAdUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Nithin on 3/12/2018.
 */

public class AdsManager extends Activity {
    private AdView mAdView;

    public void GoogleBannerAds(Context context) {
        mAdView = new AdView(context);
        mAdView.setAdUnitId(getResources().getString(R.string.debug_banner_basic));
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

//    public void AmazonAdsRegistration(Context context) {
//        AdRegistration.getInstance("65c41d85d93146f494a86407a34f1ad7", context);
//        AdRegistration.enableLogging(true);
//        AdRegistration.enableTesting(true);
//    }

    public PublisherInterstitialAd LoadAmazonInterstitialAds(final Context context) {
        AdRegistration.getInstance("65c41d85d93146f494a86407a34f1ad7", context);
        AdRegistration.enableLogging(true);
        AdRegistration.enableTesting(true);
        final PublisherInterstitialAd ainterstitialAd = new PublisherInterstitialAd(context);
        final DTBAdRequest adLoader = new DTBAdRequest();
        adLoader.setSizes(new DTBAdSize.DTBInterstitialAdSize("53d7d2b4-134f-464a-93af-2b8801ef790c"));
        adLoader.loadAd(new DTBAdCallback() {
            @Override
            public void onFailure(AdError adError) {
                Log.e("APP", "Failed to get the interstitial ad from Amazon" + adError.getMessage());
                /**Please implement the logic to send ad request without our parameters if you want to
                 show ads from other ad networks when Amazon ad request fails**/
            }

            @Override
            public void onSuccess(DTBAdResponse dtbAdResponse) {
                ainterstitialAd.setAdUnitId("/45467520/APP_NOFLOOR");
                final PublisherAdRequest adRequest = DTBAdUtil.INSTANCE.createPublisherAdRequestBuilder(dtbAdResponse).build();
                ainterstitialAd.loadAd(adRequest);
                Toast.makeText(context, "ad success", Toast.LENGTH_LONG).show();
            }
        });
        return ainterstitialAd;
    }
}
