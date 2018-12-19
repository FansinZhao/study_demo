package com.fansin.reflect.dynamic;

import java.io.*;

/**
 * Created by zhaofeng on 17-4-3.
 */
public class MyClassloader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = new byte[1024];

        File f = new File(name);
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bis.read(b);
            bos.write(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defineClass("Hello", b, 0, b.length);
    }
}
