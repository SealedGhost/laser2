package com.uidata;

import com.laserGun.MyApplication;
import com.laserGun.R;

/**
 * 存储于sharedPreference数据库中的变量名,数据库中存储打靶模式下的各种模式时间
 * @author summersunshine
 *
 */
public class PreferenceConstants {
	public final static String Mode1= MyApplication.getAppContext().getString(R.string.mode1);
	public final static String Mode2 = MyApplication.getAppContext().getString(R.string.mode2);
    public final static String Mode3= MyApplication.getAppContext().getString(R.string.mode3);
    public final static String GameTime = "gameTime";
}
