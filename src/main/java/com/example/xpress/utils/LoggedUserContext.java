package com.example.xpress.utils;

import com.example.xpress.models.User;


// this keeps the user data, email in our case stored in the request thread.
// we can fetch this data until the thread is killed
public abstract class LoggedUserContext {

    private static ThreadLocal<String> currentLoggedUser = new ThreadLocal<>();

    public static void setCurrentLoggedUser(String loggedUser) {
        if (currentLoggedUser == null) {
            currentLoggedUser = new ThreadLocal<>();
        }
        currentLoggedUser.set(loggedUser);
    }

    public static String getCurrentLoggedUser() {
        return currentLoggedUser != null ? currentLoggedUser.get() : null;
    }

    public static void clear() {
        if (currentLoggedUser != null) {
            currentLoggedUser.remove();
        }
    }
}
