package com.HitMode;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infraredgun.R;

import java.util.ArrayList;
/**
 * Created by summersunshine on 2015/11/28.
 */
public class GradeAdapter extends  RecyclerView.Adapter<GradeAdapter.ViewHolder>
{
    public ArrayList<String> arrListScores;
    public GradeAdapter(ArrayList<String> arrList)
    {
        this.arrListScores = arrList;
    }
    public ViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hitresult,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    public void onBindViewHolder(GradeAdapter.ViewHolder viewHolder, int position) {
        int num = position + 1;
        if(num<10)
        {
            viewHolder.tv_hitnum.setText("0"+num);
        }
        else {
            viewHolder.tv_hitnum.setText("" + num);
        }
        viewHolder.tv_hitscore.setText(arrListScores.get(position));
    }
    public int getItemCount() {
        // TODO Auto-generated method stub
        return arrListScores.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_hitnum;
        public TextView tv_hitscore;
        public ViewHolder(final View view) {
            super(view);
            tv_hitnum = (TextView)view.findViewById(R.id.hitnum);
            tv_hitscore = (TextView)view.findViewById(R.id.hitscore);
        }
    }
}
