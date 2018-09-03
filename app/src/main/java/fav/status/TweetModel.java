package fav.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nithin on 3/16/2018.
 */

public class TweetModel implements Serializable {
    public String getTwitter_name() {
        return twitter_name;
    }

    public void setTwitter_name(String twitter_name) {
        this.twitter_name = twitter_name;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
    @SerializedName("twitter_name")
    @Expose
    public String twitter_name;

    @SerializedName("tweet")
    @Expose
    public String tweet;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
}
