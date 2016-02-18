package com.zb.zhihuianyang.domain;

import java.util.ArrayList;



/**
 * 组图对象封装
 * 
 * @author Ace
 * @date 2016-02-08
 */
public class PhotoBeans {
	public int retcode;
	public PhotoDatas data;

	public class PhotoDatas {
		public ArrayList<PhotoNewsDatas> news;
	}

	public class PhotoNewsDatas{
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String url;
	}
}
