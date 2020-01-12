package client;

public interface HistoryHandler {
    void writeLastMsg(String msg, String nickname);
    String getLastHundredMsg(String nickname);
}
