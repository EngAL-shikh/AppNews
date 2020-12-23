package com.engalshikh.loginwithfirebase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class AdpterNews(options: FirestoreRecyclerOptions<News>?) :
    FirestoreRecyclerAdapter<News, AdpterNews.AdpterNewsVH>(options as FirestoreRecyclerOptions<News>){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdpterNewsVH {
        return  AdpterNewsVH(LayoutInflater.from(p0.context).inflate(R.layout.news_list,p0,false))
    }

    override fun onBindViewHolder(holder: AdpterNewsVH, p1: Int, news: News) {

        holder?.title?.text=news?.title
        holder?.det?.text=news?.det
        holder?.image?.text=news?.image
        Log.d("title",news?.title.toString())
    }
    class AdpterNewsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title= itemView.findViewById(R.id.title) as TextView
        var det= itemView.findViewById(R.id.det) as TextView
        var image= itemView.findViewById(R.id.image) as TextView
    }




}