package net.d155.pinballhighscoretracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var selectedDate=""

        pckDatePlayed.setOnClickListener {
            val cal  = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dateOfPlay = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{ _, myear, mMonth, mday ->
                pckDatePlayed.text = "$myear/${mMonth+1}/$mday"
                selectedDate = "$myear/${mMonth+1}/$mday"},year,month,day)

            dateOfPlay.show()
        }

        btnSubmit.setOnClickListener {

            val machine = etMachineName.text.toString()
            val score = etScore.text.toString()
            val name = etName.text.toString()

            addScore(machine,selectedDate,score,name)

        }


    }

    private fun addScore(machine:String, date:String, score:String, name:String){

        val game:MutableMap<String,Any> = HashMap()
        game["machine"] = machine
        game["date"] = date
        game["score"] = score
        game["initials"] = name

        db.collection("machines")
            .add(game)
            .addOnSuccessListener {
                Toast.makeText(this@MainActivity,"Added game successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity, "Failed to add game", Toast.LENGTH_SHORT).show()
            }

        etMachineName.text.clear()
        etScore.text.clear()
        etName.text.clear()
        pckDatePlayed.text ="Date Played"




    }
}