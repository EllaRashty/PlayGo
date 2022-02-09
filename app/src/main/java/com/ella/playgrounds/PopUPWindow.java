package com.ella.playgrounds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopUPWindow {
    private Activity activity;

    public PopUPWindow() {
    }

    public PopUPWindow(Activity activity) {
        this.activity = activity;
    }

    // show details
    public View PopUpWindowOnMap() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popUpView = inflater.inflate(R.layout.popup_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM | Gravity.CENTER, 0, 200);
        return popUpView;
    }
}
