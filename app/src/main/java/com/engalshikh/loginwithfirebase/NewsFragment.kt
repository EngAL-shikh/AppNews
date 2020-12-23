package com.engalshikh.loginwithfirebase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NewsFragment : Fragment() {

    private var db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private  lateinit var card_news: CardView
    private   var collectionRefrence: CollectionReference =db.collection("News")
    var adpterNews:AdpterNews?=null
    lateinit var title: EditText
    lateinit var det: EditText
    lateinit var image: EditText
    lateinit var save: Button
    lateinit var rec: RecyclerView
    //private lateinit var adapter: NewsAdapter

    companion object{
        fun newInstance(data:String):NewsFragment{
            val args=Bundle().apply {
                putSerializable("name",data)
            }
            return  NewsFragment().apply {
                arguments=args
            }
        }
    }

    var contact:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contact=arguments?.getSerializable("name")as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view= inflater.inflate(R.layout.fragment_news, container, false)
        card_news= view?.findViewById(R.id.card_news) as CardView
        title=view.findViewById(R.id.title) as EditText
        det=view.findViewById(R.id.det)as EditText
        image=view.findViewById(R.id.image)as EditText
        save=view.findViewById(R.id.save)as Button
        rec=view.findViewById(R.id.rec)as RecyclerView
        rec = view.findViewById(R.id.rec)

       // rec.layoutManager = LinearLayoutManager(context)
        save.setOnClickListener {

            addToFireStore()


        }


        if (contact=="News"){

            card_news.visibility=View.VISIBLE
        }else{
            card_news.visibility=View.GONE
        }






        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // redFromFireStore()
        recNews()
    }
//
//    private fun updateUI(newsList: List<News>) {
//
//        adapter = NewsAdapter(newsList)
//        rec.adapter = adapter
//        Toast.makeText(requireContext(),newsList[0].title,Toast.LENGTH_LONG).show()
//    }
//
//    private class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//        View.OnClickListener {
//        private val title = itemView.findViewById(R.id.title) as TextView
//        private val det = itemView.findViewById(R.id.det) as TextView
//        private val image = itemView.findViewById(R.id.image) as TextView
//
//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        fun bind(news: News) {
//            title.text = news.title;
//            det.text = news.det;
//            image.text = news.image
//        }
//
//        override fun onClick(p0: View?) {}
//    }
//    private class NewsAdapter(private val newsList: List<News>) :
//        RecyclerView.Adapter<NewsHolder>() {
//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): NewsHolder {
//            val view =
//                LayoutInflater.from(parent.context).inflate(R.layout.news_list, parent, false)
//            return NewsHolder(view)
//        }
//
//        override fun getItemCount(): Int = newsList.size
//
//        override fun onBindViewHolder(holder: NewsHolder, position: Int) {
//            val news = newsList[position]
//            holder.bind(news)
//        }
//
//    }

    fun addToFireStore(){

        db= FirebaseFirestore.getInstance()

        //var raw:MutableMap<String,Any> = HashMap()

//        raw.put("title",title.text.toString())
//        raw.put("det",det.text.toString())
//        raw.put("image",image.text.toString())
//Log.d("test",title.text.toString())

        var news=News(title.text.toString(),det.text.toString(),image.text.toString())
        db.collection("News").add(news).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context,"added", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(context,"filde to add ${it.exception}", Toast.LENGTH_LONG).show()
                Log.d("test",it.exception.toString())

            }
        }


    }

    fun recNews(){


        val query: Query =collectionRefrence
        val firestoreRecyclerOptions= FirestoreRecyclerOptions.Builder<News>()
            .setQuery(query,News::class.java)
            .build()


        adpterNews=AdpterNews(firestoreRecyclerOptions)
        rec.layoutManager= LinearLayoutManager(context)
        rec.adapter=adpterNews
        Log.d("adpternews",adpterNews.toString())




    }

    override fun onStart() {
                super.onStart()
        adpterNews!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adpterNews!!.stopListening()

    }
    fun redFromFireStore(){
        db= FirebaseFirestore.getInstance()
        val newsList = mutableListOf<News>();
        db.collection("News").get().addOnCompleteListener{
            val newsList = mutableListOf<News>();


            if (it.isSuccessful) {
                val newsList = mutableListOf<News>();
                for (news in it.result!!) {
                    Log.w("TAG", "done", it.exception)
                    newsList.add(News.newsJSON(news.id,news.data))
                }
                //updateUI(newsList);
            } else {
                Log.w("TAG", "filde", it.exception)
            }
        }

//            for (decoument in it.result!!){
//                Log.d("data",decoument.data.getValue("title").toString())
//                var title=decoument.data.getValue("title").toString()
//                var det=decoument.data.getValue("det").toString()
//                var image=decoument.data.getValue("image").toString()
//                var news=News()
//
//
//
//
//
//
//              news.title=decoument.data.get("title").toString()
//              news.title=decoument.data.getValue("det").toString()
//              news.title=decoument.data.getValue("image").toString()
//
//            }
    }
}