package fragement

import adapter.FaqsAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import model.FaqsData


class FAQsFragment : Fragment() {

    private lateinit var recyclerFaqs: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: FaqsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f_a_qs, container, false)

        recyclerFaqs = view.findViewById(R.id.recycler_faqs)

        layoutManager = LinearLayoutManager(activity)

        val faqsInfoList = listOf(
            FaqsData("pratham", "doing work"),
            FaqsData(
                "Q1: How do I order food using the app?",
                "A1:To order food, open the app, select your preferred restaurant, browse the menu, add items to your cart, and proceed to checkout. You can choose delivery or pickup options."
            ),
            FaqsData(
                "Q2: How long does it take for food to be delivered?",
                "A2:Delivery times depend on factors like restaurant location and order volume. Typically, it ranges from 30 minutes to an hour."
            ),
            FaqsData(
                "Q3: What if I have food allergies or dietary restrictions?",
                "A3: Food delivery apps often allow you to specify special instructions or allergies when ordering. You can also filter restaurants by cuisine or dietary preferences."
            ),
            FaqsData(
                "Q4: Do food delivery apps charge delivery fees?",
                "A4: Yes, most apps have a delivery fee that varies based on distance and location. Sometimes, they offer free delivery promotions."
            ),
            FaqsData(
                "Q5: Is there a customer support hotline if I have issues with the app?",
                "A5: Yes, food delivery apps typically provide a customer support hotline or chat support for assistance with any app-related problems."
            )

        )
        recyclerAdapter = FaqsAdapter(activity as Context, faqsInfoList)
        recyclerFaqs.adapter = recyclerAdapter
        recyclerFaqs.layoutManager = layoutManager

        return view
    }

}