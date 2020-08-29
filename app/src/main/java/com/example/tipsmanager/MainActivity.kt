package com.example.tipsmanager

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.modifie_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var totalSales: Double = 0.0
    var totalTips: Double = 0.0
    var totalPeople: Double = 0.0
    var percentage = 0.02
    var mref:DatabaseReference? = null
    var statsRef:DatabaseReference? = null
    var mNoteList:ArrayList<Note>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database:FirebaseDatabase = FirebaseDatabase.getInstance()
        mref = database.getReference("Notes")
        statsRef = database.getReference("Statistics")
        //statsRef!!.child("anass").setValue("haha")

        mNoteList = ArrayList()
        // Add a note
        btn_addNote.setOnClickListener{
            showDialogAddNote()
        }

        // Modify or delete a note through a long press on the item
        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { p0, p1, p2, p3 ->
            val alertBuilder = android.app.AlertDialog.Builder(this)
            var view = layoutInflater.inflate(R.layout.modifie_note, null)
            val alertDialogue = alertBuilder.create()
            alertDialogue.setView(view)
            alertDialogue.show()

            // Show the texts to be modified in the window
            var myNote = mNoteList?.get(p2)!!
            view.txt_modifie_name.setText(myNote.name)
            view.txt_modifie_sales.setText(myNote.sales.toString())
            view.txt_modifie_tips.setText((myNote.tip.toString()))

            // Update list item
            view.txt_modifie_update.setOnClickListener{
                var childRef = mref?.child(myNote.id!!)
                var name = view.txt_modifie_name.text.toString()
                var sale = view.txt_modifie_sales.text.toString()
                var tip = (sale.toDouble() * percentage).toString()

                var afterUpdate = Note(myNote.id!!, name, sale.toDouble(), tip.toDouble(), getCurrentDate())
                childRef?.setValue(afterUpdate)
                alertDialogue.dismiss()
            }

            // Delete List item
            view.txt_modifie_delete.setOnClickListener{
                mref?.child(myNote.id!!)!!.removeValue()
                alertDialogue.dismiss()
            }
            false
        }
    }



    override fun onStart() {
        super.onStart()

//        statsRef?.addValueEventListener(object: ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })

        mref?.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mNoteList?.clear()
                for (n in p0!!.children) {
                   var note = n.getValue(Note::class.java)
                    // Bring the note from Firebase and place it in the first row
                   mNoteList!!.add(0, note!!)
                }

                val noteAdapter = NoteAdapter(applicationContext, mNoteList!!)
                listView.adapter = noteAdapter
            }
        })
    }


    fun showDialogAddNote(){
        val alertBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_note, null)

        alertBuilder.setView(view)
        val alertDialog = alertBuilder.create()
        alertDialog.show()

        // Save note
        view.btn_saveNote.setOnClickListener{
            val name = view.txt_name.text.toString()
            val sales = view.txt_sales.text.toString()
            var tips:Double
            // Check if User Input is valid
            if (name.isEmpty() || sales.isEmpty())
            {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            // Add the user input to the database
            else
            {
                tips = sales.toDouble() * percentage
                var id = mref!!.push().key
                var myNote = Note(id!!, name, sales.toDouble(), Math.round(tips).toDouble(), getCurrentDate())
                mref!!.child(id).setValue(myNote)
                alertDialog.dismiss()
            }
        }
    }

    // Getting current Date
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("EE hh:mm a ")
        return mdformat.format(calendar.time)
    }


}
