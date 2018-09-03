package fav.status;

import android.app.ActionBar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String[] cat;
    int date, month, year;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd, factPagerInterstitial;
    Calendar calendar = Calendar.getInstance();
    int caldate = calendar.get(calendar.DAY_OF_MONTH);
    int calmonth = calendar.get(calendar.MONTH);
    int calyear = calendar.get(calendar.YEAR);
    private ArrayList<HomeAdapterModel> homeAdapterModel;
    public PublisherInterstitialAd AmsInterstitialAd;
    public List<twitter4j.Status> statuses;
    public ArrayList<String> jsonList = new ArrayList<>();
    private ArrayList<String> queryList = new ArrayList<>();
    private SharedPreferences savedQueryPreferences;
    private static final String MyPreferences = "myPreferences";
    private String csvQuery;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public List<String> myList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    MainActivity.this.startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        FirebaseApp.initializeApp(getApplicationContext());

        //        actionBar.hide();
        String status_path = Environment.getExternalStorageDirectory().toString();
        File directory = new File(status_path);
        try {
            File[] files = directory.listFiles();
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
            }
        } catch (Exception e) {
        }

        MobileAds.initialize(this, String.valueOf(R.string.prod_app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(String.valueOf(R.string.detail_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        factPagerInterstitial = new InterstitialAd(this);
        factPagerInterstitial.setAdUnitId(String.valueOf(R.string.factpager_interstitial));
        factPagerInterstitial.loadAd(new AdRequest.Builder().build());

        Toast.makeText(MainActivity.this, Environment.getExternalStorageDirectory().toString(), Toast.LENGTH_LONG).show();

        File rootDir = new File(MainActivity.this.getFilesDir().getPath(), "quotes");
        File file = new File(rootDir + "/quotes.json");

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences mySavedQuery = MainActivity.this.getSharedPreferences(MyPreferences, MODE_PRIVATE);
        final String csvQueryHome = mySavedQuery.getString("savedQuery", "");
        csvQuery = csvQueryHome;
        new MainActivity.TwitterApi().execute(csvQueryHome.split(","));

        SharedPreferences myPref = MainActivity.this.getSharedPreferences("pref", MODE_PRIVATE);
        date = myPref.getInt("date", 01);
        month = myPref.getInt("month", 01);
        year = myPref.getInt("month", 1990);

        //get tweets
//        new TwitterApi().execute("luos_evitagen","RGVzoomin");

        //share your quote
        Typeface customFont = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/OpenSans-Regular.ttf");
        final EditText yourQuote = this.findViewById(R.id.your_quote);
        yourQuote.setTypeface(customFont);
        ImageView yqEdit = this.findViewById(R.id.yq_edit);
        ImageView yqShare = this.findViewById(R.id.yq_share);


        yourQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("Oppon", "onCreate: " + token);
                yourQuote.setFocusable(true);
            }
        });

        yourQuote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    yourQuote.setTextColor(getResources().getColor(R.color.bodyText));
            }
        });

        yqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yourQuote.getText().toString().isEmpty()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/*");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WTF! - status & quotes");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, yourQuote.getText().toString());
                    shareIntent.putExtra(Intent.EXTRA_REFERRER, "https://play.google.com/store/apps/details?id=fav.quotes");
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(Intent.createChooser(shareIntent, "Set Your Quote"));
                } else {
                    Toast.makeText(MainActivity.this, "Enter your quote", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //edit your quote
        final ImageView editYourQuote = findViewById(R.id.yq_edit);
        editYourQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!yourQuote.getText().toString().isEmpty()) {
                    ArrayList<String> bgurls = new ArrayList<String>();
                    Bundle bundle = new Bundle();
                    homeAdapterModel = getContentModel(MainActivity.this);
                    for (int i = 0; i < getContentModel(MainActivity.this).size(); i++) {
                        for (int j = 0; j < homeAdapterModel.get(i).getBgurls().size(); j++) {
                            bgurls.add(homeAdapterModel.get(i).getBgurls().get(j));
                        }
                    }
                    yourQuote.setEnabled(false);
                    bundle.putString("quote_detail", yourQuote.getText().toString());
                    bundle.putStringArrayList("bgs", bgurls);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailQuoteFragment detailQuoteFragment = new DetailQuoteFragment();
                    fragmentTransaction.add(R.id.container, detailQuoteFragment);
                    fragmentTransaction.addToBackStack(null);
                    detailQuoteFragment.setArguments(bundle);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(MainActivity.this, "Enter your quote", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //speak your status
        ImageView mic = this.findViewById(R.id.yq_record);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        //refresh tweets
        ImageView refreshTweets = this.findViewById(R.id.refresh_tweets);
        refreshTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MainActivity.TwitterApi().execute(csvQueryHome.split(","));
            }
        });

        //offline or online check and load data
        if (file.exists() && (caldate == date && calmonth == month && calyear == year)) {
            Context context = MainActivity.this;
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_list);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            recyclerView.setLayoutManager(layoutManager);

            ArrayList<HomeAdapterModel> homeAdapterModel = getContentModel(context);
            HomeAdapter homeAdapter = new HomeAdapter(MainActivity.this, homeAdapterModel);
            recyclerView.setAdapter(homeAdapter);
        } else if ((!file.exists() || (caldate != date || calmonth != month || calyear != year)) ||
                (file.exists() && (caldate != date || calmonth != month || calyear != year))) {
            new async().execute();
        }

        //quote of the day
//        TextView textView = findViewById(R.id.daily_quote);
//        AsyncDownloader as = new AsyncDownloader(textView);
//        as.execute();
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EditText yourQuote = this.findViewById(R.id.your_quote);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    yourQuote.setEnabled(true);
                    yourQuote.setActivated(true);
                    yourQuote.setTextColor(getResources().getColor(R.color.bodyText));
                    yourQuote.setText(result.get(0));
                }
                break;
            }

        }
    }

    //text collection content model
    public ArrayList getContentModel(final Context context) {
        ArrayList<HomeAdapterModel> homeAdapterModels = null;
        String filePath = MainActivity.this.getFilesDir().getPath() + "/quotes/quotes.json";
        try {
            Reader reader = new FileReader(new File(filePath));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<HomeAdapterModel>>() {
            }.getType();
            homeAdapterModels = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return homeAdapterModels;
        }
    }

    //celeb collection content model
    public ArrayList getCelebContentModel(final Context context) {
        ArrayList<TwitterModel> twitterModel = null;
        String filePath = MainActivity.this.getFilesDir().getPath() + "/quotes/celebs.json";
        try {
            Reader reader = new FileReader(new File(filePath));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<TwitterModel>>() {
            }.getType();
            twitterModel = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return twitterModel;
        }
    }

    //Card Images content model
    public ArrayList getCardImagesContentModel(final Context context) {
        ArrayList<ImageCardModel> imageCardModel = null;
        String filePath = MainActivity.this.getFilesDir().getPath() + "/quotes/cardImages.json";
        try {
            Reader reader = new FileReader(new File(filePath));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ImageCardModel>>() {
            }.getType();
            imageCardModel = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return imageCardModel;
        }
    }

    //tweets content model
    public ArrayList getTweetContentModel(final Context context) {
        ArrayList<TweetModel> tweetModel = null;
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<TweetModel>>() {
            }.getType();
            tweetModel = gson.fromJson(String.valueOf(jsonList), type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tweetModel;
        }
    }

    //download text collections
    public class async extends AsyncTask<String, String, String> {
        File rootDir = new File(MainActivity.this.getFilesDir().getPath(), "quotes");
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        File quotesFile = new File(rootDir + "/quotes.json");
        File celebsFile = new File(rootDir + "/celebs.json");
        File cardImagesFile = new File(rootDir + "/cardImages.json");

        @Override
        protected String doInBackground(String... params) {

            if (!quotesFile.exists() || caldate > date || calmonth > month || calyear > year) {
                try {
                    String quotesURL = "https://dl.dropboxusercontent.com/s/v6r6lhjlau1ytpi/quotes.json";
                    URL url = new URL(quotesURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    FileOutputStream outputStream = new FileOutputStream(new File(rootDir, "quotes.json"));
                    InputStream inputStream = connection.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;

                    while ((len1 = inputStream.read(buffer)) > 0) {
                        total += len1; //total = total + len1
                        outputStream.write(buffer, 0, len1);
                    }
                    setLastUpdatedDate(date, month, year);
                    outputStream.close();
                } catch (Exception e) {
                    Log.d("async", e.getMessage());
                }

                //download celeb list
                try {
                    String quotesURL = "https://dl.dropboxusercontent.com/s/qpd3v2wudvkr16g/celebs.json";
                    URL url = new URL(quotesURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    FileOutputStream outputStream = new FileOutputStream(new File(rootDir, "celebs.json"));
                    InputStream inputStream = connection.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;

                    while ((len1 = inputStream.read(buffer)) > 0) {
                        total += len1; //total = total + len1
                        outputStream.write(buffer, 0, len1);
                    }
                    setLastUpdatedDate(date, month, year);
                    outputStream.close();
                } catch (Exception e) {
                    Log.d("async", e.getMessage());
                }

                //download card images
                try {
                    String quotesURL = "https://dl.dropboxusercontent.com/s/cj5qc9vhk1b1quc/cardImages.json";
                    URL url = new URL(quotesURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    FileOutputStream outputStream = new FileOutputStream(new File(rootDir, "cardImages.json"));
                    InputStream inputStream = connection.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    long total = 0;

                    while ((len1 = inputStream.read(buffer)) > 0) {
                        total += len1; //total = total + len1
                        outputStream.write(buffer, 0, len1);
                    }
                    setLastUpdatedDate(date, month, year);
                    outputStream.close();
                } catch (Exception e) {
                    Log.d("async", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!NetworkUtils.isNetworkConnected(getApplicationContext()) && !quotesFile.exists()) {
                this.dialog.dismiss();
                Button openSettings = findViewById(R.id.open_settings);
                openSettings.setVisibility(View.VISIBLE);
                openSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (quotesFile.exists()) {
                Context context = MainActivity.this;
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), 0, false);
                recyclerView.setLayoutManager(layoutManager);

                ArrayList<HomeAdapterModel> homeAdapterModel = getContentModel(context);
                HomeAdapter homeAdapter = new HomeAdapter(MainActivity.this, homeAdapterModel);
                recyclerView.setAdapter(homeAdapter);
            }

            if (celebsFile.exists()) {
                Context context = MainActivity.this;
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.celebs_recycle);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), 0, false);
                recyclerView.setLayoutManager(layoutManager);

                ArrayList<TwitterModel> twitterModel = getCelebContentModel(context);
                CelebListAdapter celebListAdapter = new CelebListAdapter(MainActivity.this, twitterModel);
                recyclerView.setAdapter(celebListAdapter);
            }

            if (cardImagesFile.exists()) {
                Context context = MainActivity.this;
                ArrayList<ImageCardModel> imageCardModel = getCardImagesContentModel(context);
                ViewPager viewPager = findViewById(R.id.facts_pager);
                viewPager.setAdapter(new FactsViewPagerAdapter(MainActivity.this, imageCardModel));
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position % 5 == 0) {
                           if (mInterstitialAd.isLoaded()){
                               mInterstitialAd.show();
                           }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
            this.dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("loading...");
            this.dialog.show();
            if (!rootDir.exists()) {
                rootDir.mkdirs();
            }
        }
    }

    //get tweets
    public class TwitterApi extends AsyncTask<String, Integer, ArrayList<String>> {
        ConfigurationBuilder cb = new ConfigurationBuilder().setDebugEnabled(true)
                .setOAuthConsumerKey("tsCAHJI6LT0NC7676YVcH1ML7")
                .setOAuthConsumerSecret("L10VeIgvGa5gkRHPpkfuJHexJZtE20UePdSC0KqFiN5RchDXnj")
                .setOAuthAccessToken("93873323-ARIR7FZ7pTcRiSq4liQTp2p7W9KWjyBItgJ2JPnWp")
                .setOAuthAccessTokenSecret("XAgz15CGQ1oumJ3dTynvucuLT0XlqQTryuCou0N0DdTPh");

        @Override
        protected ArrayList<String> doInBackground(String... celebs) {
            TwitterFactory twitterFactory = new TwitterFactory(cb.build());
            Twitter twitter = twitterFactory.getInstance();
            HashMap<String, String> tweetDict = new HashMap<String, String>();
            jsonList.clear();

            try {
                for (String user : celebs) {
                    if (StringUtils.isNotBlank(user)) {
                        statuses = twitter.getUserTimeline(user);
                        for (int i = 0; i < 1; i++) {
                            tweetDict.put("\"twitter_name\"", '"' + user + '"');
                            tweetDict.put("\"tweet\"", '"' + statuses.get(i).getText().replace('"', '\'') + '"');
                            tweetDict.put("\"timestamp\"", '"' + statuses.get(i).getCreatedAt().toString().split(" ")[1] + " " + statuses.get(i).getCreatedAt().toString().split(" ")[2] + '"');

                            String s = tweetDict.toString();
                            Set<Map.Entry<String, String>> entries = tweetDict.entrySet();
                            Comparator<Map.Entry<String, String>> valueComparator = new Comparator<Map.Entry<String, String>>() {

                                @Override
                                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                                    String v1 = o1.getValue();
                                    String v2 = o2.getValue();
                                    return v1.compareTo(v2);
                                }
                            };

                            jsonList.add(s);
                            tweetDict.clear();
                        }
                    }
                }
            } catch (TwitterException te) {
                te.printStackTrace();
            }
            return jsonList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<String> jsonlist) {
            super.onPostExecute(jsonlist);
            Log.d("tweee", jsonlist.toString());
            Context context = MainActivity.this;
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tweets_recycle);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), 0, false);
            recyclerView.setLayoutManager(layoutManager);

            if (jsonlist != null) {
                recyclerView.setVisibility(View.VISIBLE);
                ArrayList<TweetModel> tweetModel = getTweetContentModel(context);
                TweetModelAdapter tweetModelAdapter = new TweetModelAdapter(MainActivity.this, tweetModel);
                recyclerView.setAdapter(tweetModelAdapter);
            } else if (jsonlist == null) {
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "paused the activity");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop", "stop the activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "destroy the activity");
    }


    //check network connection
    public abstract static class NetworkUtils {
        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    //store date stamp in shared preferences
    public void setLastUpdatedDate(int Date, int Month, int Year) {
        SharedPreferences preferences = MainActivity.this.getSharedPreferences("dataRefresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.d("preffff..", String.valueOf(Date) + String.valueOf(Month) + String.valueOf(Year));
        editor.putInt("date", Date);
        editor.putInt("month", Month);
        editor.putInt("year", Year);
        editor.commit();
    }

    //store query list in shared preferences
    public void setCelebSelection(String savedQueryList) {
        SharedPreferences preferencesCeleb = MainActivity.this.getSharedPreferences(MyPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesCeleb.edit();
        editor.putString("savedQuery", savedQueryList);
        editor.commit();
    }

    //Celeblist adapter
    public class CelebListAdapter extends RecyclerView.Adapter<CelebListAdapter.ViewHolder> {
        private ArrayList<TwitterModel> mTwitterModel;
        private Context mContext;

        public CelebListAdapter(Context context, ArrayList<TwitterModel> twitterModel) {
            this.mContext = context;
            this.mTwitterModel = twitterModel;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celebs_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            SharedPreferences celebPref = MainActivity.this.getSharedPreferences("celebPref", MODE_PRIVATE);
            myList = new ArrayList<String>(Arrays.asList(csvQuery.split(",")));
            Log.d("myList", myList.toString());
            if (myList.contains(mTwitterModel.get(position).celebName)) {
                holder.tick.setVisibility(View.VISIBLE);
            } else if (!myList.contains(mTwitterModel.get(position).celebName)) {
                holder.tick.setVisibility(View.INVISIBLE);
            }

            holder.celebName.setText(mTwitterModel.get(position).getCelebName());
            Uri uri = Uri.parse(mTwitterModel.get(position).getCelebImage());
            holder.celebImage.setImageURI(uri);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(20f);
            roundingParams.setRoundAsCircle(true);
            holder.celebImage.getHierarchy().setRoundingParams(roundingParams);
            holder.celebImage.setOnClickListener(new View.OnClickListener() {
                //                 && holder.tick.getVisibility() == View.VISIBLE
                @Override
                public void onClick(View v) {
                    if (myList.contains(mTwitterModel.get(position).getCelebName())) {
                        myList.remove(mTwitterModel.get(position).getCelebName());
                        holder.tick.setVisibility(View.INVISIBLE);
                        csvQuery = StringUtils.join(myList, ",");
                        myList.clear();
                        myList = new ArrayList<String>(Arrays.asList(csvQuery.split(",")));
                        setCelebSelection(csvQuery);
                    } else if (!myList.contains(mTwitterModel.get(position).getCelebName())) {
                        myList.add(mTwitterModel.get(position).getCelebName());
                        holder.tick.setVisibility(View.VISIBLE);
                        csvQuery = StringUtils.join(myList, ",");
                        myList.clear();
                        myList = new ArrayList<String>(Arrays.asList(csvQuery.split(",")));
                        setCelebSelection(csvQuery);
                    }
                    Log.d("csvQ", csvQuery);
                    new MainActivity.TwitterApi().execute(csvQuery.split(","));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTwitterModel.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private SimpleDraweeView celebImage;
            private ImageView tick;
            private TextView celebName;
            private View view;

            public ViewHolder(View itemView) {
                super(itemView);
                celebImage = itemView.findViewById(R.id.celeb_image);
                celebName = itemView.findViewById(R.id.celeb_name);
                tick = itemView.findViewById(R.id.tick);
                view = itemView;
            }
        }
    }
}
