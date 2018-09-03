package fav.status;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Nithin on 2/5/2018.
 */

public class SetColorDialog extends DialogFragment implements DialogInterface.OnDismissListener{
    private Dismissed callBack;
    private palatteDismissed palatte_callBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBack = (Dismissed) getTargetFragment();
        palatte_callBack = (palatteDismissed) getTargetFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_set_color,new LinearLayout(getActivity()),false);
        String[] color = getResources().getStringArray(R.array.colors);

        final ArrayList<String> colorsPath=new ArrayList<>();
        for (int i=0; i<color.length;i++) {
            colorsPath.add(color[i]);
        }

        RecyclerView colorsListView = view.findViewById(R.id.color_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),4);
        colorsListView.setLayoutManager(layoutManager);

        SetColorDialog dialog=this;

        if(getTag()=="setcolordialog"){
            SetColorAdapter adapter = new SetColorAdapter(getActivity(),R.layout.color_list_item,colorsPath,callBack,getTag(),dialog);
            colorsListView.setAdapter(adapter);
        } else if(getTag()=="setpalattedialog"){
            SetColorAdapter adapter = new SetColorAdapter(getActivity(),R.layout.color_list_item,colorsPath,palatte_callBack,getTag(),dialog);
            colorsListView.setAdapter(adapter);
        }

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        return builder;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public interface Dismissed {
        public void colorDialogDismissed(String path);
    }

    public interface palatteDismissed {
        public void palatteDialogDismissed(String color);
    }
}
