package com.mmisoft.loop_movies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmisoft.loop_movies.data.datamodel.Movie
import com.mmisoft.loop_movies.data.datamodel.RecyclerViewDataItem
import com.mmisoft.loop_movies.databinding.FragmentHomeBinding
import com.mmisoft.loop_movies.ui.recyclerview.adapter.RecyclerViewHorizontalAdapter
import com.mmisoft.loop_movies.ui.dialog.FullscreenModalMovieBottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Firebase Login

        //findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

        // RecyclerView
        val recyclerView = binding!!.horizontalMovieImageRecyclerview
        val horizontalLayoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        val adapter = RecyclerViewHorizontalAdapter()
        adapter.setData(
            listOf(
                RecyclerViewDataItem.ImageViewItem("https://image.tmdb.org/t/p/w500/x21s3p5wPww534nYj1cWakTcqz4.jpg"),
                RecyclerViewDataItem.ImageViewItem("https://image.tmdb.org/t/p/w500/vQtBqpF2HDdzbfXHDzR4u37i1Ac.jpg"),
                RecyclerViewDataItem.ButtonItem {
                    val fullScreenMovieDetail =
                        FullscreenModalMovieBottomSheetDialog(
                            Movie(
                            rating = 3.9870000000000005,
                            id = 530915,
                            revenue = 374733942,
                            releaseDate = "2019-12-25",
                            director = null,
                            posterUrl = "https://image.tmdb.org/t/p/w500/iZf0KyrE25z1sage4SYFLCCrMi9.jpg",
                            cast = null,
                            runtime = 119,
                            title = "1917",
                            overview = "At the height of the First World War, two young British soldiers must cross enemy territory and deliver a message that will stop a deadly attack on hundreds of soldiers.",
                            reviews = 9932,
                            budget = 100000000,
                            language = "en",
                            genres = arrayListOf("War", "Drama", "Action", "Thriller", "History")
                        )
                        )
                    parentFragmentManager.let {
                        fullScreenMovieDetail.show(
                            it,
                            "fullScreenMovieDetailDialog"
                        )
                    }
                }
            )
        )
        recyclerView.adapter = adapter

        //binding?.searchAllMoviesButton?.elevation = 0f

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}