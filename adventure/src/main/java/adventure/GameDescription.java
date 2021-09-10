/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.io.FileNotFoundException;
import java.io.IOException;
import parser.ParserOutput;
import type.AdvObject;
import type.Command;
import type.Room;
import type.Npc;
import type.Score;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che descrive la struttura che deve avere il gioco,
 * si possono implementare nuovi giochi andando ad estendere e implementare
 * questa classe
 * @author psp
 */
public abstract class GameDescription implements Serializable {

    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private final List<AdvObject> inventory = new ArrayList<>();

    private final List<Npc> npcs = new ArrayList<>();
    
    private final Score score = new Score();
    
    private Room currentRoom;

      public List<Room> getRooms() {
        return rooms;
    }

      /**
       * Funzione che dato il nome di una stanza ricerca
       * e restituisce la stanza associata tramite una pipeline
       * @param name nome della stanza 
       * @return 
       */
   public Room getRoom(String name) {
        return getRooms()
                .stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
   
   /**
       * Funzione che dato il nome di un personaggio ricerca
       * e restituisce il personaggio associato tramite una pipeline
       * @param name nome del personaggio
       * @return 
       */
   public Npc getNpc(String name) {
        return getNpcs()
                .stream()
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<AdvObject> getInventory() {
        return inventory;
    }

    public List<Npc> getNpcs() {
        return npcs;
    }
    
    /**
     * Funzione che restituisce i personaggi 'in vita'
     * @return 
     */
    public Npc getNpcLive() {
        for(Npc n : getNpcs()){
            if (n.isLive()){
                return n;
            }
        }
        return null;
    }
    
    /**
     * funzione che restituisce true se esiste almeno un npc 'in vita'
     * false altrimenti
     * @return 
     */
    public boolean NpcsAreLive(){
        return getNpcs()
                .stream()
                .anyMatch(n -> (n.isLive()));
    }

    public Score getScore() {
        return score;
    }
    
    public abstract void init() throws Exception; //inizzializzazione partita

    public abstract String nextMove(ParserOutput p, Engine engine);     //gestione mosse

    public abstract String save() throws FileNotFoundException, IOException, ClassNotFoundException; // Salvataggio della partita

    public abstract String load(Engine engine) throws FileNotFoundException, IOException, ClassNotFoundException; // Caricamento della partita
}
