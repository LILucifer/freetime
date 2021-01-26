/**
 * @author lishixiang
 * @Title:
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020/7/23 21:06
 */
public class _sjjdj {

    public static void main(String[] args) {
        testMain();
    }

    public static void test1(){
        int a = 10 ,b  , c  = 10 , d ;
        b = ++a;
        d = c++;

        System.out.println("b=" + b + "d=" + d);

        int age = 0;
        int age1 = age++;
        {
            age1 = age ;// age = 0 age1 =0
            age = age +1;//age = 1
        }
        System.out.println("age1 = " + age1);
        System.out.println("age = "+age );
        age = 0;
        int age2 = ++age;
        {
            age = age +1 ; // age = 1
            age2 = age ; //age2 = 1
        }
        System.out.println("age2 = " +age2);
        System.out.println("age = "+age );


        System.out.println(age == age1);
        System.out.println(age !=age1); //不等于

    }

    public static void testMain(){
        int a = 10, b = 2, c = 4, d = 19 , e;
        e = a < b ? c : d;
        System.out.println(e);

    }
}
