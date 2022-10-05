package be.rivzer.lootdrop.Helpers;

import be.rivzer.lootdrop.Config.Config;
import be.rivzer.lootdrop.Logger;
import be.rivzer.lootdrop.Main;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Counter extends BukkitRunnable {
    public int seconds;
    public int minuten;
    public Main plugin;
    public Player player;
    public Location c;
    public TextLine tx;

    public Counter(Main plugin, Player player, Location b) {
        this.plugin = plugin;
        this.seconds = 60;
        this.minuten = Config.getCustomConfig1().getInt("Settings.Countdown")-1;
        this.c = b;
        this.player = player;
        this.tx = TimerAS.textHolo.get(this.c).insertTextLine(2, Logger.color("&f" + this.minuten + ":" + this.seconds));
    }

    public void run() {
        --seconds;
        if (seconds == -1) {
            seconds = 60;
            --minuten;
        }

        if(TimerAS.textHolo.containsKey(c)){
            tx.setText(Logger.color("&f" + minuten + ":" + seconds));
        }

        if (seconds == 0 && minuten == 0) {
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                TimerAS.textHolo.get(c).delete();
            },15);
            if(TimerAS.textHolo.containsKey(c)){
                TimerAS.textHolo.get(c).delete();
                int y = c.getBlockY();
                y--;
                y--;
                Block b = Bukkit.getServer().getWorld(c.getWorld().getName()).getBlockAt(c.getBlockX(), y, c.getBlockZ());
                b.setType(Material.AIR);
            }
        }
    }
}