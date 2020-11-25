package net.d155.pinballhighscoretracker

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    val RC_SIGN_IN= 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


// Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
            )

// Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_ball)
                    .build(),
                RC_SIGN_IN
            )

    }

    //adds a document in the machines collection to firestore database
    private fun addScore(machine:String, date:String, score:Int, name:String){

        val game:MutableMap<String,Any> = HashMap()
        game["machine"] = machine.toUpperCase()
        game["date"] = date
        game["score"] = score
        game["initials"] = name.toUpperCase()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                var selectedDate=""

                //Enables the user to select date played from a calendar
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

                //Uploads data to firestore database
                btnSubmit.setOnClickListener {

                    val machine = etMachineName.text.toString()
                    val score = etScore.text.toString().toInt()
                    val name = etName.text.toString()

                    addScore(machine,selectedDate,score,name)

                }

                btnViewScores.setOnClickListener {
                    startActivity(Intent(this@MainActivity,ViewScores::class.java))
                }

                btnSignOut.setOnClickListener {
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            recreate()
                        }
                }

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                recreate()
            }
        }
    }








}