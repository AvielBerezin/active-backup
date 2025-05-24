package aviel.scratch.network_api;

public interface TopicListener<Message> {
    void onReceivedMessage(Message message);
    void onWriterLost(long id);
}
