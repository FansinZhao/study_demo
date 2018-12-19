package com.fansin.designpattern;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaofeng on 17-5-12.
 */
public class CompositeDemo {

    /*
    组合模式:一种树型结构的模式
    jdk中map/list是这种模式,可以添加自身
    业务场景:
    代付服务中,每个商户准备一个证书,用来向全渠道发送支付请求.证书失效后,会将失效证书移动到BAK/目录下,同时新上传一个证书
    目录结构
    ---证书目录
        ---机构号
            ---商户号
                --BAK
                    |失效证书
                |有效证书
     这里含有两种:文件(叶子)和目录
     */

    public static void main(String[] args) {
        AbstractCertFile file = new CertFile("/证书目录/");
        file.addFile(new CertFile("机构A/"));
        file.addFile(new CertFile("商户A/"));
        file.addFile(new MerchantAbstractCertFile("有效证书.cer"));
        file.printPath();
    }

}

abstract class AbstractCertFile {

    protected String name;

    public AbstractCertFile(String name) {
        this.name = name;
    }

    abstract void addFile(AbstractCertFile file);

    abstract void printPath();

}

class MerchantAbstractCertFile extends AbstractCertFile {

    public MerchantAbstractCertFile(String name) {
        super(name);
    }

    @Override
    void addFile(AbstractCertFile file) {
        //什么都不做
    }

    @Override
    void printPath() {
        System.out.println(name);
    }
}

class CertFile extends AbstractCertFile {

    private List<AbstractCertFile> list = new LinkedList<>();

    public CertFile(String name) {
        super(name);
    }

    @Override
    void addFile(AbstractCertFile file) {
        //什么都不做
        list.add(file);
    }

    @Override
    void printPath() {
        System.out.println(name);
        for (AbstractCertFile file : list) {
            file.printPath();
        }
    }
}
