package fav.status;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Nithin on 3/16/2018.
 */

public class CelebListAdapter extends RecyclerView.Adapter<CelebListAdapter.ViewHolder> {
    private ArrayList<TwitterModel> mTwitterModel;
    private Context mContext;

    public CelebListAdapter(Context context, ArrayList<TwitterModel> twitterModel) {
        this.mContext = context;
        this.mTwitterModel = twitterModel;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celebs_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.celebName.setText(mTwitterModel.get(position).getCelebName());
        Uri uri = Uri.parse(mTwitterModel.get(position).getCelebImage());
        holder.celebImage.setImageURI(uri);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(20f);
        roundingParams.setRoundAsCircle(true);
        holder.celebImage.getHierarchy().setRoundingParams(roundingParams);
        holder.celebImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTwitterModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView celebImage;
        private TextView celebName;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            celebImage = itemView.findViewById(R.id.celeb_image);
            celebName = itemView.findViewById(R.id.celeb_name);
            view=itemView;
        }
    }
}
