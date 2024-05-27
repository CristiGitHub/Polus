package com.example.polus.ui.questions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.models.Question

class QuestionAdapter(private val questionList: List<Question>) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.subjectTextView.text = question.subject
        holder.descriptionTextView.text = question.description
        holder.tagsTextView.text = question.tags
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val tagsTextView: TextView = itemView.findViewById(R.id.tagsAutoCompleteTextView)
    }
}
