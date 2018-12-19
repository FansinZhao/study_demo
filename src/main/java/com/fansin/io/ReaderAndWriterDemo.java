package com.fansin.io;

import java.io.*;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaofeng on 17-4-25.
 */
public class ReaderAndWriterDemo {

    public static void main(String[] args) {
        System.out.println("------------OutputStreamWriter/InputStreamReader 或 FileWriter/FileReader--------------");
        String outWriter = "outWriter";

        try (
                /*
                FileOutputStream fileOutputStream = new FileOutputStream(outWriter);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)
                */
                FileWriter outputStreamWriter = new FileWriter(outWriter)
        ) {

            System.out.println("FileWriter 简化代码量 等价 \n" +
                               "FileOutputStream fileOutputStream = new FileOutputStream(outWriter);\n" +
                               "OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream)");

            System.out.println("OutputStreamWriter 字符集:" + outputStreamWriter.getEncoding());
            outputStreamWriter.write('a');
            outputStreamWriter.write('\n');
            outputStreamWriter.write("a中文bc\n".toCharArray());
            outputStreamWriter.write("这是一条中文信息!");
            outputStreamWriter.flush();//手动刷新
            System.out.println("写入字符文件成功!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
              /*
                FileInputStream fileInputStream = new FileInputStream(outWriter);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream)
                */
                FileReader inputStreamReader = new FileReader(outWriter)
        ) {
            System.out.println("FileReader 简化代码量 等价\n" +
                               "FileInputStream fileInputStream = new FileInputStream(outWriter);\n" +
                               "InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream)");
            System.out.println("char[] 可以取出任意一个字符,包括中文字符");
            System.out.println("InputStreamReader 字符集:" + inputStreamReader.getEncoding());
            System.out.println("字节流解码器是否准备好了?" + inputStreamReader.ready());
            char[] chars = new char[1024];
            System.out.println("打印字符!");
            while (inputStreamReader.read(chars) != -1) {
                System.out.println(new String(chars));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("------------BufferedWriter/BufferedReader  --------------");
        String bufferFile = "bufferWriter";

        try (
                FileWriter fileWriter = new FileWriter(bufferFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            System.out.println("BufferedWriter写入文件,添加换行功能!");
//            System.out.println("获取系统换行符:[" + AccessController.doPrivileged(
//                    new GetPropertyAction("line.separator")) + "]");
//            System.out.println("获取系统换行符:"+ AccessController.doPrivileged(new GetPropertyAction("line.separator")));
            System.out.println("获取系统换行符:" + System.getProperty("line.separator"));
            System.out.println("添加新的方法,添加换行");
            bufferedWriter.write('中');
            bufferedWriter.newLine();
            bufferedWriter.write("中a英b文c字符!");
            bufferedWriter.newLine();
            bufferedWriter.write("中a英b文c字符数组!".toCharArray());
            bufferedWriter.newLine();
            bufferedWriter.flush();//手动刷新,close会刷新
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileReader fileReader = new FileReader(bufferFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

        ) {
            System.out.println("BufferedReader 并没有继承InputStream 是否支持mark:" + bufferedReader.markSupported());
            System.out.println("reader是否准备好:" + bufferedReader.ready());
            System.out.println("mark(1),参数生效,最小为1");
            bufferedReader.mark(1);
            System.out.println("常规读取!");
            char[] chars = new char[10];
            int length = bufferedReader.read(chars);
            System.out.println(new String(chars) + " 行数:" + length);
            bufferedReader.reset();
            System.out.println("新功能读取一行,readLine");
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------LineNumberReader-------------------");

        try (
                FileReader fileReader = new FileReader(bufferFile);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

        ) {
            System.out.println("LineNumberReader 并没有继承InputStream 是否支持mark:" + lineNumberReader.markSupported());
            System.out.println("reader是否准备好:" + lineNumberReader.ready());
            System.out.println("mark(1),参数生效,最小为1");
            lineNumberReader.mark(1);
            System.out.println("显示linenumber");
            while (true) {
                System.out.println("当前行号:" + lineNumberReader.getLineNumber());
                String line = lineNumberReader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                System.out.println(lineNumberReader.getLineNumber() + " " + line);
                if (lineNumberReader.getLineNumber() == 2) {
                    lineNumberReader.setLineNumber(10);
                    System.out.println("设置当前行号为10,后续的行号将以此为累加");
                    System.out.println(lineNumberReader.getLineNumber());
                }
            }
            System.out.println("常规读取!");
            char[] chars = new char[10];
            int length = lineNumberReader.read(chars);
            System.out.println(new String(chars) + " 行数:" + length);
            lineNumberReader.reset();
            System.out.println("新功能读取一行,readLine");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("------------CharArrayWriter/CharArrayReader  --------------");
        String charArrayFile = "charArrayWriter";
        try (
                FileWriter fileWriter = new FileWriter(charArrayFile);
                CharArrayWriter charArrayWriter = new CharArrayWriter();

        ) {
            System.out.println("默认大小为32");
            charArrayWriter.write("中文字符串aaa");
            charArrayWriter.write("cccc".toCharArray());
            charArrayWriter.append('b').append("追加");
            charArrayWriter.writeTo(fileWriter);
            System.out.println("目前在缓存中:" + new String(charArrayWriter.toCharArray()) + " " + charArrayWriter.size());
            System.out.println("目前在缓存中:" + charArrayWriter.toString());
            System.out.println("flush 不生效,因为没有地方可以输出");
            charArrayWriter.flush();
            System.out.println("reset 生效,数组大小变为0");
            charArrayWriter.reset();
            System.out.println("目前在缓存中:" + new String(charArrayWriter.toCharArray()) + " " + charArrayWriter.size());
            System.out.println("目前在缓存中:" + charArrayWriter.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                CharArrayReader charArrayReader = new CharArrayReader("中应为回答ssddf222".toCharArray())
        ) {
            System.out.println("是否支持mark:" + charArrayReader.markSupported());
            System.out.println("流是否准备好?" + charArrayReader.ready());
            System.out.println("mark参数无效");
            charArrayReader.mark(0);
            char[] chars = new char[1024];
            int length = charArrayReader.read(chars);
            System.out.println("读取内容:" + new String(chars) + " " + length);
            charArrayReader.reset();
            chars = new char[1024];
            length = charArrayReader.read(chars);
            System.out.println("重复读取读取内容:" + new String(chars) + " " + length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-----------PushbackReader  --------------");
        try (PushbackReader pushbackReader = new PushbackReader(new CharArrayReader("呵呵,(ab)1111".toCharArray()))
        ) {
            System.out.println("支持mark/reset?" + pushbackReader.markSupported());
            try {
                pushbackReader.mark(0);
                pushbackReader.reset();
            } catch (IOException e) {
                System.out.println("强制使用会报异常!");
            }
            char chars[] = new char[1];
            while (true) {
                int c = pushbackReader.read(chars);
                if (c == -1) {
                    break;
                }
                char tmp = chars[0];
                if (tmp == 'a') {
                    System.out.println("推回必须是本次读取的内容,不能修改后推回!");
                    pushbackReader.unread(chars);
                    char cs[] = new char[2];
                    pushbackReader.read(cs);
                    System.out.println("读取一对:" + new String(cs));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------StringWriter/StringReader----------------");
        try (StringWriter stringWriter = new StringWriter()) {
            System.out.println("可以像使用StringBuffer一样使用");
            stringWriter.append("aaa").append("|bbbb");
            stringWriter.write("也可以像writer一样使用");
            System.out.println("buffer类型" + stringWriter.getBuffer().getClass().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (StringReader stringReader = new StringReader("一条消息!")) {
            System.out.println("支持mark/reset:" + stringReader.markSupported());
            System.out.println("检查字节流是否正常:" + stringReader.ready());
            stringReader.mark(0);
            char chars[] = new char[1];
            while (stringReader.read(chars) != -1) {
                System.out.println(new String(chars));
            }
            stringReader.reset();
            System.out.println("reset后还可以重头在读取");
            while (stringReader.read(chars) != -1) {
                System.out.println(new String(chars));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("--------------Writer独有 PrintWriter-----------------");
        try (
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter, true)) {
            System.out.println("除了可以直接使用字符外,基本等同PrintWriter");
            printWriter.format("%1$TY-%1$Tm-%1$Td %1$tH:%1$tM:%1$tS", new Date());
            printWriter.printf("%1$tF %1$tR", new Date());
            printWriter.println("字符串");
            printWriter.println(true);
            printWriter.println('a');
            printWriter.println(11111);
            printWriter.println(11.111);
            printWriter.append("只能追加字符串");
            System.out.println("有无异常:" + printWriter.checkError());
            System.out.println("写入内容:" + stringWriter.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------PipedReader/PipedWriter----------------");
        System.out.println("同PipedInputStream/PipedOutputStream");
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader();
//        ExecutorService service = Executors.newFixedThreadPool(2);
        ThreadPoolExecutor service = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
                                                            new ArrayBlockingQueue<>(2));
        try {
            service.execute(new Thread(new Receiver(pipedReader, pipedWriter)));
            service.execute(new Thread(new Sender(pipedWriter)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.shutdown();

    }

    static class Sender implements Runnable {

        private PipedWriter pipedWriter;

        public Sender(PipedWriter pipedWriter) {
            this.pipedWriter = pipedWriter;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println("写入消息>>");
                String msg = String.format("当前日期:%1$tF %1$tT", new Date());
                try {
                    pipedWriter.write(msg);
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Receiver implements Runnable {

        private PipedReader pipedReader;

        public Receiver(PipedReader pipedReader, PipedWriter pipedWriter) throws IOException {
            this.pipedReader = pipedReader;
            pipedWriter.connect(pipedReader);
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         */
        @Override
        public void run() {
            while (true) {
                char chars[] = new char[1024];
                try {
                    int length = pipedReader.read(chars);
                    if (length == -1) {
                        Thread.sleep(1000L);
                    }
                    System.out.println("接收到消息:" + new String(chars) + " " + length);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
