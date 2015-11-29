package com.HitMode;

import com.laserGun.R;
import com.uidata.CommonData;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class AdapterRecycler  extends  RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    public String[] hitscores;
    public static Context context;

    public AdapterRecycler(Context context, String[] hitscores) {
        this.hitscores = hitscores;
        this.context = context;
    }

    public AdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hitresult, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterRecycler.ViewHolder viewHolder, int position) {
        int num = position + 1;
        if (num < 10) {
            viewHolder.tv_hitnum.setText("0" + num);
        } else {
            viewHolder.tv_hitnum.setText("" + num);
        }
        viewHolder.tv_hitscore.setText(hitscores[position]);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_hitnum;
        public TextView tv_hitscore;

        public ViewHolder(final View view, final int position) {
            super(view);
            tv_hitnum = (TextView) view.findViewById(R.id.hitnum);
            tv_hitscore = (TextView) view.findViewById(R.id.hitscore);
            tv_hitscore.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        HitModeActivity activity = (HitModeActivity) context;
                        Map<Integer, ArrayList<String>> map = activity.getMap();
                        if (map != null && map.size() != 0 && map.containsKey(position)) {
                            Intent intent = new Intent(activity, QueryGrade.class);
                            intent.putExtra("HitPosition", position);
                            intent.putExtra("gradeArrayList", map.get(position));
                            ((HitModeActivity) context).startActivityForResult(intent, 1);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return CommonData.TARGETNUM;
    }
}