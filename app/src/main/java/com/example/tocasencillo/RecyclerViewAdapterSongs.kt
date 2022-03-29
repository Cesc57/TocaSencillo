package com.example.tocasencillo

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tocasencillo.databinding.ItemRecyclerBinding

class RecyclerViewAdapterSongs
    : RecyclerView.Adapter<RecyclerViewAdapterSongs.ViewHolder>() {

    lateinit var context: Context
    lateinit var cursor: Cursor

    fun RecyclerViewAdapterSongs(context: Context, cursor: Cursor) {
        this.context = context
        this.cursor = cursor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)

        holder.tvSong.text = cursor.getString(1)
    }

    override fun getItemCount(): Int {
        if (cursor == null) {
            return 0
        } else {
            return cursor.count
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder {

        val tvSong: TextView

        constructor(view: View) : super(view) {
            val bindingItemsRV = ItemRecyclerBinding.bind(view)
            tvSong = bindingItemsRV.recSong

            view.setOnClickListener {
                Toast.makeText(context, "${tvSong.text}", Toast.LENGTH_SHORT).show()
            }

        }
    }


}