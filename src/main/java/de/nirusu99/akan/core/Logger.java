package de.nirusu99.akan.core;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    AkanBot bot;
    JSONObject logObj;
    File logFile;
    FileWriter writer;
    File logFolder;
    File guildFolder;

    public Logger(@Nonnull final AkanBot bot) {
        this.bot = bot;
        guildFolder = new File(new File("").getAbsolutePath().concat(File.separator + "guilds"));
        if (guildFolder.mkdir()) {
            bot.printInfo("created guilds directory");
        }
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
            logFile = tmp;
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
            logObj = (JSONObject) parser.parse(new FileReader(logFile));
        } catch (IOException | ParseException e) {
            bot.printInfo(e.getMessage());
        }
    }

    private synchronized void createGuild(final long guildId) {
        File guildConfig = new File(guildFolder.getAbsolutePath().concat(File.separator + guildId + ".json"));
        if (!guildConfig.exists()) {
           try {
               if (!guildConfig.createNewFile()) {
                   bot.printInfo("couldn't create guild config for " + guildId);
               } else {
                   bot.printInfo("created guild config for " + guildId);
               }
               writer = new FileWriter(guildConfig);
               writer.write("{}");
               writer.flush();
               writer.close();
           } catch (IOException e) {
               bot.printInfo(e.getMessage());
            }
        }
    }

    public synchronized int getVolume(final long guildId) {
        File guildConf = null;
        try {
            guildConf = getConfig(guildId);
        } catch (IllegalArgumentException e) {
            return 100;
        }
        JSONObject object;
        final int volume;
        try {
            JSONParser parser = new JSONParser();
            object = (JSONObject) parser.parse(new FileReader(guildConf));
            volume = Integer.parseInt((String) object.get("volume"));
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return volume;
    }

    public synchronized void setVolume(final long guildId, final int volume) {
        File guildConf = null;
        createGuild(guildId);
        try {
            guildConf = getConfig(guildId);
        } catch (IllegalArgumentException e) {
            bot.printInfo(e.getMessage());
            return;
        }
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(new FileReader(guildConf));
            object.remove("volume");
            object.put("volume", String.valueOf(volume));
            writer = new FileWriter(guildConf);
            writer.write(object.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            bot.printInfo(e.getMessage());
        }

    }

    private synchronized File getConfig(final long guildId) {
        for (File f : guildFolder.listFiles()) {
            if (f.getName().equals(guildId + ".json")) {
                return f;
            }
        }
        throw new IllegalArgumentException("couldn't find config for " + guildId);
    }

    public synchronized void addLog(@Nonnull GuildMessageReceivedEvent event, @Nonnull final ICommand cmd) {
        updateFile();
        JSONArray array = new JSONArray();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        array.add(event.getAuthor().getAsTag());
        array.add(event.getMessage().getContentRaw());
        array.add(cmd.getName());
        array.add(String.valueOf(event.getGuild()));
        array.add(event.getChannel().getName());
        logObj.put(dtf.format(now), array);
        try {
            if (!logFile.canWrite()) {
                bot.printInfo("Logger: Couldn't write log.json");
            }
            writer = new FileWriter(logFile);
            writer.write(logObj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

