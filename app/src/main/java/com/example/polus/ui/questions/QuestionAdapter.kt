package com.example.polus.ui.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R
import com.example.polus.data.Question

class QuestionAdapter(
    private var questions: List<Question>,
    private val onItemClicked: (Question) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    fun updateQuestions(newQuestions: List<Question>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

    class QuestionViewHolder(
        itemView: View,
        private val onItemClicked: (Question) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)

        fun bind(question: Question) {
            titleTextView.text = question.title
            descriptionTextView.text = question.description
            subjectTextView.text = question.subject

            itemView.setOnClickListener {
                onItemClicked(question)
            }
        }
    }
}
