package be.rivzer.lootdrop.Guis;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Logger;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LootDrop {

    public static void openGUI(Player p, Location location){
        Inventory inv = Bukkit.createInventory(p, 45, Logger.color(Config.getCustomConfig1().getString("Settings.LootDropGui.Name")));

        //SET ITEMS BASED ON LOOTDROP && NOT IF ALREADY BEEN OPENED ONES
        List<String> itemlijst = Config.getCustomConfig3().getStringList("List");
        String[] items = (String[])itemlijst.toArray(new String[0]);
        String[] var1 = items;

        for(int i = 0; i < items.length; ++i) {
            String id = var1[i];

            if(Config.getCustomConfig3().getString("Crates." + id) == null) return;

            if(Config.getCustomConfig3().getString("Crates." + id + ".Cords.World").equals(location.getWorld().getName())
            && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.X") == location.getBlockX()
            && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.Y") == location.getBlockY()
            && Config.getCustomConfig3().getInt("Crates." + id + ".Cords.Z") == location.getBlockZ()) {
                String crateType = Config.getCustomConfig3().getString("Crates." + id + ".Type");

                List<String> itemlijst2 = Config.getCustomConfig3().getStringList("Crates." + id + ".LootList");
                String[] items2 = (String[]) itemlijst2.toArray(new String[0]);
                String[] var12 = items2;

                for (int ii = 0; ii < items2.length; ++ii) {
                    String idd = var12[ii];

                    if (Config.getCustomConfig3().getString("Crates." + id + "." + idd) == null){
                        ii++;
                        idd = var12[ii];
                    }

                    setItem(inv,
                            Material.valueOf(Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".Loot.Material").toUpperCase()),
                            Config.getCustomConfig3().getInt("Crates." + id + "." + idd + ".Loot.Ammount"),
                            ii,
                            Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".Loot.Name"),
                            Config.getCustomConfig3().getString("Crates." + id + "." + idd + ".Loot.NBT"),
                            p);

                    p.closeInventory();
                    p.openInventory(inv);
                }
            }
        }
    }

    public static void setItem(Inventory inv, Material mat, Integer ammount, Integer slotnum, String name, String nbteNaam, Player p){
        ItemStack item = new ItemStack(mat, ammount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Logger.color(name));
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setString("mtcustom", nbteNaam);

        ItemStack item1 = nbti.getItem();

        inv.setItem(slotnum, item1);
    }

}
