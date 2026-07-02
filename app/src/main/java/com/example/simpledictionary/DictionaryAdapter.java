package com.example.simpledictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.WordViewHolder> {

    private ArrayList<WordModel> wordList;
    private OnItemClickListener clickListener;

    // واجهات الاستماع للأحداث (Interfaces for Clicks)
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    // دالة لربط الأحداث من الـ MainActivity
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    // المُشيد (Constructor)
    public DictionaryAdapter(ArrayList<WordModel> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel currentItem = wordList.get(position);
        holder.tvWord.setText(currentItem.getWord());
        holder.tvMeaning.setText(currentItem.getMeaning());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    // كلاس الـ ViewHolder لتعريف العناصر المرئية داخل تصميم العنصر
    public static class WordViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWord;
        public TextView tvMeaning;

        public WordViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tv_word);
            tvMeaning = itemView.findViewById(R.id.tv_meaning);

            // حدث الضغط العادي للتعديل
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // حدث الضغط المطول للحذف
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(position);
                            return true; // تعني أن الحدث تم التعامل معه بنجاح
                        }
                    }
                    return false;
                }
            });
        }
    }
}
