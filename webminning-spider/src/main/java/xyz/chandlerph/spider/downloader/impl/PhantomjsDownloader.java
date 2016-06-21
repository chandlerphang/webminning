package xyz.chandlerph.spider.downloader.impl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import xyz.chandlerph.spider.downloader.Downloader;
import xyz.chandlerph.spider.http.Request;

/**
 * 
 * @author Chandler Phang
 */
public class PhantomjsDownloader implements Downloader {
	
	private PhantomjsPool pool;
	
	public void init() {
		pool = new PhantomjsPool();
	}
	
	public void close() {
		pool.closeAll();
	}

	public String download(Request req) throws Exception {
		WebDriver webDriver = pool.get();
		webDriver.get(req.getUrl());
		try {
			Thread.sleep((long) 2 * 1000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement htmlEl = webDriver.findElement(By.xpath("/html"));
		String html =  htmlEl.getAttribute("outerHTML");
		
		pool.returnToPool(webDriver);
		return html;
	}

}
