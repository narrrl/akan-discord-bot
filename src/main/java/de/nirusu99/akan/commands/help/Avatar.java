package de.nirusu99.akan.commands.help;

import de.nirusu99.akan.commands.CommandContext;
import de.nirusu99.akan.commands.ICommand;
import de.nirusu99.akan.utils.Const;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.kohsuke.MetaInfServices;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link ICommand} sends the avatars for given users embedded.
 *
 * @author Nils Pukropp
 * @since 1.0
 */
@MetaInfServices(ICommand.class)
public final class Avatar  implements ICommand {
    private static final Pattern PATTERN = Pattern.compile("avatar( " + Const.USER_REGEX + ")+");

    @Override
    public void run(final CommandContext ctx) {
        List<Member> users = ctx.getMessage().getMentionedMembers();
        for (Member m : users) {
            final EmbedBuilder emb = new EmbedBuilder();
            emb.setTitle(m.getUser().getAsTag(),m.getUser().getAvatarUrl())
                    .setImage(m.getUser().getEffectiveAvatarUrl() + "?size=512")
            .setColor(Color.PINK);
            ctx.getChannel().sendTyping().queue(rep ->
                    ctx.getChannel().sendMessage(emb.build()).queue());
        }

    }

    @Override
    public String getName() {
        return "avatar";
    }

    @Override
    public String syntax() {
        return "<prefix>avatar <@User>";
    }

    @Override
    public String gifHelpUrl() {
        return null;
    }

    @Override
    public String toString() {
        return "Gets the avatar of the specified users";
    }

    @Override
    public Matcher matches(String input) {
        return PATTERN.matcher(input);
    }
}
