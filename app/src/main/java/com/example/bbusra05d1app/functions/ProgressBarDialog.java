package com.example.bbusra05d1app.functions;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bbusra05d1app.R;

public class ProgressBarDialog {
    //datamember
    private AlertDialog dailog;
    private TextView tvMessage;
    //constructor
    public ProgressBarDialog(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_bar_layout,null);
        tvMessage = v.findViewById(R.id.progressbar_title);
        AlertDialog.Builder ad = new AlertDialog.Builder(context);

        dailog = ad.create();
        dailog.setView(v);
    }
    public void setMessage(String message){
        tvMessage.setText(message);
    }
    public void show(){

        dailog.show();
    }
    public void close(){

        dailog.dismiss();
    }
}
