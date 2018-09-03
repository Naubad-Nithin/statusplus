package fav.status;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Nithin on 2/5/2018.
 */

public class SetColorAdapter extends RecyclerView.Adapter<SetColorAdapter.ViewHolder>{

    public ArrayList<String> colorsPath;
    public Context mContext;
    public SetColorDialog.Dismissed callback;
    public SetColorDialog.palatteDismissed palatteCallBack;
    private Typeface mTypeface;
    private LayoutInflater mInflater;
    protected String tag;
    public SetColorDialog dialog;

    public SetColorAdapter(@NonNull Context context, int resource, ArrayList<String> colorsPath, SetColorDialog.Dismissed callBack,String tag,SetColorDialog dialog) {
        this.colorsPath=colorsPath;
        this.mContext=context;
        this.callback=callBack;
        this.tag=tag;
        this.dialog=dialog;
    }

    public SetColorAdapter(@NonNull Context context, int resource, ArrayList<String> colorsPath, SetColorDialog.palatteDismissed palatteCallBack,String tag,SetColorDialog dialog) {
        this.colorsPath=colorsPath;
        this.mContext=context;
        this.palatteCallBack=palatteCallBack;
        this.tag=tag;
        this.dialog=dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.color_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setBackgroundColor(Color.parseColor(colorsPath.get(position)));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag=="setpalattedialog"){
                    palatteCallBack.palatteDialogDismissed(String.valueOf(colorsPath.get(position)));
                    dialog.dismiss();
                } else if(tag=="setcolordialog"){
                    callback.colorDialogDismissed(String.valueOf(colorsPath.get(position)));
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return colorsPath.size();
    }

    @Override
    public int getItemCount() {
        return colorsPath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.color_hex);
        }
    }
}
