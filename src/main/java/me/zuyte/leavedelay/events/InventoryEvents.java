package me.zuyte.leavedelay.events;

import me.zuyte.leavedelay.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Main.getBedWars().getVersionSupport().isCustomBedWarsItem(e.getCurrentItem()) && Main.getBedWars().getVersionSupport().getCustomData(e.getCurrentItem()).equals("BWLEAVE-DELAY")) {
            e.setCancelled(false);
        }
    }
}
