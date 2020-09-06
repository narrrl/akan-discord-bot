package de.nirusu99.akan.core;

import java.util.HashMap;

import de.nirusu99.akan.AkanBot;

public class GuildManager {

    private static HashMap<Long, GuildManager> gMaps;
    private final long guildId;
    private final AkanBot bot;
    private int volume = 100;
    private String prefix = AkanBot.PREFIX;

    public static HashMap<Long, GuildManager> getGuildManagers() {
        if (gMaps == null) {
            gMaps = new HashMap<>();
        }

        return gMaps;
    }



    public GuildManager(final AkanBot bot, final long guildId) {
        this.guildId = guildId;
        this.bot = bot;

        Logger logger = bot.getLogger();

        try {
            volume = Integer.parseInt(String.valueOf(logger.getValueForGuild("volume", guildId)));
        } catch (IllegalArgumentException e) {
            logger.writeValueForGuild("volume", volume, guildId);
        }

        try {
            prefix = (String) logger.getValueForGuild("prefix", guildId);
        } catch (IllegalArgumentException e) {
            logger.writeValueForGuild("prefix", prefix, guildId);
        }
        

    }



    public long getGuildId() {
        return this.guildId;
    }



	public static GuildManager getManager(long idLong, final AkanBot bot) {
        GuildManager gm = getGuildManagers().get(idLong);
        if (gm == null) {
            gm = new GuildManager(bot, idLong);
            getGuildManagers().put(idLong, gm);
        }
        return gm;
	}



	public String getPrefix() {
		return this.prefix;
	}



	public boolean setPrefix(String newPrefix) {
        Logger logger = bot.getLogger();
        try {
            logger.writeValueForGuild("prefix", newPrefix, this.guildId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        this.prefix = newPrefix;
        return true;
	}



	public boolean withSuccessReaction() {
        Logger logger = bot.getLogger();
        boolean withReaction;
        try {
            withReaction = (boolean) logger.getValueForGuild("reaction", guildId);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return withReaction;
    }
    
    public boolean setSuccessReaction(final boolean b) {
        Logger logger = bot.getLogger();
        try {
            logger.writeValueForGuild("reaction", b, guildId);
        } catch ( IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    
}
