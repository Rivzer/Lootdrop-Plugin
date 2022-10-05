package be.rivzer.lootdrop.Listeners;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Helpers.TimerAS;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClose implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();

        if(e.getInventory() == null) return;

        if(!(e.getInventory().getName().equals(Logger.color(Config.getCustomConfig1().getString("Settings.LootDropGui.Name"))))) return;

        if(!LootDropSpawn.data.containsKey(p.getUniqueId())){
            return;
        }

        Main.bezig.remove(LootDropSpawn.data.get(p.getUniqueId()));

        for(ItemStack item : e.getInventory().getContents()) {
            if(item != null) return;
        }

        Location loca = LootDropSpawn.data.get(p.getUniqueId()).add(0.5, 2.3, 0.5);
        TimerAS.textHolo.get(loca).delete();
    }
}
