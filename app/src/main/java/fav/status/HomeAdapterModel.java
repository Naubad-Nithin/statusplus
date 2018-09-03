package fav.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nithin on 1/25/2018.
 */

public class HomeAdapterModel implements Serializable{

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("img_url")
    @Expose
    public String img_url;

    @SerializedName("quotes")
    @Expose
    public ArrayList<String> quotes;

    @SerializedName("bgurl")
    @Expose
    public ArrayList<String> bgurls;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public ArrayList<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<String> quotes) {
        this.quotes = quotes;
    }

    public ArrayList<String> getBgurls() {
        return bgurls;
    }

    public void setBgurls(ArrayList<String> bgurls) {
        this.bgurls = bgurls;
    }

}
