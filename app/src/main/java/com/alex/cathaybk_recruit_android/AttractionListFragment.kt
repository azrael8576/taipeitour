package com.alex.cathaybk_recruit_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import com.alex.cathaybk_recruit_android.adapter.AttractionAdapter
import com.alex.cathaybk_recruit_android.adapter.AttractionLoadStateAdapter
import com.alex.cathaybk_recruit_android.api.ServiceLocator
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionListBinding
import com.alex.cathaybk_recruit_android.paging.asMergedLoadStates
import com.alex.cathaybk_recruit_android.repository.AttractionRepository
import com.alex.cathaybk_recruit_android.utilities.InjectorUtils
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class AttractionListFragment : Fragment() {

    private lateinit var selectedLangs: String
    private var selectedLangIndexs: Int = 0

    private lateinit var adapter: AttractionAdapter
    private lateinit var binding: FragmentAttractionListBinding
    private val langs = arrayOf("zh-tw", "zh-cn", "en", "ja", "ko",
        "es", "id", "th", "vi")

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
            initAdapter()
            initSwipeToRefresh()
            setHasOptionsMenu(true)
        }
    }

    private fun initAdapter() {
//        val glide = GlideApp.with(this)
        adapter = AttractionAdapter()
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = AttractionLoadStateAdapter(adapter),
            footer = AttractionLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            sharedViewModel.attractionList.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun initToolbar(binding: FragmentAttractionListBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbarOverflowMenuButton.setOnClickListener(View.OnClickListener {
            selectedLangs = langs[selectedLangIndexs]
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("List of Lang")
                .setSingleChoiceItems(langs, selectedLangIndexs) { dialog_, which ->
                    binding.list.scrollToPosition(0)
                    selectedLangIndexs = which
                    selectedLangs = langs[which]
                    Toast.makeText(requireContext(), "$selectedLangs Selected", Toast.LENGTH_SHORT)
                        .show()
                    sharedViewModel.showLang(selectedLangs)
                    dialog_.dismiss()
                }
                .show()
        })
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }
}