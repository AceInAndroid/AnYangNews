package com.zb.zhihuianyang.domain;

import java.util.ArrayList;


/**
 * 新闻中心分类数据
 * 
 * gson对象封装原则:
 * 遇到{}就是一个对象
 * 遇到[]就是一个Arraylist
 * 对象中所有属性命名必须和服务器返回字段完全一致
 * 
 * @author Ace
 * @date 2015-2-12
 */
public class NewsMenuData {

	public int retcode;
	public ArrayList<String> extend;
	public ArrayList<NewsDatas> data;

	public class NewsDatas {
		public String id;
		public String title;
		public int type;
		public ArrayList<NewsTabDatas> children;
		
		@Override
		public String toString() {
			return "NewsDatas [title=" + title + ", children=" + children + "]";
		}
	}

	// 页签信息封装
	public class NewsTabDatas {
		public String id;
		public String title;
		public String url;
		public int type;
		
		@Override
		public String toString() {
			return "NewsTabDatas [title=" + title + "]";
		}
	}

	@Override
	public String toString() {
		return "NewsMenuData [data=" + data + "]";
	}
}