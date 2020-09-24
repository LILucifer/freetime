package com.weixiao.smart.basedatatype.responsibilitychain;


import java.util.Iterator;
import java.util.Random;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-22 22:50.
 */
public class Mail {
    enum Readability {
        CAN_GET_READ,YES,NO1,NO2,NO3
    }

    enum Address {
        GUANGZHOU,G1,G2,G3,NO
    }

    enum Scanability {
        GOODIDEA,NO1,NO2,NO3
    }

    enum GetAddress {
        ADDRESS_RETURN,NO1,NO2
    }

    Readability readability;
    Address address;
    Scanability scanability;
    GetAddress getAddress;


    public static Mail randomMails(){
        Mail mail = new Mail();
        mail.readability = random(Readability.class);
        mail.address = random(Address.class);
        mail.scanability = random(Scanability.class);
        mail.getAddress = random(GetAddress.class);
        return mail;
    }

    private static <T extends Enum<T>> T random(Class<T > clas) {
        int pick = new Random().nextInt(clas.getEnumConstants().length);
        return clas.getEnumConstants()[pick];
    }


    public static Iterable<Mail> generator(int count) {

        Iterable<Mail> iterable = new Iterable<Mail>() {
            int n = count;
            @Override
            public Iterator<Mail> iterator() {

                return new Iterator<Mail>() {
                    @Override
                    public boolean hasNext() {
                        return n >0;
                    }

                    @Override
                    public Mail next() {
                        if (hasNext()) {
                            n--;
                            return Mail.randomMails();
                        }
                        return null;
                    }
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        return iterable;
    }






}
