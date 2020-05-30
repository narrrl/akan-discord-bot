package de.nirusu99.akan.core;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    AkanBot bot;
    JSONObject obj;
    File file;
    FileWriter writer;
    File logFolder;

    public Logger(@Nonnull final AkanBot bot) {
        this.bot = bot;
        logFolder = new File(new File("").getAbsolutePath().concat(File.separator + "logs"));
        if (logFolder.mkdir()) {
            bot.printInfo("created logs directory");
        }
        updateFile();
    }

    private synchronized void updateFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        File tmp = new File(logFolder.getAbsolutePath().concat(File.separator + dtf.format(now) + "log.json"));
        try {
            file = tmp;
            if (!tmp.exists()) {
                if (!tmp.createNewFile()) {
                    bot.printInfo("couldn't create new log file for " + dtf.format(now));
                } else {
                    bot.printInfo("created log file for " + dtf.format(now));
                }
                writer = new FileWriter(tmp);
                writer.write("{}");
                writer.flush();
                writer.close();
            }
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            bot.printInfo(e.getMessage());
        }
    }

    public synchronized void addLog(@Nonnull GuildMessageReceivedEvent event,@Nonnull final ICommand cmd) {
        updateFile();
        JSONArray array = new JSONArray();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        array.add(event.getAuthor().getAsTag());
        array.add(event.getMessage().getContentRaw());
        array.add(cmd.getName());
        array.add(String.valueOf(event.getGuild()));
        array.add(event.getChannel().getName());
        obj.put(dtf.format(now),array);
        try {
            if (!file.canWrite()) {
                bot.printInfo("Logger: Couldn't write log.json");
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

