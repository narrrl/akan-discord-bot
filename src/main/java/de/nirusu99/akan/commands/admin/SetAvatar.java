package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.Error;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Message;
import org.kohsuke.MetaInfServices;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} sets the avatar of the bot account with
 * {@link net.dv8tion.jda.api.managers.AccountManager#setAvatar(Icon)}. The new avatar should be
 * attached to the user message. Only bot owner can set the avatar {@link AkanBot#userIsOwner(User).}
 *
 * @author Nils Pukropp
 * @since 1.1
 */
@MetaInfServices(ICommand.class)
public final class SetAvatar implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("setavatar");

    @Override
    public void run(@Nonnull CommandContext ctx) {
        if (AkanBot.userIsOwner(ctx.getAuthor())) {
            List<Message.Attachment> attachment = ctx.getMessage().getAttachments();
            if (attachment.size() != 1) {
                ctx.reply("You must attach an image");
                return;
            }
            try {
                InputStream s = new URL(attachment.get(0).getUrl()).openStream();
                ctx.getJDA().getSelfUser().getManager().setAvatar(Icon.from(s)).queue();
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
            ctx.reply("Updated avatar!");
        } else {
            ctx.reply(Error.NOT_OWNER.toString());
        }
    }

    @Override
    public String getName() {
        return "setavatar";
    }

    @Override
    public String toString() {
        return "Sets the avatar of the bot. You have to attach a picture to the message!";
    }

    @Override
    public String syntax() {
        return null;
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
