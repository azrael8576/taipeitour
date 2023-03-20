package com.wei.traveltaipei.ui

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
import com.wei.traveltaipei.GlideApp
import com.wei.traveltaipei.R
import com.wei.traveltaipei.adapter.AttractionAdapter
import com.wei.traveltaipei.adapter.AttractionLoadStateAdapter
import com.wei.traveltaipei.api.ServiceLocator
import com.wei.traveltaipei.databinding.FragmentAttractionListBinding
import com.wei.traveltaipei.paging.asMergedLoadStates
import com.wei.traveltaipei.repository.AttractionRepository
import com.wei.traveltaipei.utilities.InjectorUtils
import com.wei.traveltaipei.viewmodels.AttractionViewModel
import com.wei.traveltaipei.vo.Attraction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class AttractionListFragment : Fragment(), OnClickAttractionListener {

    private lateinit var selectedLangs: String
    private var selectedLangIndexs: Int = 0

    private lateinit var adapter: AttractionAdapter
    private lateinit var binding: FragmentAttractionListBinding

    private val sharedViewModel: AttractionViewModel by activityViewModels {
        val repoType = AttractionRepository.Type.values()[AttractionRepository.Type.DB.ordinal]
        val repo = ServiceLocator.instance(requireContext())
            .getRepository(repoType)
        @Suppress("UNCHECKED_CAST")
        InjectorUtils.provideAttractionViewModelFactory(repo, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        val glide = GlideApp.with(this)
        adapter = AttractionAdapter(glide, this@AttractionListFragment)
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
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.traveltaipei_gray_50_a600))
        binding.toolbarTranslateButton.setColorFilter(resources.getColor(R.color.traveltaipei_gray_50_a600))
        binding.toolbarTranslateButton.setOnClickListener(getLangDialog())
    }

    private fun getLangDialog(): View.OnClickListener {
        return View.OnClickListener { view ->
            val langs = resources.getStringArray(R.array.langs)
            selectedLangs = langs[selectedLangIndexs]
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialThemeDialog)
                .setTitle("List of Lang")
                .setSingleChoiceItems(langs, selectedLangIndexs) { dialog_, which ->
                    binding.list.scrollToPosition(0)
                    selectedLangIndexs = which
                    selectedLangs = langs[which]
                    Toast.makeText(requireContext(), "Selected $selectedLangs", Toast.LENGTH_SHORT)
                        .show()
                    sharedViewModel.showLang(selectedLangs)
                    dialog_.dismiss()
                }
                .setBackground(resources.getDrawable(R.drawable.selector_lang_dialog))
                .show()
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    override fun setClickedAttraction(attraction: Attraction) {
        sharedViewModel.setClickedAttraction(attraction)
    }
}

interface OnClickAttractionListener {
    fun setClickedAttraction(attraction: Attraction)
}
