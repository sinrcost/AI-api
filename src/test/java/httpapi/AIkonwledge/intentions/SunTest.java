package httpapi.AIkonwledge.intentions;

import org.testng.annotations.Test;

public class SunTest {

    @Test
    public void test1(){
        System.out.println("test1");
    }

    @Test(dependsOnMethods = "test1")
    public void test2(){
        System.out.println("test2");
    }

    @Test(dependsOnMethods = "test1")
    public void test3(){
        System.out.println("test3");
    }

    @Test(dependsOnMethods = "test3")
    public void test4(){
        System.out.println("test4");
    }

}
