package fav.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Nithin on 3/16/2018.
 */

public class TwitterModel implements Serializable {

    @SerializedName("celeb_name")
    @Expose
    public String celebName;

    @SerializedName("celeb_image")
    @Expose
    public String celebImage;

    public String getCelebName() {
        return celebName;
    }

    public void setCelebName(String celebName) {
        this.celebName = celebName;
    }

    public String getCelebImage() {
        return celebImage;
    }

    public void setCelebImage(String celebImage) {
        this.celebImage = celebImage;
    }

}
