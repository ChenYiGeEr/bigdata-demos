package com.lim.demos.kerberos.example;

import com.github.markusbernhardt.proxy.ProxySearch;
import com.github.markusbernhardt.proxy.selector.fixed.FixedProxySelector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URL;
import java.security.Principal;

/**
 * KerberosAuthExample
 * <p>kerberos身份认证例子</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/14 上午10:59
 */
public class KerberosAuthExample {

    private static final String PROXY_HOST = "CHANGEME";
    private static final int PROXY_PORT = 3128;

    // 调用服务器方法，传入URL
    public static void callServer(String url) throws IOException {
        HttpClient httpclient = getHttpClient();

        try {

            HttpUriRequest request = new HttpGet(url);
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println("----------------------------------------");

            System.out.println("STATUS >> " + response.getStatusLine());

            if (entity != null) {
                System.out.println("RESULT >> " + EntityUtils.toString(entity));
            }

            System.out.println("----------------------------------------");

            EntityUtils.consume(entity);

        } finally {
            httpclient.getConnectionManager().shutdown();
        }

    }

    // 获取HTTP客户端方法
    private static HttpClient getHttpClient() {

        Credentials use_jaas_creds = new Credentials() {
            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }
        };
        // 创建并设置凭据提供者
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(null, -1, null), use_jaas_creds);

        // 创建认证方案注册表
        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();

        // 创建并返回HTTP客户端
        CloseableHttpClient httpclient = HttpClients.custom()
                // set our proxy - httpclient doesn't use ProxySelector
                .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost(PROXY_HOST, PROXY_PORT)))
                .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                .setDefaultCredentialsProvider(credsProvider).build();

        return httpclient;
    }

    // 自动配置代理方法
    public static void autoconfigureProxy() {
        final ProxySearch proxySearch = new ProxySearch();
        proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
        proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
        proxySearch.addStrategy(ProxySearch.Strategy.ENV_VAR);
//        ProxySelector.setDefault(proxySearch.getProxySelector());
        ProxySelector.setDefault(new FixedProxySelector(PROXY_HOST, PROXY_PORT));
    }

    // 主方法
//    public static void main(String[] args) throws IOException {
//        System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
//        System.setProperty("java.security.auth.login.config", "login.conf");
//        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
//        System.setProperty("sun.security.krb5.debug", "true");
//        System.setProperty("sun.security.jgss.debug", "true");
//
//        // 设置默认回调处理程序以避免在命令行提示输入密码
//        // check https://github.com/frohoff/jdk8u-dev-jdk/blob/master/src/share/classes/sun/security/jgss/GSSUtil.java#L241
//        Security.setProperty("auth.login.defaultCallbackHandler", "net.curiousprogrammer.auth.kerberos.example.KerberosCallBackHandler");
//
//        autoconfigureProxy();
//
//        callServer("http://example.com");
//        callServer("https://example.com");
//    }

    public static void main(String[] args) {
        try {
            // 设置krb5.conf文件路径
            System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
            // 设置Keytab文件路径和Principal名称
            URL resourceUrl = KerberosAuthExample.class.getClassLoader().getResource("jaas.conf");
            if (resourceUrl != null) {
                System.setProperty("java.security.auth.login.config", resourceUrl.getPath());
            }

            // 创建GSSManager实例
            GSSManager manager = GSSManager.getInstance();

            // 创建Kerberos名字
            GSSName serverName = manager.createName("HTTP/your.server.com@EXAMPLE.COM", GSSName.NT_HOSTBASED_SERVICE);

            // 创建Kerberos OID
            Oid krb5 = new Oid("1.2.840.113554.1.2.2");

            // 创建GSSContext实例
            GSSContext context = manager.createContext(serverName, krb5, null, GSSContext.DEFAULT_LIFETIME);

            // 开始身份验证过程
            byte[] token = new byte[0];
            context.requestMutualAuth(true); // 请求相互认证
            context.requestConf(true);       // 请求机密性保护

            while (!context.isEstablished()) {
                token = context.initSecContext(token, 0, token.length);
                if (token != null && token.length > 0) {
                    // 将token发送给服务器并接收响应（这里假设token发送和接收过程已经实现）
                    token = sendTokenToServerAndReceiveResponse(token);
                }
            }

            System.out.println("Kerberos authentication established successfully.");

            // 清理GSSContext
            context.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] sendTokenToServerAndReceiveResponse(byte[] token) {
        /*
         * TODO 这里实现发送token到服务器并接收响应的逻辑
         * 这部分的实现会依赖于你的网络通信代码
         */
        return new byte[0]; // 返回服务器的响应token
    }

}
