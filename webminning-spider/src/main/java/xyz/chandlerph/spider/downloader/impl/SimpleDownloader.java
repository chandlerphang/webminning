package xyz.chandlerph.spider.downloader.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import xyz.chandlerph.spider.downloader.Downloader;
import xyz.chandlerph.spider.http.CharsetDetector;
import xyz.chandlerph.spider.http.Request;

/**
 * 
 * @author Chandler Phang
 */
public class SimpleDownloader implements Downloader {

	@Override
	public String download(Request req) throws Exception {
		URL url = new URL(req.getUrl());
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		
		if("GET".equals(req.getMethod())) {
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.setDoInput(true);
		} else if("POST".equals(req.getMethod())) {
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
		} else {
			throw new Exception("not support http method: " + req.getMethod());
		}
		
		// 设置header
		for(Map.Entry<String, List<String>> entry : req.getHeaders().entrySet()) {
            String key = entry.getKey();
            List<String> valueList = entry.getValue();
            for (String value : valueList) {
                con.addRequestProperty(key, value);
            }
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[2048];
		InputStream is = con.getInputStream();
		int read;
		while((read = is.read(buf)) != -1) {
			baos.write(buf, 0, read);
		}
		baos.flush();
		byte[] cntbyte = baos.toByteArray();
		
		is.close();
		baos.close();
		return new String(cntbyte, CharsetDetector.guessEncoding(cntbyte));
	}
	
}
