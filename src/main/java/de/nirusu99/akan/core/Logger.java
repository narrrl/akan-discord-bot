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

public class Logger {
    final JSONObject obj;
    final File file;
    FileWriter writer;

    public Logger() {
        file = new File(new File("").getAbsolutePath().concat(File.separator + "log.json"));
        try {
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addLog(MessageReceivedEvent event, final CMD cmd) {
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
            writer = new FileWriter(file);
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

