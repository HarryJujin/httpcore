package com.zhongan.qa.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;

public class HttpClientBase {
    private CloseableHttpClient client;
    private HttpClientBuilder   builder     = HttpClients.custom();
    private CookieStore         cookieStore = new BasicCookieStore();

    private void setProxy(String route, int port) {
        HttpHost proxy = new HttpHost(route, port);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        builder.setRoutePlanner(routePlanner);
    }

    private void setCookie(String cookieKey, String cookieValue) {
        BasicClientCookie cookie = new BasicClientCookie(cookieKey, cookieValue);
        cookie.setDomain("");
        cookieStore.addCookie(cookie);
    }

    public CloseableHttpClient initClient(Map<String, String> cookieMap, Map<String, Object> proxyMap) {
        if (null != cookieMap) {
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                setCookie(entry.getKey(), entry.getValue());
            }
            builder.setDefaultCookieStore(cookieStore);
        }
        if (null != proxyMap) {
            setProxy((String) proxyMap.get("route"), (int) proxyMap.get("port"));
        }
        this.client = builder.build();
        return this.client;
    }

    public CloseableHttpClient initHttpsClient(Map<String, String> cookieMap, Map<String, Object> proxyMap) {
        if (null != cookieMap) {
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                setCookie(entry.getKey(), entry.getValue());
            }
            builder.setDefaultCookieStore(cookieStore);
        }
        if (null != proxyMap) {
            setProxy((String) proxyMap.get("route"), (int) proxyMap.get("port"));
        }
        HttpClientConnectionManager manager = this.initManager();
        this.builder.setConnectionManager(manager);
        this.client = builder.build();
        return this.client;
    }

    private HttpClientConnectionManager initManager() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            });
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), new String[] {
                    "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
            return new PoolingHttpClientConnectionManager(registry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
