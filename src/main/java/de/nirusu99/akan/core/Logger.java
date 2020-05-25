package de.nirusu99.akan.core;

import de.nirusu99.akan.ui.CMD;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    JSONObject obj;
    File file;
    FileWriter writer;
    File logFolder;

    public Logger() {
        logFolder = new File(new File("").getAbsolutePath().concat(File.separator + "logs"));
        logFolder.mkdir();
        updateFile();
    }

    private void updateFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        File tmp = new File(logFolder.getAbsolutePath().concat(File.separator + dtf.format(now) + "log.json"));
        try {
            file = tmp;
            if (!tmp.exists()) {
                if (!tmp.createNewFile()) {
                    System.err.println("couldn't create file");
                } else {
                    System.out.println("created log file for " + dtf.format(now));
                }
                writer = new FileWriter(tmp);
                writer.write("{}");
                writer.flush();
                writer.close();
            }
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    public void addLog(MessageReceivedEvent event, final CMD cmd) {
        updateFile();
        JSONArray array = new JSONArray();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        array.add(event.getAuthor().getAsTag());
        array.add(event.getMessage().getContentRaw());
        array.add(cmd.name());
        array.add(event.getChannelType().name().toLowerCase());
        if (event.getChannelType().getId() == 0) {
            array.add(String.valueOf(event.getGuild()));
            array.add(event.getChannel().getName());
        }
        obj.put(dtf.format(now),array);
        try {
            if (!file.canWrite()) {
                System.err.println("Logger: Couldn't writer log.json");
            }
            writer = new FileWriter(file);
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

