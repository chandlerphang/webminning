package xyz.chandlerph.webminning.spider;

/**
 * 
 * @author Chandler Phang
 */
public class Spider {
	
	public static void main(String args[]) {
		String fullcookies = CookiesHelper.getCookie();
		if(fullcookies == null) {
            return;
		}
		String xsrf = fullcookies.substring(fullcookies.indexOf("_xsrf") + 6);
	}
	
}
