package be.rivzer.lootdrop.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class onLootDropOpenEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    Player p;
    Location l;

    public onLootDropOpenEvent(Player p, Location loca){
        this.p = p;
        this.l = loca;
    }

    public Player getPlayer(){
        return p;
    }

    public Location getLocation(){
        return l;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
