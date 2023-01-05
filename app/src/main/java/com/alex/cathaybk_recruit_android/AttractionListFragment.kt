package com.alex.cathaybk_recruit_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alex.cathaybk_recruit_android.adapter.AttractionAdapter
import com.alex.cathaybk_recruit_android.api.ServiceLocator
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionListBinding
import com.alex.cathaybk_recruit_android.repository.AttractionRepository
import com.alex.cathaybk_recruit_android.utilities.InjectorUtils
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel
import kotlinx.coroutines.flow.collectLatest

class AttractionListFragment : Fragment() {

    private lateinit var adapter: AttractionAdapter
    private lateinit var binding: FragmentAttractionListBinding
    private val sharedViewModel: AttractionViewModel by activityViewModels{
        val repoType = AttractionRepository.Type.values()[AttractionRepository.Type.DB.ordinal]
        val repo = ServiceLocator.instance(requireContext())
            .getRepository(repoType)
        @Suppress("UNCHECKED_CAST")
        InjectorUtils.provideAttractionViewModelFactory(repo, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAttractionListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            super.onViewCreated(view, savedInstanceState)
            initToolbar(this)
            adapter = AttractionAdapter()
            list.adapter = adapter
            subscribeUi(adapter)
            setHasOptionsMenu(true)
        }
    }

    private fun initToolbar(binding: FragmentAttractionListBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbarOverflowMenuButton.setOnClickListener(View.OnClickListener {
            //TODO remove nav, add 翻譯功能
            Toast.makeText(context, "TODO remove nav, add 翻譯功能", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_attractionListFragment_to_attractionDetailFragment)
        })
    }

    private fun subscribeUi(adapter: AttractionAdapter) {
        //        val glide = GlideApp.with(this)
        lifecycleScope.launchWhenCreated {
            sharedViewModel.attractionList.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}