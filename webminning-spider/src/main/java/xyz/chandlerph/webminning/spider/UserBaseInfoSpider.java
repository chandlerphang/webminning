package xyz.chandlerph.webminning.spider;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import xyz.chandlerph.webminning.spider.domain.UserBaseInfo;

/**
 * 用户基本信息抓取，注解方式爬取
 */
public class UserBaseInfoSpider {

	private static final String START_URL = "http://www.zhihu.com/people/excited-vczh";

	private Site site = Site
			.me()
			.setCycleRetryTimes(5)
			.setRetryTimes(5)
			.setSleepTime(1000)
			.setTimeOut(3 * 60 * 1000)
			.setUserAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
			.setCharset("UTF-8");

	public void crawl() {
		OOSpider.create(site, new UserBaseInfoPipeline(), UserBaseInfo.class)
				.addUrl(START_URL)
				.thread(50).run();
	}

	public static void main(String[] args) {
		UserBaseInfoSpider spider = new UserBaseInfoSpider();
		spider.crawl();
	}
}
