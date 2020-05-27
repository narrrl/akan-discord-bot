package de.nirusu99.akan.commands.admin;

import de.nirusu99.akan.AkanBot;
import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.commands.Error;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;

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
public final class SetAvatar implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("setavatar");

    @Override
    public void run(CommandContext cfx) {
        if(AkanBot.userIsOwner(cfx.getAuthor())) {
            List<Message.Attachment> attachment = cfx.getMessage().getAttachments();
            if (attachment.size() != 1) {
                cfx.getChannel().sendTyping().queue(rep ->
                        cfx.getChannel().sendMessage("You must attach a image.").queue());
                return;
            }
            try {
                InputStream s = new URL(attachment.get(0).getUrl()).openStream();
                cfx.getJDA().getSelfUser().getManager().setAvatar(Icon.from(s)).queue(rep ->
                        cfx.getChannel().sendTyping().queue( msg ->
                                cfx.getChannel().sendMessage("updated avatar!").queue()));
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            cfx.getChannel().sendTyping().queue(rep ->
                    cfx.getChannel().sendMessage(Error.NOT_OWNER.toString()).complete());
        }
    }

    @Override
    public String getName() {
        return "setavatar";
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
