package com.fansin.http;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * <p>Title: AllTrustSSLContext</p>
 * <p>Description: 客户端不对证书验证</p>
 *
 * @author zhaofeng
 * @version 1.0
 * @date 18 -5-9
 */
public class AllTrustSSLContext {

    private static final String SSL_PROTOCOL = "SSL";

    private AllTrustSSLContext() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     * @throws KeyManagementException   the key management exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static SSLContext getInstance() throws KeyManagementException, NoSuchAlgorithmException {
        return getInstance(SSL_PROTOCOL);
    }

    /**
     * Gets instance.
     *
     * @param protocol the protocol
     * @return the instance
     * @throws KeyManagementException   the key management exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static SSLContext getInstance(String protocol) throws KeyManagementException, NoSuchAlgorithmException {

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance(SSL_PROTOCOL);
            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (KeyManagementException e) {
            throw new KeyManagementException("无法找到TrustManager!TrustManager实例为空实现,不应该出现该异常!!!", e);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("无法找到协议类型,请确定[SSLv2Hello,SSLv3,TLSv1,TLSv1.1,TLSv1.2]", e);
        }
        return sc;
    }
}
