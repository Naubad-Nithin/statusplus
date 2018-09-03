package fav.status;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CardImagesAdapter extends RecyclerView.Adapter<CardImagesAdapter.ViewHolder>{
    private ArrayList<ImageCardModel> mImageCardModel;
    private SimpleDraweeView draweeView;
    private Context mContext;

    public CardImagesAdapter(Context context, ArrayList<ImageCardModel> imageCardModel) {
        this.mContext = context;
        this.mImageCardModel = imageCardModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weird_facts_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.factDesc.setText(mImageCardModel.get(position).getTag());
        Uri uri = Uri.parse(mImageCardModel.get(position).getImageUrl());
        draweeView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return mImageCardModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView factDesc;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            factDesc = itemView.findViewById(R.id.fact_description);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.weird_item_image);
        }
    }
}
