package com.fansin.serialize;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by zhaofeng on 17-4-2.
 */
public class MySerializable {

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, IllegalBlockSizeException, InvalidKeyException,
                   NoSuchAlgorithmException, NoSuchPaddingException, SignatureException {
        //serializablle
        normal();
        System.out.println("-------------------");
        //ExternalSerializable
        exteral();
        //seal
        System.out.println("--------------------");
        sealed();
        //signed
        System.out.println("--------------------");
        signed();
        //poxy
        System.out.println("--------------------");
        proxy();

    }

    public static void proxy()
            throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException,
                   InvalidKeyException, IllegalBlockSizeException, SignatureException {
        File out = new File("proxy.out");
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(out));

        TestPerson person = new TestPerson("fansin", 27);
        ProxyPerson proxy = new ProxyPerson(person);
        oout.writeObject(proxy);
        oout.flush();
        oout.close();

        if (out.exists()) {
            System.out.println("生成序列化文件." + out.getAbsolutePath());
        }

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(out));
        Object o = oin.readObject();
        System.out.println(o instanceof TestPerson);
        System.out.println(o);

        oin.close();
    }

    public static void signed()
            throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException,
                   InvalidKeyException, IllegalBlockSizeException, SignatureException {
        File out = new File("sign.out");
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(out));

        ExternalPerson person = new ExternalPerson("fansin", "男", 27);

        //加密
        Signature signingEngine = Signature.getInstance("MD5withRSA");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        SignedObject signedObject = new SignedObject(person, privateKey, signingEngine);
        System.out.println("签名加密后文件:" + signedObject.toString());
        oout.writeObject(signedObject);
        oout.flush();
        oout.close();

        if (out.exists()) {
            System.out.println("生成序列化文件." + out.getAbsolutePath());
        }

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(out));
        SignedObject signedObject1 = (SignedObject) oin.readObject();
        //解密
        if (signedObject1.verify(publicKey, signingEngine)) {
            System.out.println("验证签名成功");
            Object o = signedObject1.getObject();
            System.out.println(o);
        }
        oin.close();
    }

    public static void sealed()
            throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException,
                   InvalidKeyException, IllegalBlockSizeException {
        File out = new File("sealed.out");
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(out));

        ExternalPerson person = new ExternalPerson("fansin", "男", 27);

        //加密
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        Key key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        SealedObject sealedObject = new SealedObject(person, cipher);
        System.out.println("加密后文件:" + sealedObject.toString());
        oout.writeObject(sealedObject);
        oout.flush();
        oout.close();

        if (out.exists()) {
            System.out.println("生成序列化文件." + out.getAbsolutePath());
        }

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(out));
        SealedObject seal = (SealedObject) oin.readObject();
        //解密
        Object o = seal.getObject(key);
        System.out.println(o);
        oin.close();
    }

    public static void exteral() throws IOException, ClassNotFoundException {
        File out = new File("ExternalPerson.out");
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(out));

        ExternalPerson person = new ExternalPerson("fansin", "男", 27);

        oout.writeObject(person);
        oout.flush();
        oout.close();

        if (out.exists()) {
            System.out.println("生成序列化文件." + out.getAbsolutePath());
        }

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(out));
        Object o = oin.readObject();
        System.out.println(o);

        oin.close();
    }

    public static void normal() throws IOException, ClassNotFoundException {
        File out = new File("TestPerson.out");
//        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(out));
//
//        TestPerson person = new TestPerson("fansin",27);
//
//        oout.writeObject(person);
//        oout.flush();
//        oout.close();

        if (out.exists()) {
            System.out.println("生成序列化文件." + out.getAbsolutePath());
        }

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(out));
        Object o = oin.readObject();
        System.out.println(o);

        oin.close();
    }

}
