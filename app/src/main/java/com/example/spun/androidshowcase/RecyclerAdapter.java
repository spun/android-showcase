package com.example.spun.androidshowcase;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private String[] mDataset;
    MyAdapterOnClick mListenerAdapter;

    // Constructor
    public RecyclerAdapter(String[] myDataset, MyAdapterOnClick myListenerAdapter) {
        mDataset = myDataset;
        mListenerAdapter = myListenerAdapter;
    }

    // Crea nuevas vistas (invocado por el layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_card, parent, false);

        return new MyViewHolder(v, mListenerAdapter);
    }

    // Reemplaza los contenidos de una vista (invocado por el layout manager)
    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position]);
    }

    // Revuelve el tamaño de la lista de datos (invocado por el layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public interface MyAdapterOnClick {
        void onTap(String charSequence);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        // cada elemento es una cadena de texto en este caso
        MyAdapterOnClick mListener;
        public TextView mTextView;

        public MyViewHolder(View v, MyAdapterOnClick listener) {
            super(v);
            mListener = listener;
            mTextView = (TextView) v.findViewById(R.id.text_cardview);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof CardView) {
                mListener.onTap(mTextView.getText().toString());
            }
        }
    }
}
