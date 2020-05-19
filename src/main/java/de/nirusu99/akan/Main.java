package de.nirusu99.akan;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Bot.start();
        } catch (LoginException e) {
            System.err.println(e.getMessage());
        }
    }
}
