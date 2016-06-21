package xyz.chandlerph.webminning.spider;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import xyz.chandlerph.webminning.spider.domain.Cookies;
import xyz.chandlerph.webminning.spider.mappers.CookiesMapper;

public class CookiesHelper {
	
	private static final int maxretry = 5;//最大重试次数，cookie无效达到此数则发警告邮件
	
	private static class CookiesHolder {  
        private static final Cookies INSTANCE;
        
        static {
        	SqlSession sqlsession = MyBatisSqlSessionFactory.openSession();

    		try {
    			CookiesMapper mapper = sqlsession.getMapper(CookiesMapper.class);
    			List<Cookies> list = mapper.selectAll();
    			
    			if(list == null || list.size() == 0) {
    				INSTANCE = null;
    			} else {
    				INSTANCE = list.get(0);
    			}
    		} 
    		catch (Exception e) {
    			throw new RuntimeException(e);
    		}
    		finally {
    			sqlsession.close();
    		}
        } 
    } 
	
	public static String getCookie() {
		if(CookiesHolder.INSTANCE == null) {
			return null;
		}
		
		if(StringUtils.isBlank(CookiesHolder.INSTANCE.getCookie())) {
			return null;
		}
		
		Spider spider = Spider.create(new PageProcessor() {
			@Override
			public void process(Page page) {
				String findname = page.getHtml().$(".zu-top-nav-userinfo .name").xpath("//span/text()").get();
				if(!getName().equals(findname)) {
					doRetry(page);
					return;
				}
				String xsrf = page.getHtml().xpath("//input[@name='_xsrf']/@value").get();
				if(StringUtils.isEmpty(xsrf)) {
					doRetry(page);
					return;
				}
				
				page.putField("cookie", CookiesHolder.INSTANCE.getCookie());
		        page.putField("xsrf", xsrf);
			}
			
			@Override
			public Site getSite() {
				Site site = Site.me().setRetryTimes(maxretry);
				site.addHeader("Cookie", CookiesHolder.INSTANCE.getCookie());
				site.addHeader("Accept", "'text/html, application/xhtml+xml, */*");
				site.addHeader("Accept-Language", "zh-CN");
				site.addHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
				site.addHeader("Connection", "Keep-Alive");
				site.addHeader("Accept-Encoding", "gzip,deflate");
				return site;
			}
		});
		ResultItems resultItems = spider.<ResultItems>get("https://www.zhihu.com/");
		if(StringUtils.isEmpty(resultItems.get("cookie")) || StringUtils.isEmpty(resultItems.get("xsrf"))) {
			return null;
		} else {
			return resultItems.get("cookie") + " _xsrf=" + resultItems.get("xsrf");
		}
	}
	
	private static void doRetry (Page page) {
		Object cycleTriedTimesObject = page.getRequest().getExtra(Request.CYCLE_TRIED_TIMES);
		if (cycleTriedTimesObject == null) {
		    page.addTargetRequest(page.getRequest().setPriority(0).putExtra(Request.CYCLE_TRIED_TIMES, 1));
		    page.getResultItems().setSkip(true);
		} else {
		    int cycleTriedTimes = (Integer) cycleTriedTimesObject;
		    cycleTriedTimes++;
		    if (cycleTriedTimes >= maxretry) {
		        page.putField("cookie", null);
		        page.putField("xsrf", null);
		    }
		}
	}
	
	public static String getHash() {
		if(CookiesHolder.INSTANCE != null) {
			return CookiesHolder.INSTANCE.getHash();
		} else {
			return null;
		}
	}
	
	public static String getName() {
		if(CookiesHolder.INSTANCE != null) {
			return CookiesHolder.INSTANCE.getName();
		} else {
			return null;
		}
	}
	
}
