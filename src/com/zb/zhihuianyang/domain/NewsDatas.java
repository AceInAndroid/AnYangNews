package com.zb.zhihuianyang.domain;

import java.util.ArrayList;

/**
 * 页签页的新闻列表数据
 * @author Ace 
 * @data 2016 -  2 - 14
 */
public class NewsDatas {
	public int retcode;
	public NewsTabs data;

	public class NewsTabs {
		public ArrayList<TopNews> topnews;
		public ArrayList<News> news;
		public String title;
		public String more;
		

		@Override
		public String toString() {
			return "NewsTabs [topnews=" + topnews + ", news=" + news + ", title=" + title + "]";
		}

		public class TopNews {
			public String id;
			public String pubdate;
			public String title;
			public String topimage;
			public String url;
			@Override
			public String toString() {
				return "TopNews [pubdate=" + pubdate + ", title=" + title + ", topimage=" + topimage + "]";
			}

			

		}

		public class News {
			public String id;
			public String pubdate;
			public String title;
			public String listimage;
			public String url;
			@Override
			public String toString() {
				return "News [pubdate=" + pubdate + ", title=" + title + ", listimage=" + listimage + "]";
			}

			

		}

	}

	@Override
	public String toString() {
		return "NewsDatas [data=" + data + "]";
	}

}
