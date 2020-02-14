package com.cqust.crawler_webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

public class JobProcessor implements PageProcessor {
    /**
     * 解析页面
     * @param page
     */
    public void process(Page page) {
        //解析page，把解析结果放入resultItems
        //解析数据方式1 xpath
        page.putField("div0 text()",page.getHtml().xpath("//div[@id=service-2017]/div/ol/li/text()").all());
        page.putField("div1",page.getHtml().xpath("//div[@id=service-2017]/div/ol/li").all());
        //解析数据方式2 css
        page.putField("div2",page.getHtml().css("div#service-2017 i").all());
        //解析数据方式3 正则表达式
        page.putField("div3含北字",page.getHtml().css("div.items a").regex(".*北.*").all());
        //c处理结果api
        page.putField("div4含北字",page.getHtml().css("div.items a").regex(".*北.*").get());
        page.putField("div5含北字",page.getHtml().css("div.items a").regex(".*北.*").toString());
        //获取链接
       /* page.addTargetRequests(page.getHtml().css("a").links().regex(".*999$").all());
        page.putField("时尚",page.getHtml().xpath("//div[@id=categorys-2014]/div/text()").all());*/
        page.addTargetRequest("https://list.jd.com/list.html?cat=1315,1343,11999");
        page.addTargetRequest("https://list.jd.com/list.html?cat=1315,1343,11999");
        page.addTargetRequest("https://list.jd.com/list.html?cat=1315,1343,11999");
    }
    private Site site = Site.me()
            .setCharset("utf8")
            .setTimeOut(10*1000)
            .setRetrySleepTime(3000)
            .setRetryTimes(3)
            ;
    public Site getSite() {
        return site;
    }
    //主函数执行爬虫,去重 默认Hash方法。
    public static void main(String[] args) {
        Spider spider = Spider.create(new JobProcessor())
                .addUrl("https://www.jd.com/allSort.aspx")
                //.addPipeline(new FilePipeline("C:\\Users\\Mr.c\\Desktop\\pipline"))
                //采用boolmFilterduplicateRemove方法+依赖包 com.google.guava guava去重
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000000)))//设置url最大数字
                .thread(5);
        Scheduler scheduler = spider.getScheduler();
        //执行爬虫
        spider.run();
    }
}
