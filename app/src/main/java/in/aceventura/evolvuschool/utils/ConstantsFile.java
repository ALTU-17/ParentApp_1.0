package in.aceventura.evolvuschool.utils;

import android.app.Activity;
import android.view.View;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class ConstantsFile {
    public static int flagVersion = 0;
    // for loding Acadamic Year
    public static String flagN = "";


    public static void showGuideView(Activity activity, View view, String title, String subTitle){
        new GuideView.Builder(activity)
                .setTitle(title)
                .setContentText(subTitle)
                .setTargetView(view)
                .setGravity(Gravity.center)
                .setDismissType(DismissType.outside)
                .build()
                .show();

    }

}
