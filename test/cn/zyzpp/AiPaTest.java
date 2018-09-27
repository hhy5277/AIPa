package cn.zyzpp;

import cn.zyzpp.executor.AiPaExecutor;
import cn.zyzpp.worker.AiPaWorker;
import org.jsoup.nodes.Document;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AiPaTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException, InstantiationException, IllegalAccessException {
        new AiPaTest().test1();
        new AiPaTest().test2();
    }

    public void test1() throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException {
        //准备网址集合
        List<String> linkList = new ArrayList<>();
        linkList.add("http://jb39.com/jibing/FeiQiZhong265988.htm");
        linkList.add("http://jb39.com/jibing/XiaoErGuoDu262953.htm");
        linkList.add("http://jb39.com/jibing/XinShengErShiFei250995.htm");
        linkList.add("http://jb39.com/jibing/GaoYuanFeiShuiZhong260310.htm");
        linkList.add("http://jb39.com/zhengzhuang/LuoYin337449.htm");
        //第一步：新建爱爬
        AiPaExecutor aiPaExecutor = AiPa.newInstance(new AiPaWorker() {
            @Override
            public String run(Document doc) {
                //使用JSOUP进行HTML解析
                return doc.title() + doc.body().text();
            }

            @Override
            public Boolean fail(String link) {
                //任务执行失败
                //可以记录失败网址
                //记录日志
                return false;
            }
        }).setCharset(Charset.forName("GBK"));
        //第二步：提交任务
        for (int i = 0; i < 10; i++) {
            aiPaExecutor.submit(linkList);
        }
        //第三步：读取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();
        for (int i = 0; i < futureList.size(); i++) {
            //get() 方法会阻塞当前线程直到获取返回值
            System.out.println(futureList.get(i).get());
        }
        //第四步：关闭线程池
        aiPaExecutor.shutdown();
    }

    public void test2() throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException {
        //准备网址集合
        List<String> linkList = new ArrayList<>();
        linkList.add("http://jb39.com/jibing/FeiQiZhong265988.htm");
        linkList.add("http://jb39.com/jibing/XiaoErGuoDu262953.htm");
        linkList.add("http://jb39.com/jibing/XinShengErShiFei250995.htm");
        linkList.add("http://jb39.com/jibing/GaoYuanFeiShuiZhong260310.htm");
        linkList.add("http://jb39.com/zhengzhuang/LuoYin337449.htm");
        //第一步：新建爱爬,传入任务方法
        AiPaExecutor aiPaExecutor = AiPa.newInstance(new MyAiPaWorker());
        //第二步：提交任务,这里自定义爬虫类。
        for (int i = 0; i < 10; i++) {
            aiPaExecutor.submit(linkList, MyCallable.class);
        }
        //第三步：读取返回值
        List<Future> futureList = aiPaExecutor.getFutureList();
        for (int i = 0; i < futureList.size(); i++) {
            //get() 方法会阻塞当前线程直到获取返回值
            System.out.println(futureList.get(i).get());
        }
        //第四步：关闭线程池
        aiPaExecutor.shutdown();
    }

}
