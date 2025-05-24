package aviel.scratch.network_api;

import java.util.concurrent.atomic.AtomicReference;

public final class NetworkApiMock {
    private final AtomicReference<Runnable> handOverUserAction;
    private final AtomicReference<TopicListener<ActiveBackupCompetition>> activeBackupCompetition;
    private final NetworkApi networkApi;

    private NetworkApiMock(NetworkApi networkApi,
                           AtomicReference<TopicListener<ActiveBackupCompetition>> activeBackupCompetition,
                           AtomicReference<Runnable> handOverUserAction) {
        this.networkApi = networkApi;
        this.activeBackupCompetition = activeBackupCompetition;
        this.handOverUserAction = handOverUserAction;
    }

    public static NetworkApiMock create() {
        AtomicReference<TopicListener<ActiveBackupCompetition>> activeBackupCompetition = new AtomicReference<>();
        AtomicReference<Runnable> handOverUserAction = new AtomicReference<>();
        NetworkApi instance = new NetworkApi() {
            @Override
            public TopicReader openActiveBackupCompetitionReader(TopicListener<ActiveBackupCompetition> listener) {
                if (activeBackupCompetition.get() != null) {
                    throw new IllegalStateException("ActiveBackupCompetitionReader can only be opened once");
                }
                activeBackupCompetition.set(listener);
                return super.openActiveBackupCompetitionReader(listener);
            }

            @Override
            public TopicReader openHandOverProvider(Runnable handOverImplementation) {
                if (handOverUserAction.get() != null) {
                    throw new IllegalStateException("HandOverProvider can only be opened once");
                }
                handOverUserAction.set(handOverImplementation);
                return super.openHandOverProvider(handOverImplementation);
            }
        };
        return new NetworkApiMock(instance, activeBackupCompetition, handOverUserAction);
    }

    public void triggerOnWriterLost(long id) {
        activeBackupCompetition.get().onWriterLost(id);
    }

    public void triggerOnReceivedMessage(ActiveBackupCompetition message) {
        activeBackupCompetition.get().onReceivedMessage(message);
    }

    public void triggerHandOver() {
        handOverUserAction.get().run();
    }

    public NetworkApi networkApi() {
        return networkApi;
    }
}
