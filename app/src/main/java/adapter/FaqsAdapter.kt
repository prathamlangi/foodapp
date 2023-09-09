package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import model.FaqsData

class FaqsAdapter(private val context: Context, private val itemList:List<FaqsData>) :
    RecyclerView.Adapter<FaqsAdapter.FaqsViewHolder>() {
    class FaqsViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtFaqsTitle:TextView = view.findViewById(R.id.questionTextView)
        val txtFaqsDescription:TextView = view.findViewById(R.id.answerTextView)
//        val imgFaqsbtn:TextView = view.findViewById(R.id.faqs_title_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_single_row_faqs, parent, false)
        return FaqsAdapter.FaqsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FaqsViewHolder, position: Int) {
        val faqs = itemList[position]
        holder.txtFaqsTitle.text = faqs.faqsTitle
        holder.txtFaqsDescription.text = faqs.faqsDescription

        holder.txtFaqsTitle.setOnClickListener {
            val visibility = holder.txtFaqsDescription.visibility
            holder.txtFaqsDescription.visibility =
                if (visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }
    }