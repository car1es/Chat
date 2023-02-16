package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private volatile static Logger instance;

    private Logger() {
    }

    public void log(String msg) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm")) + "] " + msg);

        try (FileWriter writer = new FileWriter("file.log", true)) {

            writer.write("[" + LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm")) + "] " + msg + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
}
