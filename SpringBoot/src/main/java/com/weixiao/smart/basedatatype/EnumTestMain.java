package com.weixiao.smart.basedatatype;

import com.weixiao.smart.algorithm.Hanota;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2020-09-20 22:22.
 */
@Slf4j
public class EnumTestMain {


    public enum DataTypeEnum {
        DOUBLE,FLOAT , LIST,STRING

    }

    public interface Food {
        String foodName();
    }


    public enum BananaEnum implements Food {
        BANANA_ENUM
        ;

        @Override
        public String foodName() {
            return "this is banana";
        }
    }

    public static void main(String[] args ) {


        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {


            log.info("--------------- {}" , dataTypeEnum);
            log.info("hashCode = {}" , dataTypeEnum.hashCode());
            log.info("ordinal = {}", dataTypeEnum.ordinal());
            log.info("DeclaringClass = {}", dataTypeEnum.getDeclaringClass());


            String[] temo = {"temo", "sdasd"};
            EnumSet enumSet = EnumSet.allOf(DataTypeEnum.class);
            //FIXME 以下两个 日子输出中 array 类型输出有问题   why???????
            log.info(" values = {}",enumSet.toArray());//values = DOUBLE
            log.info(" values = {} , {}",temo , enumSet.toArray());//values = [temo, sdasd] , [DOUBLE, FLOAT, LIST, STRING]

        }
        Food banana = BananaEnum.BANANA_ENUM;
        EnumSet enumSet = EnumSet.allOf(DataTypeEnum.class);

        log.info("size = {} stream  = {}  values = {}", enumSet.size() , enumSet.stream(),enumSet.toArray());
    }

    public static <T extends Enum<T>> T random(Class<T> ec) {
        ec.getEnumConstants();
        return null;
    }

}
