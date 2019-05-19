import org.apache.commons.collections.list.SynchronizedList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-04-20 23:36.
 */
public class Team {
    static  class Tem {
        private  int data = 5;
        public void bump(int bum){
            int data = 3;
            System.out.println(data);
            bum++;
            data = data + bum;
            data = ~data;
        }

    }
    public static void main(String[] args) {
//        meth(args);
//
//        Tem tm = new Tem();
//        int data = 2;
//        tm.bump(2);
//        System.out.println(tm.data +" "+ data);
//        new Team();
        int i = 2 ;
        int j = 0;
        j = (i++ + i++) * --i;
        System.out.println(j);
        int k = 2;
        System.out.println((k++ + k++) );

    }


    //    private Team(){
//
//    }
    public static void meth(String[] args) {
        System.out.println(args);
        System.out.println(args[0]);
        File file = new File("");
        File file1 = new File("");
        file1.delete();
        file.renameTo(new File(""));
        file.delete();

        int i = 0;
        if (i >= 0) ;
            if(i==0);
            System.out.println("1");
        //else System.out.println("2");
        Integer k = 10;
        List list = new ArrayList();
        SynchronizedList.decorate(list);
        Collections.synchronizedList(list);
    }

}
