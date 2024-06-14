package com.lim.demos.kerberos.example;

import javax.security.auth.callback.*;

/**
 * KerberosCallBackHandler
 * <p>回调</p>
 *
 * @author lim
 * @version 1.0
 * @since 2024/6/14 上午11:03
 */
public class KerberosCallBackHandler implements CallbackHandler {

    @Override
    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        // call database or retrieve credentials by other means
        String user = "admin/admin";
        String password = "centos_lim";

        for (Callback callback : callbacks) {

            if (callback instanceof NameCallback) {
                NameCallback nc = (NameCallback) callback;
                nc.setName(user);
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback) callback;
                pc.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Unknown Callback");
            }

        }
    }
}
