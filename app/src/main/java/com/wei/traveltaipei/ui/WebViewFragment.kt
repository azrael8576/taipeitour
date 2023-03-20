package com.wei.traveltaipei.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.wei.traveltaipei.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentWebViewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            toolbar.title = args.title

            if (args.url.isNotEmpty()) {
                progressbar.max = PROGRESSBAR_MAX
                progressbar.progress = 1
                webview.settings.javaScriptEnabled = true
                webview.webChromeClient = getWebChromeClient()
                webview.webViewClient = getWebViewClient()
                webview.loadUrl(args.url)
            }

            setHasOptionsMenu(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.apply {
            linearLayout.removeAllViews()
            webview.destroy()
        }
    }

    private fun getWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressbar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressbar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url == null) return false
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        return true
                    }
                } catch (e: Exception) {
                    return true
                }
                view.loadUrl(url)
                return true
            }
        }
    }

    private fun getWebChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressbar.progress = newProgress
            }
        }
    }

    companion object {
        const val PROGRESSBAR_MAX = 100
    }

}
