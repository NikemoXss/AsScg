package com.ys.www.asscg.fragment;



import android.support.v4.app.Fragment;

/**
 * Created by admin on 13-11-23.
 */
public class FragmentFactory {
	public static Fragment getInstanceByIndex(int index) {
		Fragment fragment = null;
		switch (index) {
		case 1:
			fragment = new IndexFragment_Scg();// 主页
			break;
		case 2:
			fragment = new AccountFragment_Scg();// 账户
			break;
		case 3:
			fragment = new SetFragment_Scg();// 设置
			break;

		}
		return fragment;
	}
}
