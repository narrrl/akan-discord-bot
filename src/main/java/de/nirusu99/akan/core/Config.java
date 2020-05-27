package de.nirusu99.akan.core;

import net.dv8tion.jda.api.entities.Emote;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class Config {
    final JSONObject obj;
    final File file;
    FileWriter writer;

    public Config() {
        file = new File(new File("").getAbsolutePath().concat(File.separator + "config.json"));
        try {
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String getToken() {
        String token = (String) obj.get("token");
        if (token == null) {
            throw new IllegalArgumentException("token is empty");
        }
        return token;
    }

    public String getPrefix() {
        String prefix = (String) obj.get("prefix");
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is empty");
        }
        return prefix;
    }

    public boolean withSuccessReaction() {
        return Boolean.parseBoolean((String) obj.get("successReaction"));
    }

    public void setCheckMark(final boolean b) {
        obj.remove("successReaction");
        obj.put("successReaction",String.valueOf(b));
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
