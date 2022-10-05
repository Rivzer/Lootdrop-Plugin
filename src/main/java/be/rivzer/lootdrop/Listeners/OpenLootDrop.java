package be.rivzer.lootdrop.Listeners;

import be.rivzer.lootdrop.Events.onLootDropOpenEvent;
import be.rivzer.lootdrop.Guis.LootDrop;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class OpenLootDrop implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getClickedBlock().getType() == Material.PURPUR_PILLAR){
                if(e.getHand() != EquipmentSlot.HAND) return;
                LootDropSpawn.data.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
                if(!LootDropSpawn.data.containsKey(e.getPlayer().getUniqueId())){
                    e.getPlayer().sendMessage(Logger.color("&cEr is een fout opgetreden!"));
                    return;
                }
                if(Main.bezig.contains(e.getClickedBlock().getLocation())) return;
                Main.bezig.add(e.getClickedBlock().getLocation());
                LootDrop.openGUI(e.getPlayer(), e.getClickedBlock().getLocation());
                Bukkit.getServer().getPluginManager().callEvent(new onLootDropOpenEvent(e.getPlayer(), e.getClickedBlock().getLocation()));
            }
        }
    }

}
