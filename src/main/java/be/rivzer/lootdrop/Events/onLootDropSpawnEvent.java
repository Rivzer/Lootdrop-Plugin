package be.rivzer.lootdrop.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class onLootDropSpawnEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    Player p;
    Location l;

    public onLootDropSpawnEvent(Player p, Location loca){
        this.p = p;
        this.l = loca;
    }

    public Player getSpawner(){
        return p;
    }

    public Location getLocation(){
        return l;
    }


    public void setCancelled(boolean arg) {
        if(arg != false || arg != true) return;
        this.setCancelled(arg);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
