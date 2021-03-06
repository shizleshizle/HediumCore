package nl.shizleshizle.hediumcore.listeners;

import nl.shizleshizle.hediumcore.Main;
import nl.shizleshizle.hediumcore.objects.User;
import nl.shizleshizle.hediumcore.permissions.Perm;
import nl.shizleshizle.hediumcore.permissions.PermAttachments;
import nl.shizleshizle.hediumcore.permissions.PermGroup;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User p = new User(event.getPlayer());
        event.setJoinMessage(ChatColor.GOLD + "Hedium" + ChatColor.DARK_AQUA + " >> " + ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " has joined the game.");
        Main.sql.enterUserInDatabase(p);
        if (!p.hasPlayedBefore() || p.getGroup() == null) {
            p.setGroup(PermGroup.MEMBER);
        }
        Perm.loginPlayer(p.getName());
        PermAttachments.addPerms(p);
        if (p.hasNick()) {
            p.loadNick();
        }
    }
}
