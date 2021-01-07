package de.rnd7.cupsmqtt.config;

import com.google.gson.annotations.SerializedName;

public class ConfigCups {
    @SerializedName("host")
    private String host;

    @SerializedName("port")
    private int port = 631;

    @SerializedName("username")
    private String username = "anonymous";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }
}
