package fav.status;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Nithin on 2/3/2018.
 */

public class DetailQuoteImageAdapter extends RecyclerView.Adapter<DetailQuoteImageAdapter.ViewHolder> {
    private ArrayList<String> bgurls;
    private SimpleDraweeView draweeView, frag_drawee_view;
    private ImageView palatte;
    private Context mContext;

    public DetailQuoteImageAdapter(Context context, ArrayList<String> bgurls, SimpleDraweeView frag_drawee_view, ImageView palatte) {
        this.mContext = context;
        this.bgurls = bgurls;
        this.frag_drawee_view = frag_drawee_view;
        this.palatte=palatte;
    }


    @Override
    public DetailQuoteImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bg_selector_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(20f);
        Uri uri = Uri.parse(bgurls.get(position));
        draweeView.setImageURI(uri);
        draweeView.setTag(position);
//        roundingParams.setRoundAsCircle(true);
        draweeView.getHierarchy().setRoundingParams(roundingParams);
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palatte.setVisibility(View.GONE);
                int pos = (int) v.getTag();
                Uri frag_uri = Uri.parse(bgurls.get(pos));
                frag_drawee_view.setImageURI(frag_uri);
                frag_drawee_view.setBackgroundColor(Color.YELLOW);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bgurls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView imageView;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            draweeView = itemView.findViewById(R.id.bg_item);
            view = itemView;
        }
    }
}
