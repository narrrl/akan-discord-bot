package de.nirusu99.akan.core;

import de.nirusu99.akan.AkanBot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class Config {
    private final JSONObject obj;
    private final File file;
    private final AkanBot akanbot;

    public Config(@Nonnull final AkanBot akanBot) {
        file = new File(new File("").getAbsolutePath().concat(File.separator + "config.json"));
        this.akanbot = akanBot;
        try {
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getValue(final String key) {
        return (String) obj.get(key);
    }

    public synchronized boolean withSuccessReaction() {
        return Boolean.parseBoolean((String) obj.get("successReaction"));
    }

    public synchronized void setSuccessReaction(final boolean b) {
        write("successReaction", String.valueOf(b));
    }

    public synchronized void setPrefix(@Nonnull final String prefix) {
        write("prefix", prefix);
    }

    public synchronized void setActivity(@Nonnull final String status, @Nonnull final String type) {
        write("activity", status);
        write("activityType",type);
    }

    public synchronized void write(@Nonnull final String key, @Nonnull final String value) {
        obj.remove(key);
        obj.put(key,value);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            akanbot.printInfo(e.getMessage());
        }
    }
}
