package aviel.scratch;

import aviel.scratch.active_backup.ActiveBackupProvider;
import aviel.scratch.active_backup.active_backup_events.StatefulActiveBackup;
import aviel.scratch.network_api.ActiveBackupCompetition;
import aviel.scratch.network_api.NetworkApi;
import aviel.scratch.network_api.NetworkApiMock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scratch {
    private static final Logger LOGGER = LogManager.getLogger();

    public static class Run1 {
        public static void main(String[] args) throws InterruptedException {
            NetworkApiMock networkApiMock = NetworkApiMock.create();
            NetworkApi networkApi = networkApiMock.networkApi();
            String site = "";
            long id = System.currentTimeMillis();
            try (ActiveBackupProvider abProvider = new ActiveBackupProvider(networkApi, site, id, new StatefulActiveBackup() {
                @Override
                public void onBackup() {
                    LOGGER.info("backup");
                }

                @Override
                public void onActive() {
                    LOGGER.info("active");
                }
            })) {
                abProvider.changeStrength(5);
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1001L, 5, "site1"));
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1002L, 15, "site1"));
                Thread.sleep(6000);
                abProvider.changeStrength(10);
                Thread.sleep(1000);
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1002L, 30, "site1"));
                Thread.sleep(1000);
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1002L, 10, "site1"));
                Thread.sleep(1000);
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1001L, 30, "site1"));
                Thread.sleep(1000);
                networkApiMock.triggerOnWriterLost(1001L);
                Thread.sleep(1000);
                networkApiMock.triggerOnWriterLost(1002L);
                Thread.sleep(1000);
            }
        }
    }

    public static class Run2 {
        public static void main(String[] args) throws InterruptedException {
            NetworkApiMock networkApiMock = NetworkApiMock.create();
            NetworkApi networkApi = networkApiMock.networkApi();
            String site = "";
            long id = System.currentTimeMillis();
            try (ActiveBackupProvider abProvider = new ActiveBackupProvider(networkApi, site, id, new StatefulActiveBackup() {
                @Override
                public void onBackup() {
                    LOGGER.info("backup");
                }

                @Override
                public void onActive() {
                    LOGGER.info("active");
                }
            })) {
                abProvider.changeStrength(5);
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1001L, 10, "site1"));
                networkApiMock.triggerOnReceivedMessage(new ActiveBackupCompetition(1002L, 10, "site1"));
                Thread.sleep(6000);
                networkApiMock.triggerHandOver();
                Thread.sleep(1000);
            }
        }
    }
}
