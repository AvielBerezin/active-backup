package aviel.scratch.network_api;

public class NetworkApi {
    public static final NetworkApi instance = new NetworkApi();

    public TopicReader openActiveBackupCompetitionReader(TopicListener<ActiveBackupCompetition> listener) {
        return new TopicReader();
    }

    public TopicReader openHandOverProvider(Runnable handOverImplementation) {
        return new TopicReader();
    }

    public TopicWriter<ActiveBackupCompetition> openActiveBackupCompetitionWriter() {
        return new TopicWriter<>();
    }
}
