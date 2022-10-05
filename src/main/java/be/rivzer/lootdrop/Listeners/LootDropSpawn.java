package be.rivzer.lootdrop.Listeners;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Helpers.TimerAS;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LootDropSpawn implements Listener {

    private Main plugin = Main.getPlugin(Main.class);
    public static HashMap<UUID, Location> data = new HashMap<>();
    Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();
        Location loca = b.getLocation();
        Location yc;
        int hoogte = Config.getCustomConfig1().getInt("Settings.Spawn.HoogteSpawn");
        int ytc = loca.getBlockY();

        if(p.getInventory() == null) return;
        if(p.getInventory().getItemInMainHand() == null) return;
        if(p.getInventory().getItemInMainHand().getItemMeta() == null) return;
        if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;

        if(e.getBlock() == null) return;

        List<String> itemlijst = Config.getCustomConfig2().getStringList("List");
        String[] items = (String[])itemlijst.toArray(new String[0]);
        String[] var1 = items;

        for(int i = 0; i < items.length; ++i) {
            String lootDropNaam = var1[i];

            if(Config.getCustomConfig2().getString("LootDrops." + lootDropNaam) != null){
                if(ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase(Config.getCustomConfig2().getString("LootDrops." + lootDropNaam + ".TorchName"))){
                    Material mat = Material.valueOf(Config.getCustomConfig2().getString("LootDrops." + lootDropNaam + ".Material"));

                    if(e.getBlock().getType() != mat) return;

                    int x = b.getX();
                    int y = b.getY();
                    int z = b.getZ();

                    e.setCancelled(true);

                    if(cooldowns.containsKey(p.getUniqueId())) {
                        if(cooldowns.get(p.getUniqueId()) > System.currentTimeMillis()){
                            long timeLeft = (cooldowns.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000;
                            p.sendMessage(Logger.color("&cU heeft nog een cooldown, u moet nog &4" + timeLeft + " &cseconden wachten."));
                            return;
                        }
                    }

                    if(Main.lootdrop.containsKey(p.getUniqueId())){
                        p.sendMessage(Logger.color("&cU bent al een lootdrop aan oproepen!"));
                        return;
                    }

                    if(Main.oproeploca.contains(b.getLocation())){
                        p.sendMessage(Logger.color("&cEr word hier al een lootdrop opgeroepen!"));
                        return;
                    }

                    x++;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    x--;
                    x--;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    x++;

                    z++;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    z--;
                    z--;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    z++;

                    y++;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    y--;
                    y--;
                    if(Bukkit.getServer().getWorld(b.getWorld().getName()).getBlockAt(x, y, z).getType().equals(Material.PURPUR_PILLAR)){
                        p.sendMessage(Logger.color("&cU kan hier geen lootdrop oproepen!"));
                        return;
                    }
                    y++;

                    ytc++;

                    for (int h = 0; h < hoogte; h++){
                        yc = new Location(loca.getWorld(), loca.getBlockX(), ytc, loca.getBlockZ());

                        if(yc.getBlock().getType() != Material.AIR){
                            p.sendMessage(Logger.color("&cU lootdrop kon hier niet opgeroepen worden!"));
                            return;
                        }

                        ytc++;
                    }

                    cooldowns.put(p.getUniqueId(), System.currentTimeMillis() + (5 * 1000));
                    Main.lootdrop.put(p.getUniqueId(), e.getBlock().getLocation());
                    Main.oproeploca.add(b.getLocation());
                    data.put(p.getUniqueId(), b.getLocation());

                    int y1 = loca.getBlockY();
                    y1 = (y1 + hoogte);

                    loca = new Location(loca.getWorld(), loca.getX(), y1, loca.getZ());

                    (new TimerAS(loca, p, b.getLocation(), ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()))).runTask(plugin);

                    ItemStack item = new ItemStack(p.getInventory().getItemInMainHand().getType(), p.getInventory().getItemInMainHand().getAmount()-1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Logger.color(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
                    item.setItemMeta(meta);
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), item);

                    p.updateInventory();

                    p.sendMessage(Logger.color("&eUw &6&l" + lootDropNaam + " &elootdrop is onderweg!"));

                    return;
                }
            }
        }
    }
}
