package de.rnd7.cupsmqtt;

import de.rnd7.cupsmqtt.config.Config;
import de.rnd7.cupsmqtt.config.ConfigParser;
import de.rnd7.cupsmqtt.cups.CupsService;
import de.rnd7.cupsmqtt.mqtt.GwMqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public Main(final Config config) {
        LOGGER.debug("Debug enabled");
        LOGGER.info("Info enabled");

        try {
            Events.register(this);
            final GwMqttClient client = new GwMqttClient(config, Events.getBus());

            Events.register(client);

            new CupsService(config.getCups())
                .start();

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(final String[] args) {
        if (args.length != 1) {
            LOGGER.error("Expected configuration file as argument");
            return;
        }

        try {
            new Main(ConfigParser.parse(new File(args[0])));
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
