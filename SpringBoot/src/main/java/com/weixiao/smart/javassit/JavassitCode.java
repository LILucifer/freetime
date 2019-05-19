package com.weixiao.smart.javassit;

import com.weixiao.smart.javassit.entity.StudentModel;
import javassist.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lishixiang0925@126.com.
 * @description (这里用一句话描述这个类的作用)
 * @Created 2019-05-09 14:41.
 */
@Slf4j
public class JavassitCode {
    public static void dynamicCodeStudent() {
//        StudentModel studentModel = new StudentModel();
//        studentModel.setAge(4);
//        studentModel.setStudentName("TOM");
        try {
            ClassPool pool = ClassPool.getDefault();
            //Loader cl = new Loader(pool);
            CtClass cc = pool.get("com.weixiao.smart.javassit.entity.StudentModel");
            //cc.setSuperclass(pool.get("test.Point"));
            //CtMethod ctMethod = cc.getDeclaredMethod("getStudentNo");
            //CtMethod ctMethod2 = new CtMethod();
            //cc.addMethod();
            CtNewMethod.make("public void setStudentNo(String studentNo) {this.studentNo = studentNo;}",cc);
            cc.addField(new CtField(pool.get("java.lang.String"),"weight" , cc),CtField.Initializer.constant("283kg"));
            CtField f = CtField.make("public String weight2 = \"sdsd\";", cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f);
            //cc.writeFile(".");// update the class file

            Class<?> me = cc.toClass();
            Object obj = me.newInstance();
            //System.out.println(StudentModel.class.getField("weight2").getName());

//            StudentModel studentMode2 = new StudentModel();
            log.info("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        dynamicCodeStudent();
    }
}
