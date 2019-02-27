package com.remotearthsolutions.expensetracker.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.utils.Constants;

public class WebViewFragment extends Fragment {

    public WebViewFragment() {
    }

    private ProgressBar progressBar;
    private WebView webView;
    private String url, screen;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        if (getArguments() != null) {
            url = getArguments().getString(Constants.KEY_URL);
            screen = getArguments().getString("screen");
        }

        if (screen.equals("license_details")) {
            Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }

        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        
        return view;
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

    }


}