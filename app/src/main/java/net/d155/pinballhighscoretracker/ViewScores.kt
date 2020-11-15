package net.d155.pinballhighscoretracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_view_scores.*

class ViewScores : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_scores)



        populateSpinner()


    }


    private fun populateSpinner(){

        var machineList = db.collection("machines")
        var list = arrayListOf<String>()


        machineList.get().addOnCompleteListener {
                if(it.isSuccessful){
                    val document = it.result
                    if (document != null) {
                        for(x in document){
                            var game = x.get("machine").toString().toUpperCase()
                            list.add(game)
                        }
                        Log.d("LIST","Full List+$list")
                        list = ArrayList(list.distinct())
                        Log.d("LIST","list.distinct()+${list.distinct()}")
                        Log.d("LIST","Full List+$list")

                        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, list)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        spinner.adapter = adapter
                        adapter.notifyDataSetChanged()

                    }
                }
        }






    }
}