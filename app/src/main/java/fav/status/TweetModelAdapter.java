package fav.status;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nithin on 3/16/2018.
 */

public class TweetModelAdapter extends RecyclerView.Adapter<TweetModelAdapter.ViewHolder> {
    private ArrayList<TweetModel> tweetModel;
    private Context mContext;

    public TweetModelAdapter(Context context,ArrayList<TweetModel> tweetModel) {
        this.tweetModel = tweetModel;
        this.mContext = context;
    }


    @Override
    public TweetModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.tweet_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetModelAdapter.ViewHolder holder, final int position) {
        holder.twitterName.setText("@"+tweetModel.get(position).getTwitter_name());
        holder.tweet.setText(tweetModel.get(position).getTweet());
        holder.timestamp.setText(tweetModel.get(position).getTimestamp());
        holder.shareTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Status+ - quotes & viral images");
                shareIntent.putExtra(Intent.EXTRA_TEXT, tweetModel.get(position).getTweet());
                shareIntent.putExtra(Intent.EXTRA_ASSIST_CONTEXT, tweetModel.get(position).getTwitter_name());
                shareIntent.putExtra("",true);
                shareIntent.putExtra(Intent.EXTRA_REFERRER, "https://play.google.com/store/apps/details?id=fav.quotes");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                mContext.startActivity(Intent.createChooser(shareIntent, "Set Your Quote"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweetModel.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView twitterName;
        private TextView tweet;
        private TextView timestamp;
        private ImageView shareTweet;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            twitterName = itemView.findViewById(R.id.twitter_name);
            tweet = itemView.findViewById(R.id.tweet);
            timestamp = itemView.findViewById(R.id.timestamp);
            shareTweet = itemView.findViewById(R.id.share_tweet);
            view = itemView;
        }
    }
}
