package xyz.chandlerph.spider.scheduler;

/**
 * 
 * @author Chandler Phang
 */
public interface Scheduler {
	
	public void add(String url);
	
	public String get();

}
