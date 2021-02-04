package com.example.marxsaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

class PersonActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var button: Button
    private lateinit var button3: Button
    private lateinit var nameTextView: TextView
    private lateinit var db: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("PersonInfo")



        nameEditText = findViewById(R.id.nameEditText)
        button = findViewById(R.id.button)
        button3 = findViewById(R.id.button3)
         nameTextView = findViewById(R.id.nameTextView)

        button.setOnClickListener {
            mAuth.signOut()
            intent = Intent(applicationContext,PersonActivity:: class.java)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        button3.setOnClickListener {

            val name = nameEditText.text.toString()

             val personInfo = PersonInfo (name)

            if (mAuth.currentUser?.uid != null) {
                db.child(mAuth.currentUser?.uid!!).setValue(personInfo).addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show()
                        nameEditText.text = null

                    } else{
                        Toast.makeText(this, "Error!!!", Toast.LENGTH_SHORT).show()

                    }
                }


            }

        }

        if (mAuth.currentUser?.uid != null){
            db.child(mAuth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener{

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(this@PersonActivity, "Error!!! ", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val  p = snapshot.getValue(PersonInfo::class.java)

                    if (p != null){
                        nameTextView.text = p.name

                    }

                }

            })
        }



    }
}