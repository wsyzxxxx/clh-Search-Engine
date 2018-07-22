package com.searcher.searcher;

import java.io.*;
import java.lang.reflect.Field;

public class test {

    public static  void f(aa a){
       int b= ((bb)a).b;
    }
    public static void main(String arg[]) throws IOException {
        File file=new File("1.txt");
        OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file));
        writer.write("123");
    writer.flush();
    }

}

class aa{
   public int a;
}
class bb extends aa{
   public int b;
}
