package xyz.chandlerph.spider.scheduler.impl;

import java.util.LinkedList;
import java.util.Queue;

import xyz.chandlerph.spider.scheduler.Scheduler;

/**
 * 
 * @author Chandler Phang
 */
public class SimpleScheduler implements Scheduler {
	
	private Queue<String> urls = new LinkedList<>();

	@Override
	public void add(String url) {
		urls.offer(url);
	}

	@Override
	public String get() {
		return urls.poll();
	}

}
