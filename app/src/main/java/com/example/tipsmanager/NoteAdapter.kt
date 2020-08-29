package com.example.tipsmanager

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.add_note.view.*
import kotlinx.android.synthetic.main.add_note.view.txt_name
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteAdapter (context: Context, noteList: ArrayList<Note>)
                : ArrayAdapter<Note>(context, 0, noteList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false)
        val note:Note = getItem(position)!!
        view.txt_name2.text = note.name
        view.txt_sales2.text = note.sales.toString()
        view.txt_tip2.text = note.tip.toString()
        view.txt_time2.text = note.timestamp.toString()
        return view
    }

}