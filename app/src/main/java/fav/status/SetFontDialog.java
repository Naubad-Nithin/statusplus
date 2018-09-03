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

public class SetFontDialog extends DialogFragment implements DialogInterface.OnDismissListener{
    private Dismissed callBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBack = (Dismissed) getTargetFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_set_font,new LinearLayout(getActivity()),false);
        String[] fontpath = getResources().getStringArray(R.array.fonts);

        final ArrayList<String> fontsPath=new ArrayList<>();
        for (int i=0; i<fontpath.length;i++) {
            fontsPath.add(fontpath[i]);
        }

        RecyclerView fontsListView = view.findViewById(R.id.font_list);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        fontsListView.setLayoutManager(layoutManager);

        SetFontDialog dialog=this;

        SetFontAdapter adapter = new SetFontAdapter(getActivity(),R.layout.font_list_item,fontsPath,callBack,dialog);
        fontsListView.setAdapter(adapter);

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
        public void dialogDismissed(String path);
    }
}
