package fragement

import adapter.HomeAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R


class HomeFragment : Fragment() {

    private lateinit var recyclerhome: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerhome = view.findViewById(R.id.recycler_home)

        layoutManager = LinearLayoutManager(activity)

        return view
    }




}