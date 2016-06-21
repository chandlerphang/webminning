package xyz.chandlerph.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import xyz.chandlerph.spider.downloader.impl.PhantomjsDownloader;
import xyz.chandlerph.spider.http.Request;
import xyz.chandlerph.spider.parser.TextExtractor;
import xyz.chandlerph.spider.scheduler.Scheduler;
import xyz.chandlerph.spider.scheduler.impl.SimpleScheduler;

/**
 * 
 * @author Chandler Phang
 */
public class Test {
	
	public static void main(String[] args) throws Exception {
		
		Scheduler scheduler = new SimpleScheduler();
		PhantomjsDownloader downloader = new PhantomjsDownloader();
		
		downloader.init();
		
		scheduler.add("http://news.sohu.com/20160620/n455295889.shtml?qq-pf-to=pcqq.c2c");
		scheduler.add("http://news.sohu.com/20160621/n455550241.shtml");
		scheduler.add("http://news.sohu.com/20160621/n455483279.shtml");
		scheduler.add("http://news.sohu.com/20160621/n455551655.shtml");
		
		String url;
		TextExtractor extractor = new TextExtractor();
		while((url = scheduler.get()) != null) {
			String html = downloader.download(new Request(url));
			Document doc = Jsoup.parse(html);
			String text = doc.select("#contentText").first().html();
			text = extractor.preProcess(text);
			String title = doc.select("h1[itemprop=headline]").first().text();
			
			System.out.println("============规则==============");
			System.out.println(title);
			System.out.println(text);
			System.out.println("==============================");
			
			System.out.println("==========正文抽取=============");
			extractor.extractHTML(html);
			System.out.println(extractor.getTitle());
			System.out.println(extractor.getText());
			System.out.println("===============================");
			
			
			String participationNumber = doc.select("em[node-type=participation-number]").first().text();
			String commentNumber = doc.select("em[node-type=comment-number]").first().text();
			System.out.println(participationNumber + "人参与，" + commentNumber + "条评论" );
		}
		
		downloader.close();
	}
	
}
