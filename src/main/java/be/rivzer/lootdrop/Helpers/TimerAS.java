package be.rivzer.lootdrop.Helpers;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Events.onLootDropSpawnEvent;
import be.rivzer.lootdrop.Listeners.LootDropSpawn;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TimerAS extends BukkitRunnable {

    private final Main plugin = Main.getPlugin(Main.class);
    private final HashMap<UUID, ArmorStand> ash = new HashMap<>();
    private final HashMap<UUID, Location> locaspawned = new HashMap<>();
    private final HashMap<UUID, String> typeCrate = new HashMap<>();
    public static final HashMap<Location, Hologram> textHolo = new HashMap<>();
    private final Player p;
    public HashMap<Integer, Double> data = new HashMap<>();

    public TimerAS(Location location, Player p, Location spawned, String type){
        location = location.add(0.5, 2, 0.5);
        this.p = p;

        ArmorStand as = Bukkit.getWorld(location.getWorld().getName()).spawn(location, ArmorStand.class);
        as.setBasePlate(false);
        as.setArms(false);
        as.setCanPickupItems(false);
        as.setGravity(true);
        as.setVisible(false);
        as.teleport(location);
        ItemStack item = new ItemStack(Material.GOLD_SPADE, 1);
        NBTItem nbti = new NBTItem(item);
        nbti.setString("dgmt", "supplydrop");
        ItemStack item1 = nbti.getItem();
        as.setHelmet(item1);
        as.setCustomName("supplydrop");

        typeCrate.put(p.getUniqueId(), type);
        ash.put(p.getUniqueId(), as);
        locaspawned.put(p.getUniqueId(), spawned);
    }

    @Override
    public void run(){
        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                if(ash.containsKey(p.getUniqueId())){
                    if(!LootDropSpawn.data.containsKey(p.getUniqueId())){
                        try{
                            ash.get(p.getUniqueId()).remove();
                            p.sendMessage(Logger.color("&cEr is iets fout gegaan, kon de cordinaten niet ophalen!"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        return;
                    }
                    if(!ash.get(p.getUniqueId()).isOnGround()){
                        //SNELHEID NAAR BENEDEN
                        double drop = Config.getCustomConfig1().getDouble("Settings.Snelheden.Drop");
                        final Vector direction = LootDropSpawn.data.get(p.getUniqueId()).getDirection();
                        ash.get(p.getUniqueId()).setVelocity(direction.multiply(0).setY(drop));

                        //DRAAIEN
                        double draaien = Config.getCustomConfig1().getDouble("Settings.Snelheden.Draaien");
                        if(data.containsKey(ash.get(p.getUniqueId()).getEntityId())){
                            data.put(ash.get(p.getUniqueId()).getEntityId(), (data.get(ash.get(p.getUniqueId()).getEntityId())+draaien));
                            ash.get(p.getUniqueId()).setHeadPose(new EulerAngle(0, data.get(ash.get(p.getUniqueId()).getEntityId()), 0));
                        }
                        else{
                            data.put(ash.get(p.getUniqueId()).getEntityId(), 0.01);
                        }
                    }
                    else {
                        Location prc = Main.lootdrop.get(p.getUniqueId());
                        Block b = prc.getBlock();
                        b.setType(Material.PURPUR_PILLAR);

                        if(ash.get(p.getUniqueId()).getCustomName().equals("supplydrop")){
                            ash.get(p.getUniqueId()).remove();
                        }

                        Location where = Main.lootdrop.get(p.getUniqueId());
                        where = where.add(0.5, 2.3, 0.5);
                        Hologram crate = HologramsAPI.createHologram(plugin, where);
                        crate.appendTextLine(Logger.color("&e&lLootdrop"));
                        crate.insertTextLine(1, Logger.color("&e&lType: &6&l" + typeCrate.get(p.getUniqueId())));

                        textHolo.put(where, crate);

                        (new Counter(plugin, p, where)).runTaskTimer(plugin, 0L, 20L);

                        List list = Config.getCustomConfig3().getStringList("List");
                        list.add(list.size()+1);
                        Config.getCustomConfig3().set("List", list);
                        Config.getCustomConfig3().set("Crates." + list.size() + ".Cords.World", b.getWorld().getName());
                        Config.getCustomConfig3().set("Crates." + list.size() + ".Cords.X", b.getX());
                        Config.getCustomConfig3().set("Crates." + list.size() + ".Cords.Y", b.getY());
                        Config.getCustomConfig3().set("Crates." + list.size() + ".Cords.Z", b.getZ());
                        Config.getCustomConfig3().set("Crates." + list.size() + ".Type", typeCrate.get(p.getUniqueId()));

                        List<String> itemlijst = Config.getCustomConfig2().getStringList("LootDrops." + typeCrate.get(p.getUniqueId()) + ".LootList");
                        String[] items = (String[])itemlijst.toArray(new String[0]);
                        String[] var1 = items;

                        for(int i = 0; i < items.length; ++i) {
                            String id = var1[i];
                            List lootList = Config.getCustomConfig3().getStringList("Crates." + list.size() + ".LootList");
                            lootList.add("L-" + (i+1));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".LootList", lootList);

                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.Name", Config.getCustomConfig2().getString("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".Name"));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.Ammount", Config.getCustomConfig2().getInt("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".Ammount"));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.NBT", Config.getCustomConfig2().getString("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".NBT"));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.Type", Config.getCustomConfig2().getString("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".Type"));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.Material", Config.getCustomConfig2().getString("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".Material").toUpperCase());
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".Loot.Data", Config.getCustomConfig2().getInt("LootDrops." + typeCrate.get(p.getUniqueId()) + ".Loot." + id + ".Data"));
                            Config.getCustomConfig3().set("Crates." + list.size() + ".L-" + lootList.size() + ".EventData", (i+1));
                        }

                        Config.saveConfig3();

                        p.sendMessage(Logger.color("&eUw &6&l" + typeCrate.get(p.getUniqueId())
                                + " &elootdrop is geland op &7X: &e"
                                + ash.get(p.getUniqueId()).getLocation().getBlockX()
                                + " &7Y: &e" + ash.get(p.getUniqueId()).getLocation().getBlockY()
                                + " &7Z: &e" + ash.get(p.getUniqueId()).getLocation().getBlockZ()));

                        typeCrate.remove(p.getUniqueId());
                        data.remove(ash.get(p.getUniqueId()).getEntityId());
                        Main.oproeploca.remove(locaspawned.get(p.getUniqueId()));
                        Bukkit.getServer().getPluginManager().callEvent(new onLootDropSpawnEvent(p, locaspawned.get(p.getUniqueId())));
                        locaspawned.remove(p.getUniqueId());
                        ash.remove(p.getUniqueId());
                        Main.lootdrop.remove(p.getUniqueId());
                    }
                }
            }
        }, 0, 0);
    }
}