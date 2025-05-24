package aviel.scratch.network_api;

import java.util.concurrent.atomic.AtomicReference;

public final class NetworkApiMock {
    private final AtomicReference<TopicListener<ActiveBackupCompetition>> box;
    private final NetworkApi networkApi;

    private NetworkApiMock(AtomicReference<TopicListener<ActiveBackupCompetition>> box, NetworkApi networkApi) {
        this.box = box;
        this.networkApi = networkApi;
    }

    public static NetworkApiMock create() {
        AtomicReference<TopicListener<ActiveBackupCompetition>> box = new AtomicReference<>();
        NetworkApi instance = new NetworkApi() {
            @Override
            public TopicReader openActiveBackupCompetitionReader(TopicListener<ActiveBackupCompetition> listener) {
                if (box.get() != null) {
                    throw new IllegalStateException("ActiveBackupCompetitionReader can only be opened once");
                }
                box.set(listener);
                return super.openActiveBackupCompetitionReader(listener);
            }
        };
        return new NetworkApiMock(box, instance);
    }

    public void triggerOnWriterLost(long id) {
        box.get().onWriterLost(id);
    }

    public void triggerOnReceivedMessage(long id, int strength) {
        box.get().onReceivedMessage(new ActiveBackupCompetition(id, strength));
    }

    public NetworkApi networkApi() {
        return networkApi;
    }
}
