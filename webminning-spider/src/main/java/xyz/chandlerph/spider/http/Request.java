package xyz.chandlerph.spider.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.chandlerph.spider.Constants.*;

/**
 * 
 * @author Chandler Phang
 */
public class Request {

	protected String method = DEFAULT_HTTP_METHOD;

	protected Map<String, List<String>> headers = new HashMap<>();

	protected String url;

	public Request(String url) {
		this.url = url;
	    setUserAgent(DEFAULT_USER_AGENT);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setUserAgent(String userAgent) {
		setHeader("User-Agent", userAgent);
	}

	public void setCookie(String cookie) {
		setHeader("Cookie", cookie);
	}

	public void addHeader(String key, String value) {
		if (key == null) {
			throw new NullPointerException("key is null");
		}
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		
		List<String> valueList = headers.get(key);
		if (valueList == null) {
			valueList = new ArrayList<String>();
			headers.put(key, valueList);
		}
		valueList.add(value);
	}

	public void removeHeader(String key) {
		if (key == null) {
			throw new NullPointerException("key is null");
		}

		if (headers != null) {
			headers.remove(key);
		}
	}

	public void setHeader(String key, String value) {
		if (key == null) {
			throw new NullPointerException("key is null");
		}
		if (value == null) {
			throw new NullPointerException("value is null");
		}
		
		List<String> valueList = new ArrayList<String>();
		valueList.add(value);
		headers.put(key, valueList);
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public List<String> getHeader(String key) {
		if (headers == null) {
			return null;
		}
		return headers.get(key);
	}

	public String getFirstHeader(String key) {
		if (headers == null) {
			return null;
		}
		List<String> valueList = headers.get(key);
		if (valueList.size() > 0) {
			return valueList.get(0);
		}
		else {
			return null;
		}
	}

}
