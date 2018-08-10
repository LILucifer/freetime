package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-21 11:18.
 */
public class DBtools {

    volatile private boolean backA = false ;
    synchronized public void  backupToA(){
        try {
            while (backA == true) {
                this.wait();
            }
            for(int i = 0 ;i<5;i++) {
                System.out.println("★★★★★★★★");
            }
            backA = true;
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    synchronized public void  backupToB(){
        try {
            while (backA == false) {
                this.wait();
            }
            for(int i = 0 ;i<5;i++) {
                System.out.println("☆☆☆☆☆☆☆☆");
            }
            backA = false;
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
