package net.d155.pinballhighscoretracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_view_scores.*

class ViewScores : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    var selectedItem =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_scores)

        populateSpinner()

        //Select game from spinner

        spinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@ViewScores,"Nothing Selected", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                Log.d("SELECTED ITEM",selectedItem)
                displayScores(selectedItem)
            }
        }

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

        var result = StringBuffer()
        result.append("SCORE:").append("\t\t\t\t\t\t").append("DATE:").append("\t\t\t\t\t\t").append("INITIALS:").append("\n\n")
        db.collection("machines")
            .whereEqualTo("machine",machineName)
            .orderBy("score",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents->
              for(document in documents){
                  Log.d("GET SCORES","GOT A SCORE")
                  result!!.append(document.data?.getValue("score")).append("\t\t\t\t\t\t\t").append(document.data?.getValue("date")).append("\t\t\t\t").append(document.data?.getValue("initials")).append("\n")
              }
             tvListofScores.text = result
            }
            .addOnFailureListener {
                Log.d("GET SCORES","FAILED TO GET SCORES")
            }

    }
}




