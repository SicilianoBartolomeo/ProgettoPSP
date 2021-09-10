/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity
 * Classe che rappresenta le stanze del gioco
 * @author psp
 */
public class Room implements Serializable{

    private final int id;

    private String name;

    private String look;

    private boolean visible;

    private boolean accessible;

    private Room south;

    private Room north;

    private Room east;

    private Room west;

    private final List<AdvObject> objects = new ArrayList<>();

    private List<Npc> npcs = new ArrayList<>();

    public Room(int id) {
        this.id = id;
        this.name = new String();
        this.look = new String();
        this.visible = true;
        this.accessible = true;
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
    }

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.look = new String();
        this.visible = true;
        this.accessible = true;
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
    }


    public List<Npc> getNpcs() {
        return npcs;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        return this.id == other.id;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

}
