package xyz.chandlerph.webminning.spider;

import xyz.chandlerph.webminning.spider.service.UserBaseInfoService;
import xyz.chandlerph.webminning.spider.service.UserBaseInfoServiceImpl;

/**
 * 
 * @author Chandler Phang
 */
public class Spider {
	
	public static void main(String args[]) {
		UserBaseInfoService service = new UserBaseInfoServiceImpl();
		int account = service.getBaseUsersAccount();
		
		System.out.println(account);
	}
	
}
