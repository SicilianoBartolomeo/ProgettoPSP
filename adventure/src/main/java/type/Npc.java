/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity
 * Classe che rappresenta gli npc:
 * ogni npc ha un id, un nome, un attributo usato per verificare
 * se e' in vita, un insieme di sinonimi e una lista di oggetti.
 * @author Barty
 */
public class Npc implements Serializable{

    private final int id;

    private final String name;
    
    private boolean live;

    private Set<String> alias;
    
    private final List<AdvObject> objects = new ArrayList<>();

    public Npc(int id, String name) {
        this.id = id;
        this.name = name; 
        this.live = false;
    }

    public Npc(int id, String name, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.live = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }
    
    public void dropObj(Room currentRoom){
        currentRoom.getObjects().addAll(this.getObjects());
    }
    
}
