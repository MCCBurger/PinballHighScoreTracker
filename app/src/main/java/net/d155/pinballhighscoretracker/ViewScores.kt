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
        //select game from list listener run display scores inside listener
        displayScores("gameName")

    }

   //populate the spinner with unique names of machines from the database
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
                        Log.d("LIST","Unique List+$list")

                        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, list)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
        }

    }

    //Displays the list of high scores from the machine the user selected from the spinner
    private fun displayScores(machineName:String){
        //query database with game name
        //create list
        //sort scores in descending order
        //change textview to list


    }
}