package com.weixiao.smart.test.sychronized;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-18 20:04.
 */
public class PrintString  implements Runnable{
    private boolean isContentPrint = true;

    public boolean isContentPrint() {
        return isContentPrint;
    }

    public void setContentPrint(boolean contentPrint) {
        isContentPrint = contentPrint;
    }

    public void print(){
        try {
            while (isContentPrint == true) {
                System.out.println("this is PrintMethod threadName = " + Thread.currentThread().getName());
                Thread.sleep(1000);
            }
            System.out.println("线程"+Thread.currentThread().getName()+"被停止了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        print();
    }
}
