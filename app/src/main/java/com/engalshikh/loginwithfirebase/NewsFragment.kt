package com.engalshikh.loginwithfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NewsFragment : Fragment() {

    private var db:FirebaseFirestore= FirebaseFirestore.getInstance()

    private   var collectionRefrence: CollectionReference =db.collection("News")
    private   var collectionRefrenceSport: CollectionReference =db.collection("SportNews")
    var adpterNews:AdpterNews?=null
    lateinit var title: EditText
    lateinit var det: EditText
    lateinit var image: EditText
    lateinit var save: Button
    lateinit var rec: RecyclerView
    lateinit var add: FloatingActionButton
    lateinit var card_add: CardView



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
        title=view.findViewById(R.id.title) as EditText
        det=view.findViewById(R.id.det)as EditText
        image=view.findViewById(R.id.image)as EditText
        save=view.findViewById(R.id.save)as Button
        rec=view.findViewById(R.id.rec)as RecyclerView

        add = view.findViewById(R.id.add_news) as FloatingActionButton
        card_add = view.findViewById(R.id.card_add_news)as CardView

        add.setOnClickListener {

            card_add.visibility=View.VISIBLE

        }
           save.setOnClickListener {
                   if (title.text.toString().trim().length<3){
                   Toast.makeText(context,"Small title, try again", Toast.LENGTH_LONG).show()
               }else if(det.text.toString().trim().length<12){
                   Toast.makeText(context,"Description Too Little Try again", Toast.LENGTH_LONG).show()

               }else{
                       if (contact=="News"){
                           addToNews("News")

                       }else if (contact=="sport"){

                           addToNews("SportNews")
                       }else{
                           addToNews("News")

                       }

               }


               title.setText("")
               det.setText("")
               image.setText("")
               card_add.visibility=View.GONE


        }









        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   if(contact=="sport"){
           feachSportNews()

        }else if(contact=="News"){

        feachNews()

        }else{



           var auth = FirebaseAuth.getInstance()
               auth?.signOut()
               var i =Intent(context,LoginActivity::class.java)
               startActivity(i)
               activity?.finish()


   }
    }


    fun addToNews(typeNews:String){





        db= FirebaseFirestore.getInstance()




        var news=News(title.text.toString(),det.text.toString(),image.text.toString())
        db.collection(typeNews).add(news).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context,"added", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(context,"filde to add ${it.exception}", Toast.LENGTH_LONG).show()
                Log.d("test",it.exception.toString())

            }
        }


    }

    fun feachNews(){


        val query: Query =collectionRefrence
        val firestoreRecyclerOptions= FirestoreRecyclerOptions.Builder<News>()
            .setQuery(query,News::class.java)
            .build()


        adpterNews=AdpterNews(firestoreRecyclerOptions)
        rec.layoutManager= LinearLayoutManager(context)
        rec.adapter=adpterNews
        Log.d("adpternews",adpterNews.toString())




    }



    fun feachSportNews(){


        val query: Query =collectionRefrenceSport
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
        if(contact=="logout"){

    }else{
            adpterNews!!.startListening()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(contact=="logout"){

        }else{
            adpterNews!!.stopListening()
        }
    }


//    fun redFromFireStore(){
//        db= FirebaseFirestore.getInstance()
//        val newsList = mutableListOf<News>();
//        db.collection("News").get().addOnCompleteListener{
//            val newsList = mutableListOf<News>();
//
//
//            if (it.isSuccessful) {
//                val newsList = mutableListOf<News>();
//                for (news in it.result!!) {
//                    Log.w("TAG", "done", it.exception)
//                    newsList.add(News.newsJSON(news.id,news.data))
//                }
//                //updateUI(newsList);
//            } else {
//                Log.w("TAG", "filde", it.exception)
//            }
//        }
//
//    }
}