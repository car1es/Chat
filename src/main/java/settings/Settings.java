package settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Settings {
    private static final File SETTING = new File("settings.txt");
    private  final String[] settings;
    public Settings() {
        this.settings = settingFile();
    }

    public String[] settingFile() {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(SETTING))) {
            String line;
            while ((line = br.readLine()) != null) {
                result = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.split(":");
    }

    public String getHost() {
        return settings[0];
    }

    public int getPort() {
        return Integer.parseInt(settings[1]);
    }
}
