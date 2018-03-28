/**
 * Created by eefijjt on 3/27/2018.
 */
public class StringDemo {

    public static void main(String[] args){
        String str1 = "java";
        String str2 = "java";
        System.out.println(str1==str2);

        String str11 = new String("java");
        String str22 = new String("java");
        System.out.println(str11==str22);

        String str111 = "javablog";
        String str222 = "java"+"blog";    //在编译时被优化成String str2 = "javablog";
        System.out.println(str111 == str222);  //true

        String s1 = "java";
        String s2 = new String("java");
        System.out.println(s1.intern() == s2.intern());
    }
}
