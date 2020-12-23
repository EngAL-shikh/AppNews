package com.engalshikh.loginwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class FireStoreActivity : AppCompatActivity() {
     lateinit var title:EditText
     lateinit var det:EditText
     lateinit var image:EditText
     lateinit var save:Button
     lateinit var rec:RecyclerView

    private var db:FirebaseFirestore= FirebaseFirestore.getInstance()
    private   var collectionRefrence:CollectionReference=db.collection("News")
    var adpterNews:AdpterNews?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_store)


        title=findViewById(R.id.title)
        det=findViewById(R.id.det)
        image=findViewById(R.id.image)
        save=findViewById(R.id.save)

       // recNews()
        save.setOnClickListener {
//            addToFireStore()
//            redFromFireStore()

        }
//        val currentFragment =
//            supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if (currentFragment == null) {
//            val fragment = NewsFragment()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragment_container, fragment)
//                .commit()
//        }
    }








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
                Toast.makeText(this,"added",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"filde to add ${it.exception}",Toast.LENGTH_LONG).show()
                Log.d("test",it.exception.toString())

            }
        }


    }

//    fun redFromFireStore(){
//        db= FirebaseFirestore.getInstance()
//
//        db.collection("News").get().addOnCompleteListener{
//            val newsList = mutableListOf<News>();
//
//
//            if (it.isSuccessful) {
//                val newsList = mutableListOf<News>();
//                for (news in it.result!!) {
//                    Log.d("TAG", news.id + " => " + news.data)
//                    newsList.add(News.newsJSON(news.id,news.data))
//                }
//                updateUI(newsList);
//            } else {
//                Log.w("TAG", "Error getting documents.", it.exception)
//            }
//        }
//
////            for (decoument in it.result!!){
////                Log.d("data",decoument.data.getValue("title").toString())
////                var title=decoument.data.getValue("title").toString()
////                var det=decoument.data.getValue("det").toString()
////                var image=decoument.data.getValue("image").toString()
////                var news=News()
////
////
////
////
////
////
////              news.title=decoument.data.get("title").toString()
////              news.title=decoument.data.getValue("det").toString()
////              news.title=decoument.data.getValue("image").toString()
////
////            }
//        }
//
//
//
//    }
//
//    fun recNews(){
//        rec=findViewById(R.id.rec)
//
//        val query:Query=collectionRefrence
//        val firestoreRecyclerOptions= FirestoreRecyclerOptions.Builder<News>()
//            .setQuery(query,News::class.java)
//            .build()
//
//
//        adpterNews=AdpterNews(firestoreRecyclerOptions)
//        rec.layoutManager=LinearLayoutManager(this)
//        rec.adapter=adpterNews
//
//    }
//    override fun onStart() {
//        super.onStart()
//        adpterNews!!.startListening()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        adpterNews!!.stopListening()
//
//    }

}