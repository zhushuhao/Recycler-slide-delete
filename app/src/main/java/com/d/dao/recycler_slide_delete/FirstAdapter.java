package com.d.dao.recycler_slide_delete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dao on 7/30/16.
 */
public class FirstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<String> list = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private MyRecyclerView recyclerView;


    public FirstAdapter(Context context, ArrayList<String> list, MyRecyclerView recyclerView) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TextHolder(inflater.inflate(R.layout.item, null, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextHolder viewHolder = (TextHolder) holder;

        viewHolder.tv.setText("第" + String.valueOf(list.get(position)) + "项");
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("delete", "clicked3");
                recyclerView.close();
                list.remove(position);
                notifyDataSetChanged();
//                recyclerView.turnToNormal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {

        public TextView tv, delete;
        public LinearLayout root;

        public TextHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.text);
            delete = (TextView) itemView.findViewById(R.id.delete);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }
}
