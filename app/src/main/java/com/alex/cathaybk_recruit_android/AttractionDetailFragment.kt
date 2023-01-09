package com.alex.cathaybk_recruit_android

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Html
import android.util.Log
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
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionDetailBinding
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel

fun actionToWebViewFragment(url: String): View.OnClickListener {
    return View.OnClickListener { view ->
        val direction =
            AttractionDetailFragmentDirections.actionAttractionDetailFragmentToWebViewFragment(
                url
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
            binding.textAttractionName.text = it.name
            binding.textOfficialSite.text = Html.fromHtml("<u>${it.official_site}</u>")
            binding.textFacebook.text = Html.fromHtml("<u>${it.facebook}</u>")

            binding.textOfficialSite.setOnClickListener(actionToWebViewFragment(binding.textOfficialSite.text.toString()))
            binding.textFacebook.setOnClickListener(actionToWebViewFragment(binding.textFacebook.text.toString()))

            for (image in it.images) {
                if (binding.imgBackdrop.isVisible){
                    break
                } else if (image.src.startsWith("http") && image.ext.isNotEmpty()) {
                    Log.e(TAG, "subscribeUi: ${image.src}", )

                    binding.imgBackdrop.visibility = View.VISIBLE
                    glide.load(image.src)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.imgBackdrop)
                }
            }
        }
    }
}