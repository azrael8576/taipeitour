package com.alex.cathaybk_recruit_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionListBinding
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel

class AttractionListFragment : Fragment() {

    private var binding: FragmentAttractionListBinding? = null
    private val sharedViewModel: AttractionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAttractionListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            super.onViewCreated(view, savedInstanceState)

            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)

            toolbar.setupWithNavController(navController, appBarConfiguration)

            toolbarOverflowMenuButton.setOnClickListener(View.OnClickListener {
                //TODO remove nav, add 翻譯功能
                Toast.makeText(context, "TODO remove nav, add 翻譯功能", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_attractionListFragment_to_attractionDetailFragment)
            })
        }
    }
}