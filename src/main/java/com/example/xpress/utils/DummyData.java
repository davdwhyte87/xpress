package com.example.xpress.utils;

import com.example.xpress.models.User;

public final class DummyData {
    public static User user = new User("jndndj","Jenny Ide","jenny@gmail.com","YEUH839jDHJUND");

    // simulating secrete data config. We should read this data from a config file or environment variable file
    public static String Secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    public static User GetUser(){

        return user;

    }
}
