package xyz.chandlerph.spider.downloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.chandlerph.spider.downloader.Downloader;

class PhantomjsPool {
    private Logger logger;
    private static final int DEFAULT_CAPACITY = 5;
    private int capacity;
    protected static final int STAT_RUNNING = 1;
    protected static final int STAT_CLODED = 2;
    private AtomicInteger stat;
    private List<WebDriver> webDriverList;
    private BlockingDeque<WebDriver> innerQueue;

    public PhantomjsPool(int capacity) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.stat = new AtomicInteger(1);
        this.webDriverList = Collections.synchronizedList(new ArrayList<WebDriver>());
        this.innerQueue = new LinkedBlockingDeque<WebDriver>();
        this.capacity = capacity;
        
        try {
	        File f = new File ("./phantomjs.exe");
	        if (!f.exists()) {
	          InputStream in = Downloader.class.getClassLoader().getResourceAsStream("drivers/phantomjs.exe");
	          OutputStream out = new FileOutputStream("./phantomjs.exe");
	          IOUtils.copy(in, out);
	          out.flush();
	          out.close();
	        }
        } catch (Exception e) {
        	
        }
        
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; cs; rv:1.9.0.8) Gecko/2009032609 Firefox/3.0.8";
        System.setProperty("phantomjs.page.settings.userAgent", userAgent);
        System.setProperty("phantomjs.binary.path","./phantomjs.exe");
    }

    public PhantomjsPool() {
        this(DEFAULT_CAPACITY);
    }

    public WebDriver get() throws InterruptedException {
        this.checkRunning();
        WebDriver poll = this.innerQueue.poll();
        if(poll != null) {
            return poll;
        } else {
            if(this.webDriverList.size() < this.capacity) {
                synchronized(this.webDriverList) {
                    if(this.webDriverList.size() < this.capacity) {
                        PhantomJSDriver e = new PhantomJSDriver();
                        this.innerQueue.add(e);
                        this.webDriverList.add(e);
                    }
                }
            }

            return this.innerQueue.take();
        }
    }

    public void returnToPool(WebDriver webDriver) {
        this.checkRunning();
        this.innerQueue.add(webDriver);
    }

    protected void checkRunning() {
        if(!this.stat.compareAndSet(1, 1)) {
            throw new IllegalStateException("Already closed!");
        }
    }

    public void closeAll() {
        boolean b = this.stat.compareAndSet(1, 2);
        if(!b) {
            throw new IllegalStateException("Already closed!");
        } else {
            Iterator<WebDriver> i$ = this.webDriverList.iterator();

            while(i$.hasNext()) {
                WebDriver webDriver = i$.next();
                this.logger.info("Quit webDriver" + webDriver);
                webDriver.quit();
            }
        }
        
        File f = new File ("./phantomjs.exe");
        if (f.exists()) {
        	f.delete();
        }
    }
}

