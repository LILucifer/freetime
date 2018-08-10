import java.util.Date;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2018-07-05 23:24.
 */
public class WorkerInterfaceTest {
    static void doSomeWork(WorkerInterface worker){
        worker.doSomeWork(new Date());
    }

    public static void main(String[] args) {
        doSomeWork((date -> {
            System.out.println(date);
        }));

        doSomeWork(new WorkerInterface() {
            @Override
            public void doSomeWork(Date date) {
                System.out.println(date);
            }
        });
    }
}
