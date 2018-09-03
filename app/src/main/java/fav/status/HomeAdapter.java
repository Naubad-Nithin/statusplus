package fav.status;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Nithin on 1/23/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private ArrayList<HomeAdapterModel> homeAdapterModel;
    private SimpleDraweeView draweeView;
    private Context context;

    public HomeAdapter(Context context,ArrayList<HomeAdapterModel> homeAdapterModel){
        this.context=context;
        this.homeAdapterModel=homeAdapterModel;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MainActivity mainActivity = (MainActivity) context;
//        RoundingParams roundingParams = RoundingParams.fromCornersRadius(50f);

        holder.textView.setText(homeAdapterModel.get(position).getName());
        Uri uri = Uri.parse(homeAdapterModel.get(position).getImg_url());
        draweeView.setImageURI(uri);
//        roundingParams.setRoundAsCircle(true);
//        draweeView.getHierarchy().setRoundingParams(roundingParams);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("put",homeAdapterModel.get(position).getQuotes());
                bundle.putStringArrayList("bgs",homeAdapterModel.get(position).getBgurls());
                FragmentManager fragmentManager = mainActivity.getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment quoteFragment = new QuoteFragment();
                transaction.add(R.id.container,quoteFragment);
                transaction.addToBackStack(null);
                quoteFragment.setArguments(bundle);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeAdapterModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.item_image);
            view = itemView;
        }
    }
}
