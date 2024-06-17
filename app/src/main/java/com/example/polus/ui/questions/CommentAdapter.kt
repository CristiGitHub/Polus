package com.example.polus.ui.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.data.Comment

class CommentAdapter(private var comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentTextView.text = comment.creator
        holder.commentTextView.text = comment.text
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        val creatorTextView: TextView = itemView.findViewById(R.id.creatorTextView)
    }
}
