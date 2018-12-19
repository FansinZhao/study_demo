package com.fansin.io;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.*;
import java.util.zip.*;

/**
 * Created by zhaofeng on 17-4-25.
 */
public class InputStreamAndOutputStreamDemo {

    public static void main(String[] args) {
        String str = "aaa bbb ccc ddd";
        byte[] bytes = str.getBytes();
        byte iBytes[];//可变数组

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.out.println("ByteArrayOutputStream-默认大小为32,扩容为2倍");
        try {
            byteArrayOutputStream.close();
            System.out.println("ByteArrayOutputStream-close无效");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-------ByteArrayInputStream/ByteArrayOutputStream-------");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            byteArrayInputStream.close();
            System.out.println("1 close 不影响后续使用");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("2 是否支持mark:" + byteArrayInputStream.markSupported());
        byteArrayInputStream.mark(0);//最开始mark
        System.out.println("mark参数无效!mark=pos");
        while (byteArrayInputStream.available() > 0) {
            System.out.println("available 表示剩余字节数量:" + byteArrayInputStream.available());
            int tmp = byteArrayInputStream.read();
            byteArrayOutputStream.write(tmp);//写入byte值
            System.out.println("字节:" + tmp);
        }

        byteArrayInputStream.reset();//
        System.out.println("reset将pos=mark");
        System.out.println("mark/reset一块使用");
        System.out.println("3 skip前4个");
        byteArrayInputStream.skip(4);
        iBytes = new byte[3];
        byteArrayInputStream.read(iBytes, 0, 3);
        System.out.println("从第5个字节获取:" + new String(iBytes));

        try {
            System.out.println("ByteArrayOutputStream-flush没有做任何事情");
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("ByteArrayOutputStream toString输出:" + byteArrayOutputStream.toString("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("ByteArrayOutputStream 相对available(),size:" + byteArrayOutputStream.size());
        byteArrayOutputStream.reset();
        System.out.println("ByteArrayOutputStream 清空所有:" + byteArrayOutputStream.size());

        System.out.println("-------FileInputStream/FileOutputStream-------");
        String fileName = "FileOutputStream.os";
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ) {
            //native创建文件
            fileOutputStream.write(str.getBytes());//文件此时已经写入文件之中
            fileOutputStream.flush();
            System.out.println("FileOutputStream-- flush无效");
            FileChannel fileChannel = fileOutputStream.getChannel();
            System.out.println("NIO-FileChannel- 文件位置" + fileChannel.position());
            System.out.println("NIO-FileChannel- 获取文件大小" + fileChannel.size());
            FileDescriptor fileDescriptor = fileOutputStream.getFD();
            System.out.println("FileDescriptor- valid" + fileDescriptor.valid());
            fileDescriptor.sync();
            System.out.println("强制缓冲区与基础设备同步!");
            System.out.println("FileOutputStream-- 会抛出checked异常!");
            fileOutputStream.close();
            System.out.println("使用close释放资源,通道");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            System.out.println("FileInputStream 读取文件!");
//            fileInputStream = new FileInputStream(fileName);
            System.out.println("文件大小" + fileInputStream.available());
            FileChannel fileChannel = fileInputStream.getChannel();
            System.out.println("NIO-FileChannel- 文件位置" + fileChannel.position());
            System.out.println("NIO-FileChannel- 获取文件大小" + fileChannel.size());
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            System.out.println("FileDescriptor- valid" + fileDescriptor.valid());
            fileDescriptor.sync();
            System.out.println("强制缓冲区与基础设备同步!");
            System.out.println("读取单个字节" + fileInputStream.read());
            System.out.println("支持mark?" + fileInputStream.markSupported());
            fileInputStream.skip(3);//已经读取了一个了
            iBytes = new byte[3];
            fileInputStream.read(iBytes);
            System.out.println("读取字节:" + new String(iBytes));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("------------FilterInputStream/FilterOutputStream结束---------------");
        System.out.println("-------FilterInputStream/FilterOutputStream-------");
        System.out.println("不允许直接实例化FilterInputStream/FilterOutputStream!");
        FilterOutputStream filterOutputStream = new FilterOutputStream(null);
        System.out.println("FilterOutputStream 可以使用null实例空流");
        System.out.println("BufferedInputStream");

        try (
                FileInputStream bInputStream = new FileInputStream(fileName);//重新打开流
                BufferedInputStream bufferedInputStream = new BufferedInputStream(bInputStream);
        ) {
            System.out.println("必须指定输入流!");
            bufferedInputStream.mark(0);
            System.out.println("读取单个字节" + bufferedInputStream.read());
            System.out.println("读取单个字节" + bufferedInputStream.read());
            System.out.println("读取单个字节" + bufferedInputStream.read());
            bufferedInputStream.reset();
            System.out.println("将不支持mark的流改变为可以支持mark");
            bufferedInputStream.read(iBytes, 0, 3);
            System.out.println("读取前3个" + new String(iBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("BufferedOutputStream");
        try (
                FileOutputStream bOutputStream = new FileOutputStream(fileName);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bOutputStream);
        ) {
            System.out.println("必须指定输出流!");
            bufferedOutputStream.write("aaa".getBytes());
            bufferedOutputStream.flush();
            System.out.println("使用flush,将缓冲数据写入文件!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------CheckedOutputStream/CheckedInputStream-------------------");
        String checkedStream = "checkStream.os";
        try (
                FileOutputStream cOutputStream = new FileOutputStream(checkedStream);//重新打开流
                CheckedOutputStream checkedOutputStream = new CheckedOutputStream(cOutputStream, new Adler32());
        ) {
            checkedOutputStream.write("123 456 789".getBytes());
            System.out.println("adler校验和:" + checkedOutputStream.getChecksum().getValue());
            System.out.println("close也是会将将缓冲数据写入文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (
                FileInputStream cInputStream = new FileInputStream(fileName);
                CheckedInputStream checkedInputStream = new CheckedInputStream(cInputStream, new Adler32());
        ) {
            iBytes = new byte[1024];
            checkedInputStream.read(iBytes);
            System.out.println("Alder校验和:" + checkedInputStream.getChecksum().getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------CheckedOutputStream/CheckedInputStream-------------------");
        String dataStream = "dataStream.os";
        try (
                FileOutputStream dOutputStream = new FileOutputStream(dataStream);//重新打开流
                DataOutputStream dataOutputStream = new DataOutputStream(dOutputStream);
        ) {
            System.out.println("CheckedOutputStream.........");
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeBoolean(false);
            dataOutputStream.writeChar('a');
            dataOutputStream.writeChars("sss");
            dataOutputStream.writeDouble(45678964566.2344d);
            dataOutputStream.writeFloat(0.2f);
            dataOutputStream.writeLong(122222l);
            dataOutputStream.writeInt(11111);
            dataOutputStream.writeShort(127);
            dataOutputStream.writeUTF("数据字节流");
            dataOutputStream.write("\na".getBytes());
            System.out.println("可以写入java数据类型,char chars byte short int long float double boolean utf8字符串");
            System.out.println("强大之处,可以添加重复类型的数据类型,读取时,按照写入顺序读出!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream dInputStream = new FileInputStream(dataStream);
             BufferedInputStream dbufferedInputStream = new BufferedInputStream(dInputStream);
             DataInputStream dataInputStream = new DataInputStream(dbufferedInputStream)
//             DataInputStream dataInputStream = new DataInputStream(dInputStream)
        ) {
            System.out.println("DataInputStream ............");
            dataInputStream.mark(0);
            iBytes = new byte[dataInputStream.available()];
            dataInputStream.readFully(iBytes);
            dataInputStream.reset();
            System.out.println("一次性全部读取:" + new String(iBytes));
            System.out.println("是否支持mark/reset取决于传入的输入流,FileInputStream不支持:" + dataInputStream.markSupported());
            System.out.println("读取时,要注意按照写入的顺序读取,尤其注意chars,读取时一次一个char,否则读取数据异常");
            System.out.println("从data流中读取第一个boolean:" + dataInputStream.readBoolean());
            System.out.println("从data流中读取第二个boolean:" + dataInputStream.readBoolean());
            System.out.println("从data流中读取一个char:" + dataInputStream.readChar());
            System.out.println("从data流中读取chars中第一个char:" + dataInputStream.readChar());
            System.out.println("从data流中读取chars中第二个char:" + dataInputStream.readChar());
            System.out.println("从data流中读取chars中第三个char:" + dataInputStream.readChar());
            System.out.println("从data流中读取double:" + dataInputStream.readDouble());
            System.out.println("从data流中读取float:" + dataInputStream.readFloat());
            System.out.println("从data流中读取long:" + dataInputStream.readLong());
            System.out.println("从data流中读取int:" + dataInputStream.readInt());
            System.out.println("从data流中读取short:" + dataInputStream.readShort());
            System.out.println("从data流中读取utf:" + dataInputStream.readUTF());
            System.out.println("从data流中读取\\n:" + dataInputStream.read());
            System.out.println("从data流中读取a:" + dataInputStream.read());
            System.out.println("从data流中读取结束  -1:" + dataInputStream.read());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------CheckedOutputStream/CheckedInputStream-------------------");
        System.out.println("Deflater/Inflater使用!");
        String string = "dsdfsfsdfdsfd";
        try {
            byte[] bs = string.getBytes("utf-8");
            System.out.println("压缩前长度:" + bs.length);
            byte[] out = new byte[100];
            Deflater deflater = new Deflater(1);
            deflater.setInput(bs);
            deflater.finish();
            int length = deflater.deflate(out);
            deflater.end();
            System.out.println("压缩后数组长度:" + length);

            Inflater inflater = new Inflater();
            inflater.setInput(out, 0, length);
            byte[] result = new byte[100];
            int ilength = inflater.inflate(result);
            deflater.end();
            System.out.println("解压后长度:" + ilength);
            System.out.println("加压后字符:" + new String(result, 0, length));
            System.out.println("针对大文件,压缩才能明显");
            System.out.println("--leve  0    1 2 3 4 5 6 7 8 9--------");
            System.out.println("--速度 不压缩 快快快...........慢");
            System.out.println("--大小 不压缩 大大大...........小");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

        System.out.println("第一组 压缩后存储再解压缩读取 DeflateOutStream/InflateInputStream");
        String defalter = "deflater.os";
        try (
                FileOutputStream deInputStream = new FileOutputStream(defalter);
                DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(deInputStream)
        ) {
            System.out.println("DeflaterOutputStream使用很简单,只有三个write,close->flush->finish,size都没有");
            System.out.println("write时调用deflater.deflate()");
            deflaterOutputStream.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(defalter);
                InflaterInputStream inflaterInputStream = new InflaterInputStream(fileInputStream);
        ) {
            System.out.println("InflaterInputStream 解压缩 DeflaterOutputStream");
            System.out.println("数据量无效:" + inflaterInputStream.available());
            iBytes = new byte[1024];
            int length = inflaterInputStream.read(iBytes);
            System.out.println("read时,调用inflater.inflate()");
            System.out.println("读取字符串长度:" + length);
            System.out.println("读取内容:" + new String(iBytes, "UTF-8"));
            System.out.println("!!!!非解压缩读取,无法读取!!!!");
            iBytes = new byte[1024];
            length = fileInputStream.read(iBytes);
            System.out.println("读取字符串长度:" + length);
            System.out.println("读取内容:" + new String(iBytes, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("第二组 压缩读取然后解压缩存储 DeflateInputStream/InflateOutStream");

        try (
                ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(str.getBytes());
                DeflaterInputStream deflaterInputStream = new DeflaterInputStream(byteArrayInputStream1);
                ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                InflaterOutputStream inflaterOutputStream = new InflaterOutputStream(byteArrayOutputStream1);
        ) {
            iBytes = new byte[1024];
            int length = deflaterInputStream.read(iBytes);
            System.out.println("读取压缩内容长度:" + length);
            System.out.println("读取压缩内容经过压缩后内容不可读:" + new String(iBytes, "UTF-8"));
            inflaterOutputStream.write(iBytes);
            inflaterOutputStream.close();//下面取值
            System.out.println("输出内容:" + byteArrayOutputStream1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------GZIPOutputStream/GZIPInputStream---------------");

        String gzip = "中文gzip.gz";//默认格式gz,方便系统识别
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(gzip);
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream)) {
            System.out.println("压缩为zip.........");
            gzipOutputStream.write(str.getBytes(), 0, str.length());
            gzipOutputStream.flush();//将压缩内容写入缓冲区
            gzipOutputStream.finish();//等待完成
            System.out.println("完成后,会在系统生成一个zip压缩文件.............");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("现在解压zip");

        try (
                FileInputStream fileInputStream = new FileInputStream(gzip);
                GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream)
        ) {
            iBytes = new byte[1024];
            int length = gzipInputStream.read(iBytes, 0, 1024);
            System.out.println("读取内容长度:" + length);
            System.out.println("读取内容:" + new String(iBytes, "UTF-8"));
            iBytes = new byte[1024];
            length = fileInputStream.read(iBytes, 0, 1024);
            System.out.println("再次验证 非配对输入输出流 将无法读取文件 :" + length);
            System.out.println("读取内容为空 :" + new String(iBytes, "UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------------------------");
        System.out.println("gzip常与tar一块使用");
        System.out.println("apache tool下的 TarInputStream/TarOutputStream");
        String tarfile = "tarFile.tar";
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(tarfile);
                TarOutputStream tarOutputStream = new TarOutputStream(fileOutputStream);
        ) {
            System.out.println("长文件模式 默认不允许,可以选择截断,或保存长文件名");
            tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
            System.out.println("开启debug模式");
            tarOutputStream.setDebug(true);
            System.out.println("TarBuffer debug模式");
            tarOutputStream.setBufferDebug(true);
            System.out.println("大文件模式 不能大于8G");
            tarOutputStream.setBigNumberMode(TarOutputStream.BIGNUMBER_ERROR);
            System.out.println("使用pax头.pax可以压缩单个文件超过8G");
            tarOutputStream.setAddPaxHeadersForNonAsciiNames(false);
            File file1 = new File(dataStream);
            TarEntry tarEntry = new TarEntry(file1);
            tarOutputStream.putNextEntry(tarEntry);
            tarEntry.setSize(file1.length());
            FileInputStream fileInputStream = new FileInputStream(file1);
            byte[] bytes1 = new byte[1];
            while (fileInputStream.read(bytes1) != -1) {
                tarOutputStream.write(bytes1);
            }
            tarOutputStream.closeEntry();
            File file2 = new File(checkedStream);
            TarEntry tarEntry1 = new TarEntry(file2);
            tarOutputStream.putNextEntry(tarEntry1);
            tarEntry.setSize(file2.length());
            FileInputStream fileInputStream1 = new FileInputStream(file2);
            byte[] bytes2 = new byte[1];
            while (fileInputStream1.read(bytes2) != -1) {
                tarOutputStream.write(bytes2);
            }
            tarOutputStream.closeEntry();
            System.out.println("记录数:" + tarOutputStream.getRecordSize());
            tarOutputStream.finish();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(tarfile);
                TarInputStream tarInputStream = new TarInputStream(fileInputStream)
        ) {
            System.out.println("读取tar包");
            System.out.println("系统记录大小:" + tarInputStream.getRecordSize());
            System.out.println("剩余记录数:" + tarInputStream.available());
            System.out.println("不支持mark/reset:" + tarInputStream.markSupported());
            System.out.println("开启debug模式");
            tarInputStream.setDebug(true);
            File file2 = new File(checkedStream);
            TarEntry tarEntry1 = new TarEntry(file2);
            System.out.println("是否可以读取文件:" + tarInputStream.canReadEntryData(tarEntry1));
            while (true) {
                TarEntry tarEntry = tarInputStream.getNextEntry();
                if (tarEntry == null) {
                    System.out.println("空entry");
                    break;
                }
                System.out.println("文件名:" + tarEntry.getName());
                System.out.println("size:" + tarEntry.getSize());
                System.out.println("real size:" + tarEntry.getRealSize());
                byte bs[] = new byte[1024];
                int length = 0;
                while ((length = tarInputStream.read(bs)) != -1) {
                    System.out.println(new String(bs));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------ZIPOutputStream/ZIPInputStream---------------");
        String zipFile = "中文.zip";

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)
        ) {
            System.out.println("使用zip写入三个文件");
            for (int i = 0; i < 3; i++) {
                String entry = "zipfile" + i;
                ZipEntry zipEntry = new ZipEntry(entry);//FileOutputStream
                zipEntry.setComment("zipfile" + i);
                zipEntry.setExtra(("额外的参数" + i).getBytes());
                System.out.println("setExtra 在 JarOutputStream用来保存信息!");
                zipEntry.setCreationTime(FileTime.fromMillis(new Date().getTime()));
                zipEntry.setLastAccessTime(FileTime.from(new Date().getTime(), TimeUnit.MILLISECONDS));
                zipEntry.setLastModifiedTime(FileTime.from(new Date().getTime(), TimeUnit.MILLISECONDS));
                /**下面的参数在进行了更新**/
//            zipEntry.setCompressedSize(0);
//            zipEntry.setCrc(0);
//            zipEntry.setMethod(ZipEntry.DEFLATED);
//            zipEntry.setSize(100);
//            zipEntry.setTime(new Date().getTime());
                zipOutputStream.putNextEntry(zipEntry);//会对ZipEntry进行属性填充
                zipOutputStream.write("这是一条记录".getBytes(), 0,
                                      "这是一条记录".getBytes().length);//写入数据,注意大小是数组大小//"这是一条记录".getBytes().length
                zipOutputStream.closeEntry();//完成当前entry
                System.out.println(zipEntry.getComment());
                System.out.println(zipEntry.getExtra());
                System.out.println(zipEntry.getCrc());
                System.out.println(entry + " 文件写入完成!");
            }
            zipOutputStream.setComment("zip 打包");
            zipOutputStream.setMethod(ZipEntry.DEFLATED);//使用压缩
            zipOutputStream.setLevel(1);//等级为1
            zipOutputStream.finish();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileInputStream fileInputStream = new FileInputStream(zipFile);
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)
        ) {
            System.out.println("使用deflate/inflate时,available只用来表示是否结束");

            while (zipInputStream.available() > 0) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();//获取ZipEntry
                if (zipEntry == null) {
                    break;
                }
                System.out.println("文件名:" + zipEntry.getName());
                System.out.println("额外数据:" + new String(zipEntry.getExtra(), "utf-8"));
                System.out.println("日期:" + zipEntry.getCreationTime().toString());
                System.out.println("---------数据被擦除了crc.reset(); inf.reset();-------");
                System.out.println("注释:" + zipEntry.getComment());
                System.out.println("size:" + zipEntry.getSize());
                System.out.println("压缩后size:" + zipEntry.getCompressedSize());
                System.out.println("crc:" + zipEntry.getCrc());
                iBytes = new byte[1024];
                int length = zipInputStream.read(iBytes, 0, 1024);
                System.out.println("读取文件内容:" + new String(iBytes, "UTF-8") + " " + length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------JarOutputStream/ZIPInputStream---------------");
        System.out.println("读取target/下jar包,并重新打一个jar");
        String jarFile = "jaros.jar";
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(jarFile);
                FileInputStream fileInputStream = new FileInputStream("demo-0.0.1-SNAPSHOT.jar");
                JarInputStream jarInputStream = new JarInputStream(fileInputStream)
        ) {
            Manifest manifest = jarInputStream.getManifest();
            Map<String, Attributes> entries = manifest.getEntries();
            System.out.println("所有属性值");
            for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            System.out.println("main属性值:");
            for (Map.Entry<Object, Object> objectObjectEntry : manifest.getMainAttributes().entrySet()) {
                System.out.println(objectObjectEntry.getKey() + "=" + objectObjectEntry.getValue());
            }
            manifest.getMainAttributes().put(new Attributes.Name("Author"), "zhaofeng赵锋");
            System.out.println("将manifest写入输出流!");
            JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream, manifest);
            while (true) {
                JarEntry jarEntry = jarInputStream.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                System.out.println("Attributes:" + jarEntry.getAttributes());
                System.out.println("getCertificates:" + jarEntry.getCertificates());
                System.out.println("getCodeSigners:" + jarEntry.getCodeSigners());
                iBytes = new byte[4096];
                int length;
                int total = 0;
                jarOutputStream.putNextEntry(jarEntry);
                while ((length = jarInputStream.read(iBytes)) != -1) {
                    //如果是文件,则将文件内容写入输出流
                    jarOutputStream.write(iBytes, 0, length);
                    total += length;
                }
                System.out.println("写入文件:" + jarEntry.getName() + " " + total);
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("打包成功:" + jarFile);
        System.out.println("----------OutputStream PrintStream-------------");
        String printStr = "printStream";

        try (
                FileOutputStream fileOutputStream = new FileOutputStream(printStr);
                PrintStream printStream = new PrintStream(fileOutputStream, true);//开启自动刷新模式
                FileInputStream fileInputStream = new FileInputStream(printStr);
        ) {
            System.out.println("本类将Formatter的强大字符处理特性融合到其中");
            System.out.println("System.out是本类的一个应用");
            System.out.println("在执行print/println/printf/format都会将值写入输出流之中!");
            printStream.format("%1$TY-%1$Tm-%1$Td %1$tH:%1$tM:%1$tS", new Date());
            printStream.printf("%1$tF %1$tR", new Date());
            printStream.println("字符串");
            printStream.println(true);
            printStream.println('a');
            printStream.println(11111);
            printStream.println(11.111);
            printStream.append("只能追加字符串");
//            printStream.printf("%d","sss");
            System.out.println("是否有I/O错误" + printStream.checkError());
            printStream.flush();//确保已经写入文件
            iBytes = new byte[4096];
            int length;
            System.out.println("打印文件内容:");
            while ((length = fileInputStream.read(iBytes)) != -1) {
                System.out.println(new String(iBytes, 0, length));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------PushbackInputStream -----------------------");
        try (
                FileInputStream fileInputStream = new FileInputStream(printStr);
                PushbackInputStream pushbackInputStream = new PushbackInputStream(fileInputStream, 10)
        ) {
            System.out.println("不支持mark/reset:" + pushbackInputStream.markSupported());

            iBytes = new byte[1];
            System.out.println("UTF下,非中文1个字节,中文3个字节,如果读取1个字节无法识别,则推回,并读出三个字节来!");
            while ((pushbackInputStream.read(iBytes)) != -1) {
                String line = new String(iBytes, "utf-8");
                if (line.matches("[\\w-\\s:]+")) {
                    System.out.println("非中文: " + line);
                } else {
                    pushbackInputStream.unread(iBytes, 0, 1);
                    byte[] temp = new byte[3];
                    pushbackInputStream.read(temp, 0, 3);
                    System.out.println("中文:" + new String(temp, "utf-8"));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("------------FilterInputStream/FilterOutputStream结束---------------");

        System.out.println("----------------InputStream SequenceInputStream--------------------");
        try (
                FileInputStream fileInputStream1 = new FileInputStream(fileName);
                FileInputStream fileInputStream2 = new FileInputStream(printStr);
                SequenceInputStream sequenceInputStream = new SequenceInputStream(fileInputStream1, fileInputStream2)
        ) {
            iBytes = new byte[4096];
            System.out.println("读取多个流信息!");
            while ((sequenceInputStream.read(iBytes)) != -1) {
                System.out.println(new String(iBytes, "utf-8"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("---------------PipedOutputStream/PipedInputStream--------------------");

        try (
                PipedOutputStream pipedOutputStream = new PipedOutputStream();
                //第一种,初始化指定输出流
                //PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
                PipedInputStream pipedInputStream = new PipedInputStream()) {

            System.out.println("两种连接方式:\n第一种创建输入流时指定输出流\n第二种输出流连接输入流");
            //第二种,创建后连接
            pipedOutputStream.connect(pipedInputStream);
//            pipedInputStream.connect(pipedOutputStream);
            System.out.println("PipedOutputStream 写入数据");
            pipedOutputStream.write(100);
            System.out.println("pipedInputStream 读取数据");
            System.out.println(pipedInputStream.read());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("--------多线程下的PipedOutputStream/PipedInputStream----------");
        ExecutorService service = Executors.newFixedThreadPool(2);
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream();
        System.out.println("输入输出流不要关闭");
        try {
            pipedInputStream.connect(pipedOutputStream);
            pipedOutputStream.write(100);
            System.out.println(pipedInputStream.read());
            service.execute(new Thread(new Sender(pipedOutputStream)));
            service.execute(new Thread(new Receiver(pipedInputStream)));
            service.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class Sender implements Runnable {

        private PipedOutputStream pipedOutputStream;

        public Sender(PipedOutputStream pipedOutputStream) {
            this.pipedOutputStream = pipedOutputStream;
        }

        public PipedOutputStream getPipedOutputStream() {
            return pipedOutputStream;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            while (true) {
                try {
                    String msg = String.format("当前日期:%1$tF %1$tT\n", new Date());
//                    System.out.println("写入消息:"+msg);
                    pipedOutputStream.write(msg.getBytes());
                    pipedOutputStream.flush();
                    Thread.sleep(1000l);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    static class Receiver implements Runnable {

        private PipedInputStream pipedInputStream;

        public Receiver(PipedInputStream pipedInputStream) {
            this.pipedInputStream = pipedInputStream;
        }

        public PipedInputStream getPipedInputStream() {
            return pipedInputStream;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            byte[] bt = new byte[1024];
            int i = 0;
            while (true) {
                try {
                    int b = pipedInputStream.read();
                    if (b == -1) {
                        continue;
                    }
                    bt[i++] = (byte) b;
                    if ((char) b == '\n') {
                        System.out.println("接收到:" + new String(bt, "utf-8"));
                        i = 0;
                        bt = new byte[1024];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
