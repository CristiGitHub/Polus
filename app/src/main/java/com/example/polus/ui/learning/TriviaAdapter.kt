package com.example.polus.ui.learning

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polus.R

class TriviaAdapter(
    private val triviaList: List<Trivia>,
    private val onSubmitClick: (String, String) -> Unit
) : RecyclerView.Adapter<TriviaAdapter.TriviaViewHolder>() {

    class TriviaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textQuestion: TextView = itemView.findViewById(R.id.text_question)
        val editAnswer: EditText = itemView.findViewById(R.id.edit_answer)
        val buttonSubmit: Button = itemView.findViewById(R.id.button_submit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriviaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trivia, parent, false)
        return TriviaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TriviaViewHolder, position: Int) {
        val trivia = triviaList[position]
        holder.textQuestion.text = trivia.question
        holder.buttonSubmit.setOnClickListener {
            val userAnswer = holder.editAnswer.text.toString()
            onSubmitClick(userAnswer, trivia.answer)
        }
    }

    override fun getItemCount() = triviaList.size
}

data class Trivia(val question: String, val answer: String)
