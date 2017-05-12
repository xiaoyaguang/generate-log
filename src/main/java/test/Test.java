package test;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by yaguang.xiao on 2016/11/18.
 */
public class Test {

    public static void main(String[] args) {
        //        Method[] methods = Test.class.getMethods();
        //        for (Method method : methods) {
        //            System.out.println("method:" + method.getName());
        //            for (Parameter parameter : method.getParameters()) {
        //                System.out.println(parameter.getName());
        //            }
        //        }

        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage(Test.class.getPackage().getName()))
                .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(GM.class);
        for (Method method : methods) {
            System.out.println(method.getDeclaringClass().getName());
        }
    }

    private @interface GM {

    }

    @GM
    public static void test(String aa1, String ss2) {

    }
}
