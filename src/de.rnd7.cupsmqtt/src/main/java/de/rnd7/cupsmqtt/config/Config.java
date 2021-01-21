package de.rnd7.cupsmqtt.config;

import de.rnd7.mqttgateway.config.ConfigMqtt;

public class Config {

    private ConfigMqtt mqtt;
    private ConfigCups cups;

    public ConfigMqtt getMqtt() {
        return mqtt;
    }

    public ConfigCups getCups() {
        return cups;
    }
}
