package com.weixiao.smart.test.waitAndNotify;

/**
 * @author lishixiang@echinetech.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-04-21 11:26.
 */
public class BackupB extends Thread {
    private DBtools dBtools;

    public BackupB(DBtools dBtools) {
        super();
        this.dBtools = dBtools;
    }
    @Override
    public void run() {
        dBtools.backupToB();
    }
}
