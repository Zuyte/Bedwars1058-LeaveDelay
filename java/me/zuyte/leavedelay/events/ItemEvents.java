package me.zuyte.leavedelay.events;

import com.andrei1058.bedwars.api.arena.GameState;
import me.zuyte.leavedelay.Main;
import me.zuyte.leavedelay.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemEvents implements Listener {
    private int task1id;

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Main.getBedWars().getVersionSupport().getItemInHand(e.getPlayer()).getType() == null || Main.getBedWars().getVersionSupport().getItemInHand(e.getPlayer()).getType() == Material.AIR)
                return;
            try {
                if (Main.getBedWars().getVersionSupport().getItemInHand(e.getPlayer()).getType() != Material.getMaterial(Main.getCfg().getYml().getString(".item.material")))
                    return;
            } catch (Exception ex) {
                Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_MATERIAL from config.yml. Invalid material, Please check config.yml.");
            }
            if (!Main.getBedWars().getVersionSupport().isCustomBedWarsItem(Main.getBedWars().getVersionSupport().getItemInHand(e.getPlayer())) && !Main.getBedWars().getVersionSupport().getCustomData(Main.getBedWars().getVersionSupport().getItemInHand(e.getPlayer())).equals("BWLEAVE-DELAY"))
                return;
            if (Main.getBedWars().getArenaUtil().getArenaByPlayer(e.getPlayer()) == null)
                return;
            if (Main.getBedWars().getArenaUtil().getArenaByPlayer(e.getPlayer()).getStatus() == GameState.starting || Main.getBedWars().getArenaUtil().getArenaByPlayer(e.getPlayer()).getStatus() == GameState.waiting) {
                final Player p = e.getPlayer();
                if (!Main.getInstance().existClick(p.getUniqueId().toString()).booleanValue()) {
                    if (!Main.useLang.booleanValue()) {
                        try {
                            p.sendMessage(ColorUtil.getMsg(Main.getMsg().getYml().getString(".start")).replace("{delay}", Main.getCfg().getYml().getString(".settings.delay")));
                        } catch (Exception ex) {
                            Main.getInstance().getLogger().severe("An error occurred while getting MSG_CLICK_START from messages.yml");
                            ex.printStackTrace();
                            return;
                        }
                    } else {
                        try {
                            p.sendMessage(ColorUtil.getMsg(Main.getBedWars().getPlayerLanguage(e.getPlayer()).getString(".addons.leavedelay.messages.start")).replace("{delay}", Main.getCfg().getYml().getString(".settings.delay")));
                        } catch (Exception ex) {
                            Main.getInstance().getLogger().severe("An error occurred while getting LANG_CLICK_START from messages.yml");
                            ex.printStackTrace();
                            return;
                        }
                    }
                    Main.getInstance().setClick(p.getUniqueId().toString());
                    Long delay = Long.valueOf(0L);
                    try {
                        delay = Long.valueOf(Long.parseLong(Main.getCfg().getYml().getString(".settings.delay")) * 20L);
                    } catch (Exception ex) {
                        Main.getInstance().getLogger().severe("An error occurred while getting CFG_SET_DELAY from config.yml");
                        ex.printStackTrace();
                        return;
                    }
                    BukkitRunnable task1 = new BukkitRunnable() {
                        public void run() {
                            if (!Main.getInstance().existClick(p.getUniqueId().toString()).booleanValue()) {
                                Main.getInstance().removeClick(p.getUniqueId().toString());
                                return;
                            }
                            if (Main.getBedWars().getArenaUtil().getArenaByPlayer(p) == null) {
                                Main.getInstance().removeClick(p.getUniqueId().toString());
                                return;
                            }
                            if (Main.getBedWars().getArenaUtil().getArenaByPlayer(p).getStatus() == GameState.waiting || Main.getBedWars().getArenaUtil().getArenaByPlayer(p).getStatus() == GameState.starting) {
                                try {
                                    p.performCommand("leave");
                                } catch (Exception exception) {}
                                Main.getInstance().removeClick(p.getUniqueId().toString());
                            } else {
                                Main.getInstance().removeClick(p.getUniqueId().toString());
                                return;
                            }
                        }
                    };
                    this.task1id = Bukkit.getScheduler().runTaskLater(Main.getInstance(), task1, delay.longValue()).getTaskId();
                } else if (!Main.useLang.booleanValue()) {
                    try {
                        Main.getInstance().removeClick(p.getUniqueId().toString());
                        Bukkit.getScheduler().cancelTask(this.task1id);
                        p.sendMessage(ColorUtil.getMsg(Main.getMsg().getYml().getString(".cancel")));
                    } catch (Exception ex) {
                        Main.getInstance().getLogger().severe("An error occurred while getting MSG_CLICK_CANCEL from messages.yml");
                        ex.printStackTrace();
                        return;
                    }
                } else {
                    try {
                        Main.getInstance().removeClick(p.getUniqueId().toString());
                        Bukkit.getScheduler().cancelTask(this.task1id);
                        p.sendMessage(ColorUtil.getMsg(Main.getBedWars().getPlayerLanguage(e.getPlayer()).getString(".addons.leavedelay.messages.cancel")));
                    } catch (Exception ex) {
                        Main.getInstance().getLogger().severe("An error occurred while getting LANG_CLICK_CANCEL from messages.yml");
                        ex.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}
