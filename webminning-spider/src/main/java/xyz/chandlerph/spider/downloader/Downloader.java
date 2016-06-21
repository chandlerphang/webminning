package xyz.chandlerph.spider.downloader;

import xyz.chandlerph.spider.http.Request;

/**
 * 
 * @author Chandler Phang
 */
public interface Downloader {
	
	public String download(Request url) throws Exception;
	
}

