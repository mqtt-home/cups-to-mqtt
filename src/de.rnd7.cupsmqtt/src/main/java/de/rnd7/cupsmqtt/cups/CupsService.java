package de.rnd7.cupsmqtt.cups;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import de.rnd7.cupsmqtt.Main;
import de.rnd7.cupsmqtt.config.ConfigCups;
import de.rnd7.mqttgateway.Events;
import de.rnd7.mqttgateway.PublishMessage;
import de.rnd7.mqttgateway.TopicCleaner;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJobAttributes;
import org.cups4j.WhichJobsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CupsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private final ConfigCups config;

    private static final Gson gson = new Gson();

    public static class CupsMessage {
        @SerializedName("jobs-completed")
        int jobsCompleted;
        @SerializedName("jobs-queued")
        int jobsQueued;
    }

    public CupsService(final ConfigCups config) {
        this.config = config;
    }

    public CupsService start() {
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleWithFixedDelay(this::poll, 1500, 2000, TimeUnit.MILLISECONDS);

        return this;
    }

    private void poll() {
        try {
            final CupsClient cups = new CupsClient(config.getHost(), config.getPort(), config.getUsername());
            final List<CupsPrinter> printers = cups.getPrinters();
            for (final CupsPrinter printer : printers) {
                final CupsMessage cupsMessage = new CupsMessage();
                final List<PrintJobAttributes> completed = printer.getJobs(WhichJobsEnum.COMPLETED, null, false);
                cupsMessage.jobsCompleted = completed.size();

                final List<PrintJobAttributes> notCompleted = printer.getJobs(WhichJobsEnum.NOT_COMPLETED, null, false);
                cupsMessage.jobsQueued = notCompleted.size();

                Events.getBus().post(
                    PublishMessage.relative(TopicCleaner.clean(printer.getName()), gson.toJson(cupsMessage))
                );
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
