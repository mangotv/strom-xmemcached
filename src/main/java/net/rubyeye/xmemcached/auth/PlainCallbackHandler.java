package net.rubyeye.xmemcached.auth;

import javax.security.auth.callback.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * A callback handler for name/password authentication
 *
 * @author dennis
 */
public class PlainCallbackHandler implements CallbackHandler, Serializable {
    private String username;
    private String password;


    public PlainCallbackHandler(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }


    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(this.username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password
                        .toCharArray());
            } else
                throw new UnsupportedCallbackException(callback);
        }

    }

}
