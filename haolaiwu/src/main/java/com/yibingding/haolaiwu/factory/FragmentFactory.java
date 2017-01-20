package com.yibingding.haolaiwu.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import com.yibingding.haolaiwu.fragment.FragmentFirst;
import com.yibingding.haolaiwu.fragment.FragmentFouth;
import com.yibingding.haolaiwu.fragment.FragmentSecond;
import com.yibingding.haolaiwu.fragment.FragmentThird;

import android.support.v4.app.Fragment;

/**
 * 用一级缓存，存储要切换的视图ui
 */
public class FragmentFactory {

	/**
	 * 根据下标返回指定 界面的fragment
	 */
	private static Map<Integer, Fragment> fragments = new LinkedHashMap<Integer, Fragment>();

	/**
	 * 1 是 首页 2 是分类页 3 是购物车 4 是个人中心
	 */

	public static Fragment createFragment(int index) {
		Fragment fragment;

		fragment = fragments.get(index);
		if (fragment == null) {
			switch (index) {
			case 0:
				fragment = new FragmentFirst();
				break;
			case 1:
				fragment = new FragmentSecond();

				break;
			case 2:
				fragment = new FragmentThird();
				break;
			case 3:
				fragment = new FragmentFouth();
				break;
			case 4:
				// fragment = new MyCenterFragment();
				break;
			}
			// fragments.put(index, fragment);
		}

		return fragment;

	}

}
