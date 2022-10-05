package be.rivzer.lootdrop;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Listeners.InventoryClick;
import be.rivzer.lootdrop.Listeners.InventoryClose;
import be.rivzer.lootdrop.Listeners.LootDropSpawn;
import be.rivzer.lootdrop.Listeners.OpenLootDrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static HashMap<UUID, Location> lootdrop = new HashMap<>();
    public static ArrayList<Location> oproeploca = new ArrayList<>();
    public static ArrayList<Location> bezig = new ArrayList<>();

    @Override
    public void onEnable() {

        Config.createCustomConfig1();
        Config.createCustomConfig2();
        Config.createCustomConfig3();

        Bukkit.getPluginManager().registerEvents(new LootDropSpawn(), this);
        Bukkit.getPluginManager().registerEvents(new OpenLootDrop(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClose(), this);

        System.out.print(Logger.color("&aPlugin &b&lLootDrop &ais opgestart! \n"));

    }

    @Override
    public void onDisable() {



    }
}
