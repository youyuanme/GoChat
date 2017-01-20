package com.yibingding.haolaiwu;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ybd.app.BaseActivity;
import com.yibingding.haolaiwu.dialog.Dialognetwork;

public class FangdaijisuanqiActivity extends BaseActivity {

	private WebView wv_fangdai;
	private String urlString;
	private Dialognetwork dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_fangdaijisuanqi);
	}

	@Override
	public void initViews() {
		wv_fangdai = (WebView) findViewById(R.id.wv_fangdai);
		urlString = getIntent().getStringExtra("url");
	}

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(this);
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public void initData() {
		show();
		wv_fangdai.loadUrl(urlString);
		WebSettings webSettings = wv_fangdai.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wv_fangdai.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				dismiss();
				super.onPageFinished(view, url);
			}

		});
	}

	public void back(View v) {
		finish();
	}
}
