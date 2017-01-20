package com.yibingding.haolaiwu.weight;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class WRefreshListView extends PullToRefreshListView {

	public WRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WRefreshListView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
		// TODO Auto-generated constructor stub
	}

	public WRefreshListView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public WRefreshListView(Context context) {
		super(context);
	}
	
	

	public boolean isHeaderShown() {  
	    return getHeaderLayout().isShown();  
	}  
	  
	public boolean isFooterShown() {  
	    return getFooterLayout().isShown();  
	} 

}
