package fav.status;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nithin on 1/31/2018.
 */

public class DetailQuoteFragment extends Fragment implements SetFontDialog.Dismissed, SetColorDialog.Dismissed, SetColorDialog.palatteDismissed {

    protected MenuItem Item;
    protected File ImageFile;
    protected TextView mTextView;
    protected SimpleDraweeView mSimpleDrawee;
    private InterstitialAd mInterstitialAd;
    RelativeLayout relativeLayout;
    SetFontDialog dialog = new SetFontDialog();
    int x, y;
    boolean setAd=false;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_quote, container, false);
        String quote = getArguments().getString("quote_detail");
        final TextView textView = view.findViewById(R.id.detail_quote);
        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Bold.ttf");
        textView.setText(quote);
        textView.setTypeface(customFont);
        mTextView = textView;

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.detail_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //text reposition on long click
        //..........................

        setHasOptionsMenu(true);

        //set Adapter
        ArrayList<String> bgurls = getArguments().getStringArrayList("bgs");

        SimpleDraweeView simpleDraweeView = view.findViewById(R.id.selected_bg);
        mSimpleDrawee = simpleDraweeView;
        ImageView palatte = view.findViewById(R.id.palatte);

        DetailQuoteImageAdapter adapter = new DetailQuoteImageAdapter(getActivity(), bgurls, simpleDraweeView, palatte);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bg_selection);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), 0, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //set font
        final TextView font = view.findViewById(R.id.font);
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font.setTextColor(getResources().getColor(R.color.colorPrimary));
                font.setBackgroundColor(getResources().getColor(R.color.white));
                FragmentManager fm = getFragmentManager();
                SetFontDialog setFontDialog = new SetFontDialog();
                setFontDialog.setTargetFragment(DetailQuoteFragment.this, 0);
                setFontDialog.show(fm, "setfontdialog");
            }
        });

        //set color
        TextView color = view.findViewById(R.id.color);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                SetColorDialog setColorDialog = new SetColorDialog();
                setColorDialog.setTargetFragment(DetailQuoteFragment.this, 0);
                setColorDialog.show(fm, "setcolordialog");
            }
        });

        //set plain color
        palatte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                SetColorDialog setColorDialog = new SetColorDialog();
                setColorDialog.setTargetFragment(DetailQuoteFragment.this, 0);
                setColorDialog.show(fm, "setpalattedialog");
            }
        });

        //justify
        final ImageView leftJustify = view.findViewById(R.id.left_just);
        final ImageView rightJustify = view.findViewById(R.id.right_just);
        final ImageView centerJustify = view.findViewById(R.id.center_just);
        final ImageView cameraIcon = view.findViewById(R.id.camera_icon);

        leftJustify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setGravity(Gravity.LEFT);
                leftJustify.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                rightJustify.setBackgroundColor(Color.TRANSPARENT);
                centerJustify.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        rightJustify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setGravity(Gravity.RIGHT);
                rightJustify.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                leftJustify.setBackgroundColor(Color.TRANSPARENT);
                centerJustify.setBackgroundColor(Color.TRANSPARENT);

            }
        });
        centerJustify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setGravity(Gravity.CENTER);
                centerJustify.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                rightJustify.setBackgroundColor(Color.TRANSPARENT);
                leftJustify.setBackgroundColor(Color.TRANSPARENT);

            }
        });

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null){
            Uri selectImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectImage,filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mSimpleDrawee.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Item = item;
        if (id == R.id.share_menu) {
            View v = getActivity().findViewById(R.id.body);
            if (isStoragePermissionGranted()) {
                share();
            }
            setAd=true;
        }else if(id == R.id.save_menu){
            saveImage();
            Toast.makeText(getActivity(), "Quote saved", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public void saveImage() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Long ts = System.currentTimeMillis() / 1000;
        String timeStamp = ts.toString();
        View v = getActivity().findViewById(R.id.body);
        //save image
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/WTF! Status");
        myDir.mkdirs();
        String fname = "wtf_" + Integer.toString(year) + Integer.toString(month) + Integer.toString(date) + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        ImageFile = file;
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes.toByteArray());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            v.setDrawingCacheEnabled(false);
        }
    }

    public void share(){
        View v = getView().findViewById(R.id.body);
        Bitmap bitmap = getBitmapFromView(v);
        try{
            File file = new File(getActivity().getExternalCacheDir(),"sharedpic.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            file.setReadable(true);

            List<Intent> shareIntentLists = new ArrayList<Intent>();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            PackageManager packageManager = getActivity().getPackageManager();
            List<ResolveInfo> resInfos = packageManager.queryIntentActivities(shareIntent,0);
            int fb_check=0;
            int insta_check=0;
            if(!resInfos.isEmpty()){
                for(ResolveInfo resInfo : resInfos){
                    String packageName = resInfo.activityInfo.packageName;
                    if(packageName.toLowerCase().contains("twitter") || (insta_check==0 && packageName.toLowerCase().contains("instagram")) || packageName.toLowerCase().contains("whatsapp")||(fb_check==0 && packageName.toLowerCase().contains("facebook"))){
                        if(packageName.toLowerCase().contains("facebook")) fb_check++;
                        if(packageName.toLowerCase().contains("instagram")) insta_check++;
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(new ComponentName(packageName,resInfo.activityInfo.name));
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                        intent.setPackage(packageName);
                        shareIntentLists.add(intent);
                    }
                }
                if(!shareIntentLists.isEmpty()){
                    Intent chooser = Intent.createChooser(shareIntentLists.get(0),"apps");
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,shareIntentLists.toArray(new Parcelable[]{}));
                    startActivity(chooser);
                }else{
                    Log.e("Error","No sharing apps");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dialogDismissed(String fontPath) {
        String path = fontPath;
        mTextView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), path));
        TextView font = getActivity().findViewById(R.id.font);
        font.setTextColor(Color.WHITE);
        font.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

    }

    @Override
    public void colorDialogDismissed(String color) {
        mTextView.setTextColor(Color.parseColor(color));
        TextView colorView = getActivity().findViewById(R.id.color);
        colorView.setTextColor(Color.WHITE);
        colorView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void palatteDialogDismissed(String color) {
        mSimpleDrawee.setBackgroundColor(Color.parseColor(color));
    }

    public Bitmap getBitmapFromView(View view){
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(setAd==true){
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
        setAd=false;
    }

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    @Override
    public void onPause() {
        super.onPause();
        EditText yourQuote = getActivity().findViewById(R.id.your_quote);
        yourQuote.setEnabled(true);
    }
}