package be.rivzer.lootdrop.Listeners;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Guis.LootDrop;
import be.rivzer.lootdrop.Helpers.TimerAS;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.CHEST) return;

        if(e.getInventory().getName().equalsIgnoreCase(Logger.color(Config.getCustomConfig1().getString("Settings.LootDropGui.Name")))){
            if(String.valueOf(e.getClick()).contains("SHIFT")){
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if(!LootDropSpawn.data.containsKey(p.getUniqueId())){
                p.closeInventory();
                p.sendMessage(Logger.color("&cU kan deze lootdrop niet openen!"));
                return;
            }

            e.setCancelled(true);

            Location loca = LootDropSpawn.data.get(p.getUniqueId());

            ItemStack is = e.getCurrentItem();
            if(is == null) return;

            List<String> itemlijst = Config.getCustomConfig3().getStringList("List");
            String[] items = (String[])itemlijst.toArray(new String[0]);
            String[] var1 = items;

            for(int i = 0; i < items.length; ++i) {
                String id = var1[i];

                if(Config.getCustomConfig3().getString("Crates." + id + ".Cords.World").equals(loca.getWorld().getName())
                        && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.X") == loca.getBlockX()
                        && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.Y") == loca.getBlockY()
                        && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.Z") == loca.getBlockZ()){
                    String crateType = Config.getCustomConfig3().getString("Crates." + id + ".Type");

                    List<String> itemlijst2 = Config.getCustomConfig3().getStringList("Crates." + id + ".LootList");
                    String[] items2 = (String[])itemlijst2.toArray(new String[0]);
                    String[] var12 = items2;

                    for(int ii = 0; ii < items2.length; ++ii) {
                        String idd = var12[ii];

                        if(Material.valueOf(Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".Loot.Material").toUpperCase()) == is.getType()
                        && Logger.color(Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".Loot.Name")).equalsIgnoreCase(Logger.color(is.getItemMeta().getDisplayName()))){
                            ItemStack itemm = new ItemStack(e.getCurrentItem());

                            e.getClickedInventory().remove(itemm);

                            if(Config.getCustomConfig3().getString("Crates." + id + "." + idd) != null){
                                p.getInventory().addItem(itemm);
                            }

                            List lijst = Config.getCustomConfig3().getStringList("Crates." + id + ".LootList");

                            lijst.remove("L-" + Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".EventData"));

                            Config.getCustomConfig3().set("Crates." + id + ".LootList", lijst);

                            Config.getCustomConfig3().set("Crates." + id + "." + idd, null);

                            Config.saveConfig3();

                            if(lijst.size() == 0){
                                List ml = Config.getCustomConfig3().getStringList("List");
                                ml.remove(id);
                                Config.getCustomConfig3().set("List", ml);
                                Config.getCustomConfig3().set("Crates." + id, null);
                                Config.saveConfig3();
                                loca.getBlock().setType(Material.AIR);
                                if(Main.bezig.contains(LootDropSpawn.data.get(p.getUniqueId()))){
                                    Main.bezig.remove(LootDropSpawn.data.get(p.getUniqueId()));
                                }
                            }
                        }
                    }
                }
            }

            for(ItemStack item : e.getInventory().getContents()) {
                if(item != null) return;
            }

            loca.getBlock().setType(Material.AIR);

            try{
                p.closeInventory();
            }
            catch (Exception eb){
                return;
            }
        }
    }
}
