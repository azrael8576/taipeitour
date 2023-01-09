package com.alex.cathaybk_recruit_android.ui

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alex.cathaybk_recruit_android.GlideApp
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionDetailBinding
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel
import com.alex.cathaybk_recruit_android.vo.Attraction

fun actionToWebViewFragment(attraction: Attraction): View.OnClickListener {
    return View.OnClickListener { view ->
        val direction =
            AttractionDetailFragmentDirections.actionAttractionDetailFragmentToWebViewFragment(
                attraction.url,
                attraction.name
            )
        view.findNavController().navigate(direction)
    }
}

class AttractionDetailFragment : Fragment() {

    private lateinit var binding: FragmentAttractionDetailBinding
    private val sharedViewModel: AttractionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAttractionDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initToolbar(binding.toolbar)
            subscribeUi(binding)
        }
    }

    private fun initToolbar(toolbar: Toolbar) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun subscribeUi(binding: FragmentAttractionDetailBinding) {
        val glide = GlideApp.with(requireContext())

        sharedViewModel.clickedAttraction.observe(viewLifecycleOwner) {

            for (image in it.images) {
                if (binding.imgBackdrop.isVisible){
                    break
                } else if (image.src.startsWith("http") && image.ext.isNotEmpty()) {
                    binding.imgBackdrop.visibility = View.VISIBLE
                    glide.load(image.src)
                        .centerCrop()
                        .into(binding.imgBackdrop)
                }
            }

            binding.textAttractionName.text = it.name
            binding.textAttractionName.visibility = if (binding.textAttractionName.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textIntroduction.text = it.introduction
            binding.textIntroduction.visibility = if (binding.textIntroduction.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textOpenTime.text = it.open_time
            binding.textOpenTime.visibility = if (binding.textOpenTime.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textAddress.text = "Address\n${it.address}"
            binding.textAddress.visibility = if (binding.textAddress.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textModified.text = "Last Updated Time\n${it.modified}"
            binding.textModified.visibility = if (binding.textModified.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textOfficialSite.text = Html.fromHtml("<u>${it.official_site}</u>")
            binding.textOfficialSite.visibility = if (binding.textOfficialSite.text.isNotEmpty()) View.VISIBLE else View.GONE
            binding.textFacebook.text = Html.fromHtml("<u>${it.facebook}</u>")
            binding.textFacebook.visibility = if (binding.textFacebook.text.isNotEmpty()) View.VISIBLE else View.GONE

            binding.textOfficialSite.setOnClickListener(actionToWebViewFragment(it))
            binding.textFacebook.setOnClickListener(actionToWebViewFragment(it))
        }
    }
}