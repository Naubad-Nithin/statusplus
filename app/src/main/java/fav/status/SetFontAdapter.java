package fav.status;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nithin on 2/5/2018.
 */

public class SetFontAdapter extends RecyclerView.Adapter<SetFontAdapter.ViewHolder> {

    public ArrayList<String> fontPaths;
    public Context mContext;
    public SetFontDialog.Dismissed callback;
    private Typeface mTypeface;
    private LayoutInflater mInflater;
    public SetFontDialog dialog;


    public SetFontAdapter(@NonNull Context context, int resource, ArrayList<String> fontsPath, SetFontDialog.Dismissed callback, SetFontDialog dialog) {
        this.fontPaths=fontsPath;
        this.mContext=context;
        this.callback=callback;
        this.dialog=dialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.font_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(fontPaths.get(position).split("fonts/")[1].split(".ttf")[0]);
        holder.textView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPaths.get(position)));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.dialogDismissed(String.valueOf(fontPaths.get(position)));
                dialog.dismiss();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return fontPaths.size();
    }

    @Override
    public int getItemCount() {
        return fontPaths.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView= view.findViewById(R.id.font_name);
        }
    }
}
