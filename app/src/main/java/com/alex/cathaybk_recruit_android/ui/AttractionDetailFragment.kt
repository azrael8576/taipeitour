package com.alex.cathaybk_recruit_android.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alex.cathaybk_recruit_android.GlideApp
import com.alex.cathaybk_recruit_android.databinding.FragmentAttractionDetailBinding
import com.alex.cathaybk_recruit_android.utilities.getFacebookPageURL
import com.alex.cathaybk_recruit_android.utilities.isAppInstalled
import com.alex.cathaybk_recruit_android.viewmodels.AttractionViewModel


fun actionToWebViewFragment(url: String, name: String): View.OnClickListener {
    return View.OnClickListener { view ->
        val direction =
            AttractionDetailFragmentDirections.actionAttractionDetailFragmentToWebViewFragment(
                url,
                name
            )
        view.findNavController().navigate(direction)
    }
}

private fun actionToOpenFbApp(context: Context, fbUrl: String, name: String): View.OnClickListener {
    return View.OnClickListener { view ->
        if (isAppInstalled(context)) {
            val facebookIntent = Intent(Intent.ACTION_VIEW)
            val facebookUrl: String = getFacebookPageURL(context, fbUrl)
            if (facebookUrl.contains("fb://")) {
                facebookIntent.data = Uri.parse(facebookUrl)
                startActivity(context, facebookIntent, null)
            } else {
                actionToWebViewFragment(fbUrl, name).onClick(view)
            }
        } else {
            actionToWebViewFragment(fbUrl, name).onClick(view)
        }
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
            binding.btnOfficialSite.visibility = if (it.official_site.isNotEmpty()) View.VISIBLE else View.GONE
            binding.btnFacebook.visibility = if (it.facebook.isNotEmpty()) View.VISIBLE else View.GONE

            binding.btnOfficialSite.setOnClickListener(actionToWebViewFragment(it.official_site, it.name))
            binding.btnFacebook.setOnClickListener(actionToOpenFbApp(requireContext(), it.facebook, it.name))
        }
    }
}