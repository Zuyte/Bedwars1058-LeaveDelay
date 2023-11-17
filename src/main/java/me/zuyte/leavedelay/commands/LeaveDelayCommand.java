package me.zuyte.leavedelay.commands;

import me.zuyte.leavedelay.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LeaveDelayCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (!p.hasPermission("bw.leavedelay"))
                return true;
            if (!Main.useLang.booleanValue()) {
                Main.getCfg().reload();
                Main.getMsg().reload();
                p.sendMessage(ChatColor.GREEN + "Reloaded config.yml & messages.yml");
            } else {
                Main.getCfg().reload();
                p.sendMessage(ChatColor.GREEN + "Reloaded config.yml");
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender c = (ConsoleCommandSender)sender;
            if (!Main.useLang.booleanValue()) {
                Main.getCfg().reload();
                Main.getMsg().reload();
                c.sendMessage(ChatColor.GREEN + "Reloaded config.yml & messages.yml");
            } else {
                Main.getCfg().reload();
                c.sendMessage(ChatColor.GREEN + "Reloaded config.yml");
            }
        }
        return false;
    }
}