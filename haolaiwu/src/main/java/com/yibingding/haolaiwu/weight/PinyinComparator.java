package com.yibingding.haolaiwu.weight;

import java.util.Comparator;

import com.yibingding.haolaiwu.domian.CityInfo;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<CityInfo> {

	public int compare(CityInfo o1, CityInfo o2) {
		if (o1.firstN.equals("@")
				|| o2.firstN.equals("#")) {
			return -1;
		} else if (o1.firstN.equals("#")
				|| o2.firstN.equals("@")) {
			return 1;
		} else {
			return o1.firstN.compareTo(o2.firstN);
		}
	}

}
