package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalhostHistoryHandler implements HistoryHandler {

    private static HistoryHandler historyHandler;
    private List<String> messages;
    private Path path;


    private LocalhostHistoryHandler() {
    }

    private void createFileIfNotExists(String nickname) {
        Path path = Paths.get(nickname + ".txt");
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeLastMsg(String msg, String nickname) {
        System.out.println(msg);
        createFileIfNotExists(nickname);
        try {
            Files.write(path, (msg+"\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLastHundredMsg(String nickname) {
        createFileIfNotExists(nickname);
        messages = new ArrayList<>();
        path = Paths.get(nickname + ".txt");
        try (Stream<String> lineStream = Files.newBufferedReader(path).lines()) {
            messages = lineStream.collect(Collectors.toList());
            if (messages.size() > 100) {
                return messages.stream().skip(messages.size() - 100).map(str -> str + "\n").reduce("", (str1, str2) -> str1 + str2);
            } else {
                return messages.stream().map(str -> str + "\n").reduce("", (str1, str2) -> str1 + str2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static HistoryHandler getInstance() {
        if (historyHandler == null) historyHandler = new LocalhostHistoryHandler();
        return historyHandler;
    }
}