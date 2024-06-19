package com.lim.demos.kerberos.example;

import org.ietf.jgss.*;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.PrivilegedAction;

/**
 * KerberosAuthExample
 * <p>kerberos身份认证例子</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/14 上午10:59
 */
public class KerberosAuthExample {

    public static void main(String[] args) {
        try {
            // 步骤1：创建Kerberos客户端
            LoginContext loginContext = new LoginContext("KerberosClient");

            // 步骤2：登录Kerberos客户端
            loginContext.login();

            // 步骤3：获取Kerberos票据
            Subject subject = loginContext.getSubject();

            GSSManager manager = GSSManager.getInstance();
            GSSName serviceName = manager.createName("HTTP/your.server@EXAMPLE.COM", GSSName.NT_HOSTBASED_SERVICE);
            Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");

            // 步骤4：将票据传递给目标服务器
            // 可以使用HTTP请求将票据传递给目标服务器
            GSSContext context = Subject.doAs(subject, (PrivilegedAction<GSSContext>) () -> {
                try {
                    // 步骤5：目标服务器验证票据
                    // 目标服务器需要验证票据的合法性，可以使用Jaas进行验证

                    // 步骤6：发送服务票据给目标服务器
                    // Kerberos认证服务器将服务票据发送给目标服务器，目标服务器使用该票据进行后续操作
                    return manager.createContext(serviceName, krb5Oid, null, GSSContext.DEFAULT_LIFETIME);
                } catch (GSSException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            if (context != null) {
                context.initSecContext(new byte[0], 0, 0);
                System.out.println("Authentication successful");
                // 步骤7：认证成功
                // 认证成功后，可以进行一些操作，例如访问受限资源
            }

        } catch (LoginException | GSSException e) {
            e.printStackTrace();
        }
    }

}
