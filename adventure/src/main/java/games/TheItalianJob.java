/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package games;

import adventure.Engine;
import adventure.GameDescription;
import commandManager.CommandManager;
import database.DBManager;
import gameEntity.NpcDescription;
import gameEntity.ObjectDescription;
import gameEntity.RoomDescription;
import parser.ParserOutput;
import type.AdvObject;
import type.AdvObjectContainer;
import type.Command;
import type.CommandType;
import type.Npc;
import type.Room;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe che implementa il gioco ed eredita e implementa la classe astratta
 * GameDescription
 *
 * @author Barty
 */
public class TheItalianJob extends GameDescription {

    //path del file contenente il file di salvataggio della partita
    private final String SALVATAGGIO_FILE_PATH = "./resources/file/salvataggio.dat";

    /**
     * metodo che inizializza il gioco
     */
    @Override
    public void init() {
        initCommand();
        initRoom();
        initNpc();
        initObject();
    }

    /**
     * Metodo per inizializzare le stanze e la mappa del gioco
     */
    private void initRoom() {

        Room hall = new Room(0, RoomDescription.HALL_NAME);
        Room corridor = new Room(1, RoomDescription.CORRIDOR_NAME);
        Room relaxRoom = new Room(2, RoomDescription.RELAX_ROOM_NAME);
        Room electricityRoom = new Room(3, RoomDescription.ELECTRICITY_ROOM_NAME);
        Room directorRoom = new Room(4, RoomDescription.DIRECTOR_ROOM_NAME);
        directorRoom.setAccessible(false);
        Room secretRoom = new Room(5, RoomDescription.SECRET_ROOM_NAME);
        secretRoom.setAccessible(false);
        Room securityRoom = new Room(6, RoomDescription.SECURITY_ROOM_NAME);
        securityRoom.setAccessible(false);
        Room boilerRoom = new Room(7, RoomDescription.BOILER_ROOM_NAME);
        boilerRoom.setVisible(false);
        Room caveau = new Room(8, RoomDescription.CAVEAU_NAME);
        caveau.setAccessible(false);
        Room exit = new Room(9, RoomDescription.EXIT_ROOM_NAME);
        exit.setAccessible(false);

        getRooms().add(hall);
        getRooms().add(corridor);
        getRooms().add(relaxRoom);
        getRooms().add(electricityRoom);
        getRooms().add(directorRoom);
        getRooms().add(secretRoom);
        getRooms().add(securityRoom);
        getRooms().add(boilerRoom);
        getRooms().add(caveau);
        getRooms().add(exit);

        //map
        hall.setNorth(corridor);
        hall.setSouth(exit);
        corridor.setWest(relaxRoom);
        corridor.setEast(securityRoom);
        corridor.setSouth(hall);
        relaxRoom.setEast(corridor);
        relaxRoom.setNorth(directorRoom);
        relaxRoom.setSouth(electricityRoom);
        electricityRoom.setNorth(relaxRoom);
        directorRoom.setSouth(relaxRoom);
        directorRoom.setEast(secretRoom);
        secretRoom.setWest(directorRoom);
        securityRoom.setNorth(boilerRoom);
        securityRoom.setSouth(caveau);
        securityRoom.setWest(corridor);
        boilerRoom.setSouth(securityRoom);
        caveau.setNorth(securityRoom);

        //starting room
        setCurrentRoom(hall);

        //carica i dati delle stanze su db
        DBManager db = new DBManager();
        db.connect();
        db.resetTable(RoomDescription.RESET_ROOM_TABLE);

        db.insert(RoomDescription.INSERT_ROOM, hall.getId(),
                RoomDescription.HALL_NAME, RoomDescription.HALL_DESCRIPTION, RoomDescription.HALL_LOOK, null);
        db.insert(RoomDescription.INSERT_ROOM, corridor.getId(),
                RoomDescription.CORRIDOR_NAME, RoomDescription.CORRIDOR_DESCRIPTION, RoomDescription.CORRIDOR_LOOK, null);
        db.insert(RoomDescription.INSERT_ROOM, relaxRoom.getId(),
                RoomDescription.RELAX_ROOM_NAME, RoomDescription.RELAX_ROOM_DESCRIPTION, RoomDescription.RELAX_ROOM_LOOK, null);
        db.insert(RoomDescription.INSERT_ROOM, electricityRoom.getId(),
                RoomDescription.ELECTRICITY_ROOM_NAME, RoomDescription.ELECTRICITY_ROOM_DESCRIPTION,
                RoomDescription.ELECTRICITY_ROOM_LOOK, null);
        db.insert(RoomDescription.INSERT_ROOM, directorRoom.getId(),
                RoomDescription.DIRECTOR_ROOM_NAME, RoomDescription.DIRECTOR_ROOM_DESCRIPTION, RoomDescription.DIRECTOR_ROOM_LOOK,
                RoomDescription.DIRECTOR_ROOM_LOCK);
        db.insert(RoomDescription.INSERT_ROOM, secretRoom.getId(),
                RoomDescription.SECRET_ROOM_NAME, RoomDescription.SECRET_ROOM_DESCRIPTION,
                RoomDescription.SECRET_ROOM_LOOK, RoomDescription.SECRET_ROOM_LOCK);
        db.insert(RoomDescription.INSERT_ROOM, securityRoom.getId(),
                RoomDescription.SECURITY_ROOM_NAME, RoomDescription.SECURITY_ROOM_DESCRIPTION,
                RoomDescription.SECURITY_ROOM_LOOK,
                RoomDescription.SECURITY_ROOM_LOCK);
        db.insert(RoomDescription.INSERT_ROOM, boilerRoom.getId(),
                RoomDescription.BOILER_ROOM_NAME, RoomDescription.BOILER_ROOM_DESCRIPTION,
                RoomDescription.BOILER_ROOM_LOOK, null);
        db.insert(RoomDescription.INSERT_ROOM, caveau.getId(),
                RoomDescription.CAVEAU_NAME, RoomDescription.CAVEAU_DESCRIPTION,
                RoomDescription.CAVEAU_LOOK, RoomDescription.CAVEAU_LOCK);
        db.insert(RoomDescription.INSERT_ROOM, exit.getId(),
                RoomDescription.EXIT_ROOM_NAME, RoomDescription.EXIT_ROOM_DESCRIPTION,
                RoomDescription.EXIT_ROOM_LOOK, RoomDescription.EXIT_ROOM_LOCK);

        db.disconnect();

    }

    /**
     * Metodo per iniziallizare gli oggetti
     */
    private void initObject() {

        AdvObject knife = new AdvObject(0, ObjectDescription.KNIFE_NAME);
        knife.setAlias(new String[]{"lama", "coltello", "pugnale", "coltellaccio"});
        knife.setPickupable(true);
        knife.setToKill(true);
        getRoom(RoomDescription.RELAX_ROOM_NAME).getObjects().add(knife);

        AdvObject buttonElecricMeter = new AdvObject(1, ObjectDescription.BUTTON_ELECTRIC_METER_NAME);
        buttonElecricMeter.setAlias(new String[]{"interruttore", "pulsante", "bottone", "pulsante", "tasto"});
        buttonElecricMeter.setPushable(true);
        buttonElecricMeter.setSpecialObj(AdvObject.specialObject.ELECTRICITY);
        getRoom(RoomDescription.ELECTRICITY_ROOM_NAME).getObjects().add(buttonElecricMeter);

        //oggetti della guardia della stanza della corrente
        AdvObject silencer = new AdvObject(2, ObjectDescription.SILENCER_NAME);
        silencer.setAlias(new String[]{"silencer"});
        silencer.setPickupable(true);
        getNpc(NpcDescription.GUARD1_NAME).getObjects().add(silencer);

        AdvObject camouflage = new AdvObject(3, ObjectDescription.CAMOUFLAGE_NAME);
        camouflage.setAlias(new String[]{"travestimenti", "camuffamento", "camuffamenti",
            "abito", "abiti", "vestito", "vestiti"});
        camouflage.setPickupable(true);
        camouflage.setUsable(true);
        camouflage.setSpecialObj(AdvObject.specialObject.CAMOUFLAGE);
        getNpc(NpcDescription.GUARD1_NAME).getObjects().add(camouflage);

        AdvObject computer = new AdvObject(4, ObjectDescription.COMPUTER_NAME);
        computer.setAlias(new String[]{"pc", "telecamere"});
        computer.setUsable(true);
        computer.setSpecialObj(AdvObject.specialObject.COMPUTER);
        getRoom(RoomDescription.SECURITY_ROOM_NAME).getObjects().add(computer);

        AdvObject phone = new AdvObject(5, ObjectDescription.PHONE_NAME);
        phone.setAlias(new String[]{"cellulare", "telefonino", "cornetta", "polizia", "guardie", "phone"});
        phone.setUsable(true);
        phone.setSpecialObj(AdvObject.specialObject.PHONE);
        getRoom(RoomDescription.SECURITY_ROOM_NAME).getObjects().add(phone);

        AdvObject boilerButton = new AdvObject(6, ObjectDescription.BOILER_BUTTON_NAME);
        boilerButton.setAlias(new String[]{"pulsante", "bottone", "interruttore", "tasto", "button"});
        boilerButton.setPushable(true);
        boilerButton.setSpecialObj(AdvObject.specialObject.BOILER);
        getRoom(RoomDescription.BOILER_ROOM_NAME).getObjects().add(boilerButton);

        AdvObject lightButton = new AdvObject(7, ObjectDescription.LIGHT_BUTTON_NAME);
        lightButton.setAlias(new String[]{"luce", "corrente"});
        lightButton.setPushable(true);
        lightButton.setSpecialObj(AdvObject.specialObject.LIGHT);
        getRoom(RoomDescription.BOILER_ROOM_NAME).getObjects().add(lightButton);

        AdvObject pass = new AdvObject(8, ObjectDescription.PASS_NAME);
        pass.setAlias(new String[]{"tessera", "tesserino", "carta", "card", "pass"});
        pass.setPickupable(true);
        pass.setSpecialObj(AdvObject.specialObject.PASS);

        AdvObjectContainer desckDrawer = new AdvObjectContainer(9, ObjectDescription.DRAWER_NAME);
        desckDrawer.setAlias(new String[]{"cassetto", "tiretto", "scrivania", "drawer"});
        desckDrawer.setOpenable(true);
        desckDrawer.add(pass);
        pass.setContained(desckDrawer.getId());
        getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getObjects().add(desckDrawer);

        AdvObject carpet = new AdvObject(10, ObjectDescription.CARPET_NAME);
        carpet.setAlias(new String[]{"arazzo", "carpet"});
        carpet.setUsable(true);
        carpet.setSpecialObj(AdvObject.specialObject.CARPET);
        getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getObjects().add(carpet);

        AdvObject secretButton = new AdvObject(11, ObjectDescription.SECRET_BUTTON_NAME);
        secretButton.setAlias(new String[]{"leva", "tasto", "pulsante", "interruttore"});
        secretButton.setPushable(true);
        secretButton.setSpecialObj(AdvObject.specialObject.SECRET_BUTTON);

        AdvObjectContainer trapDoor = new AdvObjectContainer(12, ObjectDescription.TRAP_DOOR_NAME);
        trapDoor.setAlias(new String[]{"botola", "nascondioglio", "trapdoor"});
        trapDoor.setOpenable(true);
        trapDoor.add(secretButton);
        secretButton.setContained(trapDoor.getId());
        getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getObjects().add(trapDoor);

        AdvObject pistol = new AdvObject(13, ObjectDescription.PISTOL_NAME);
        pistol.setAlias(new String[]{"ferro", "arma", "beretta", "pistol"});
        pistol.setPickupable(true);
        pistol.setToKill(true);

        AdvObject necklace = new AdvObject(14, ObjectDescription.NECKLACE_NAME);
        necklace.setAlias(new String[]{"collana", "catena", "catenina", "chain"});
        necklace.setPickupable(true);
        necklace.setSpecialObj(AdvObject.specialObject.JEWERLY);

        AdvObject watch = new AdvObject(15, ObjectDescription.WATCH_NAME);
        watch.setAlias(new String[]{"orologio", "watch"});
        watch.setPickupable(true);
        watch.setSpecialObj(AdvObject.specialObject.JEWERLY);

        AdvObject password = new AdvObject(16, ObjectDescription.PASSWORD_NAME);
        password.setAlias(new String[]{"codice", "combinazione", "pin", "foglio", "password"});
        password.setPickupable(true);
        password.setSpecialObj(AdvObject.specialObject.PASSWORD);
        getRoom(RoomDescription.SECRET_ROOM_NAME).getObjects().add(password);

        AdvObjectContainer casket = new AdvObjectContainer(17, ObjectDescription.CASKET_NAME);
        casket.setAlias(new String[]{"cofanetto", "portagioie", "portagioielli", "scrigno", "casket"});
        casket.setOpenable(true);
        casket.setPickupable(true);
        casket.add(necklace);
        casket.add(watch);
        watch.setContained(casket.getId());
        necklace.setContained(casket.getId());

        AdvObjectContainer cabinet = new AdvObjectContainer(18, ObjectDescription.CABINET_NAME);
        cabinet.setAlias(new String[]{"vetrina", "mobile", "cabinet"});
        cabinet.setOpenable(true);
        cabinet.add(pistol);
        cabinet.add(casket);
        pistol.setContained(cabinet.getId());
        casket.setContained(cabinet.getId());
        getRoom(RoomDescription.SECRET_ROOM_NAME).getObjects().add(cabinet);

        AdvObject drill = new AdvObject(19, ObjectDescription.DRILL_NAME);
        drill.setAlias(new String[]{"trivella", "trivellatore", "drill"});
        drill.setPickupable(true);
        drill.setUsable(true);
        drill.setSpecialObj(AdvObject.specialObject.DRILL);

        AdvObject diamonds = new AdvObject(20, ObjectDescription.DIAMONDS_NAME);
        diamonds.setAlias(new String[]{"diamanti", "tesoro", "bottino", "diamond"});
        diamonds.setPickupable(true);
        diamonds.setSpecialObj(AdvObject.specialObject.DIAMONDS);

        AdvObjectContainer safe = new AdvObjectContainer(21, ObjectDescription.SAFE_NAME);
        safe.setAlias(new String[]{"baule", "forziere", "scrigno", "caveau"});
        safe.add(diamonds);
        getRoom(RoomDescription.CAVEAU_NAME).getObjects().add(safe);

        AdvObjectContainer box = new AdvObjectContainer(22, ObjectDescription.BOX_NAME);
        box.setAlias(new String[]{"cassetta", "cassa", "scatola", "contenitore", "box"});
        box.setOpenable(true);
        box.add(drill);
        drill.setContained(box.getId());
        getRoom(RoomDescription.BOILER_ROOM_NAME).getObjects().add(box);

        //caricameno dati oggetti su db
        DBManager db = new DBManager();
        db.connect();
        //db.addConstraint();
        db.resetTable(ObjectDescription.RESET_OBJ_TABLE);

        db.insert(ObjectDescription.INSERT_OBJ, knife.getId(), ObjectDescription.KNIFE_NAME, ObjectDescription.KNIFE_DESCRIPTION,
                ObjectDescription.KNIFE_LOOK, ObjectDescription.KNIFE_USED, getRoom(RoomDescription.RELAX_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, buttonElecricMeter.getId(), ObjectDescription.BUTTON_ELECTRIC_METER_NAME,
                ObjectDescription.BUTTON_ELECTRIC_METER_DESC, ObjectDescription.BUTTON_ELECTRIC_METER_LOOK,
                ObjectDescription.BUTTON_ELECTRIC_METER_USED, getRoom(RoomDescription.ELECTRICITY_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, silencer.getId(), ObjectDescription.SILENCER_NAME, ObjectDescription.SILENCER_DESCRIPTION,
                ObjectDescription.SILENCER_LOOK, null, getRoom(RoomDescription.ELECTRICITY_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, camouflage.getId(), ObjectDescription.CAMOUFLAGE_NAME, ObjectDescription.CAMOUFLAGE_DESCRIPTION,
                ObjectDescription.CAMOUFLAGE_LOOK, ObjectDescription.CAMOUFLAGE_USED,
                getRoom(RoomDescription.ELECTRICITY_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, phone.getId(), ObjectDescription.PHONE_NAME, ObjectDescription.PHONE_DESCRIPTION,
                ObjectDescription.PHONE_LOOK, ObjectDescription.PHONE_USED,
                getRoom(RoomDescription.SECURITY_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, computer.getId(), ObjectDescription.COMPUTER_NAME, ObjectDescription.COMPUTER_DESCRIPTION,
                ObjectDescription.COMPUTER_LOOK, ObjectDescription.COMPUTER_USED,
                getRoom(RoomDescription.SECURITY_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, boilerButton.getId(), ObjectDescription.BOILER_BUTTON_NAME,
                ObjectDescription.BOILER_BUTTON_DESCRIPTION, ObjectDescription.BOILER_BUTTON_LOOK,
                ObjectDescription.BOILER_BUTTON_USED, getRoom(RoomDescription.BOILER_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, lightButton.getId(), ObjectDescription.LIGHT_BUTTON_NAME,
                ObjectDescription.LIGHT_BUTTON_DESCRIPTION, ObjectDescription.LIGHT_BUTTON_LOOK,
                ObjectDescription.LIGHT_BUTTON_USED, getRoom(RoomDescription.BOILER_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, pass.getId(), ObjectDescription.PASS_NAME, ObjectDescription.PASS_DESCRIPTION,
                ObjectDescription.PASS_LOOK, null, getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, desckDrawer.getId(), ObjectDescription.DRAWER_NAME,
                ObjectDescription.DRAWER_DESCRIPTION, ObjectDescription.DRAWER_LOOK, null,
                getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, carpet.getId(), ObjectDescription.CARPET_NAME, ObjectDescription.CARPET_DESCRIPTION,
                ObjectDescription.CARPET_LOOK, ObjectDescription.CARPET_USED,
                getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, secretButton.getId(), ObjectDescription.SECRET_BUTTON_NAME,
                ObjectDescription.SECRET_BUTTON_DESCRIPTION, ObjectDescription.SECRET_BUTTON_LOOK,
                ObjectDescription.SECRET_BUTTON_USED, getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, trapDoor.getId(), ObjectDescription.TRAP_DOOR_NAME,
                ObjectDescription.TRAP_DOOR_DESCRIPTION, ObjectDescription.TRAP_DOOR_LOOK,
                null, getRoom(RoomDescription.DIRECTOR_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, password.getId(), ObjectDescription.PASSWORD_NAME, ObjectDescription.PASSWORD_DESCRIPTION,
                ObjectDescription.PASSWORD_LOOK, null, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, drill.getId(), ObjectDescription.DRILL_NAME, ObjectDescription.DRILL_DESCRIPTION,
                ObjectDescription.DRILL_LOOK, ObjectDescription.DRILL_USED, getRoom(RoomDescription.BOILER_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, safe.getId(), ObjectDescription.SAFE_NAME, ObjectDescription.SAFE_DESCRIPTION,
                ObjectDescription.SAFE_LOOK, null, getRoom(RoomDescription.CAVEAU_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, pistol.getId(), ObjectDescription.PISTOL_NAME, ObjectDescription.PISTOL_DESCRIPTION,
                ObjectDescription.PISTOL_LOOK, ObjectDescription.PISTOL_USED, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, casket.getId(), ObjectDescription.CASKET_NAME, ObjectDescription.CASKET_DESCRIPTION,
                ObjectDescription.CASKET_LOOK, null, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, necklace.getId(), ObjectDescription.NECKLACE_NAME, ObjectDescription.NECKLACE_DESCRIPTION,
                ObjectDescription.NECKLACE_LOOK, null, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, watch.getId(), ObjectDescription.WATCH_NAME, ObjectDescription.WATCH_DESCRIPTION,
                ObjectDescription.WATCH_LOOK, null, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, cabinet.getId(), ObjectDescription.CABINET_NAME, ObjectDescription.CABINET_DESCRIPTION,
                ObjectDescription.CABINET_LOOK, null, getRoom(RoomDescription.SECRET_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, box.getId(), ObjectDescription.BOX_NAME, ObjectDescription.BOX_DESCRIPTION,
                ObjectDescription.BOX_LOOK, null, getRoom(RoomDescription.BOILER_ROOM_NAME).getId());

        db.insert(ObjectDescription.INSERT_OBJ, diamonds.getId(), ObjectDescription.DIAMONDS_NAME, ObjectDescription.DIAMONDS_DESCRIPTION,
                ObjectDescription.DIAMONDS_LOOK, null, getRoom(RoomDescription.CAVEAU_NAME).getId());

        db.disconnect();

    }

    /**
     * Metodo per inizializzare gli npc
     */
    private void initNpc() {
        Npc guardia = new Npc(0, NpcDescription.GUARD1_NAME);
        guardia.setAlias(new String[]{"gendarme", "guardia"});

        Npc guardia2 = new Npc(1, NpcDescription.GUARD1_NAME);
        guardia2.setAlias(new String[]{"gendarme", "guardia"});

        getNpcs().add(guardia);
        getNpcs().add(guardia2);

        DBManager db = new DBManager();
        db.connect();
        db.resetTable(NpcDescription.RESET_NPC_TABLE);
        db.insert(NpcDescription.INSERT_NPC, guardia.getId(), NpcDescription.GUARD1_NAME, NpcDescription.GUARD1_DESCRIPTION,
                NpcDescription.GUARD1_LOOK, NpcDescription.GUARD1_DEATH, 0);
        db.insert(NpcDescription.INSERT_NPC, guardia2.getId(), NpcDescription.GUARD1_NAME, NpcDescription.GUARD1_DESCRIPTION,
                NpcDescription.GUARD2_LOOK, NpcDescription.GUARD2_DEATH, 0);
        db.disconnect();
    }

    /**
     * Metodo per iniziallizare i comandi
     */
    private void initCommand() {

        Command nord = new Command(CommandType.NORD, "nord");
        nord.setAlias(new String[]{"n", "north"});
        getCommands().add(nord);

        Command sud = new Command(CommandType.SOUTH, "sud");
        sud.setAlias(new String[]{"s", "south"});
        getCommands().add(sud);

        Command est = new Command(CommandType.EAST, "est");
        est.setAlias(new String[]{"e", "east"});
        getCommands().add(est);

        Command ovest = new Command(CommandType.WEST, "ovest");
        ovest.setAlias(new String[]{"o", "west"});
        getCommands().add(ovest);

        Command end = new Command(CommandType.END, "end");
        end.setAlias(new String[]{"fine", "esci", "muori", "ammazzati", "ucciditi", "suicidati", "exit"});
        getCommands().add(end);

        Command look = new Command(CommandType.LOOK_AT, "osserva");
        look.setAlias(new String[]{"guarda", "vedi", "trova", "cerca", "descrivi", "look at"});
        getCommands().add(look);

        Command inventory = new Command(CommandType.INVENTORY, "inventario");
        inventory.setAlias(new String[]{"inv", "i", "zaino", "inventory"});
        getCommands().add(inventory);

        Command examine = new Command(CommandType.EXAMINE, "esamina");
        examine.setAlias(new String[]{"examine"});
        getCommands().add(examine);

        Command pickup = new Command(CommandType.PICK_UP, "raccogli");
        pickup.setAlias(new String[]{"prendi", "piglia", "osserva", "ruba", "pick up"});
        getCommands().add(pickup);

        Command drop = new Command(CommandType.DROP, "lascia");
        drop.setAlias(new String[]{"rimani", "butta"});
        getCommands().add(drop);

        Command open = new Command(CommandType.OPEN, "apri");
        open.setAlias(new String[]{"spalanca", "scassina", "open"});
        getCommands().add(open);

        Command close = new Command(CommandType.CLOSE, "chiudi");
        close.setAlias(new String[]{"serra", "close"});
        getCommands().add(close);

        Command push = new Command(CommandType.PUSH, "premi");
        push.setAlias(new String[]{"spingi", "attiva", "schiaccia", "aziona", "pigia", "accendi", "tira", "push"});
        getCommands().add(push);

        Command kill = new Command(CommandType.KILL, "uccidi");
        kill.setAlias(new String[]{"ammazza", "abbatti", "stordisci", "spara", "accoltella", "assassina",
            "sopprimi", "massacra", "annienta", "elimina", "ammazza", "fredda", "kill"});
        getCommands().add(kill);

        Command save = new Command(CommandType.SAVE, "salva");
        save.setAlias(new String[]{"save", "salvataggio"});
        getCommands().add(save);

        Command help = new Command(CommandType.HELP, "aiuto");
        help.setAlias(new String[]{"help"});
        getCommands().add(help);

        Command time = new Command(CommandType.TIME, "tempo");
        time.setAlias(new String[]{"time"});
        getCommands().add(time);

        Command use = new Command(CommandType.USE, "usa");
        use.setAlias(new String[]{"usa", "utilizza", "indossa", "mangia", "metti",
            "hackera", "spegni", "disattiva", "chiama", "avvisa", "sposta", "use"});
        getCommands().add(use);

        Command load = new Command(CommandType.LOAD, "carica");
        load.setAlias(new String[]{"recupera", "ricarica", "load"});
        getCommands().add(load);

    }

    /**
     * Funzione che prende in input il comando e passa la gestione al
     * CommandManager che esegue la mossa e restituisce una stringa di risposta
     *
     * @param p istanza di ParserOutput che contiene il comando e le 'entita' se sono
     * presenti (oggetti, oggetti dell'inventario o npc)
     * @param engine
     * @return risposta all'utente
     */
    @Override
    public String nextMove(ParserOutput p, Engine engine) {

        CommandManager execute = new CommandManager();
        getScore().setMoves(getScore().getMoves() + 1);

        //controlla se ci sono npc nella stanza
        if (NpcsAreLive()) {
            return killGuard(p);
        } else {
            switch (p.getCommand().getType()) {

                case NORD:
                    return move(execute.checkMovement(
                            getCurrentRoom(),
                            (r) -> {
                                return r.getNorth() != null;
                            },
                            (r) -> {
                                return r.getNorth().isAccessible();
                            }),
                            getCurrentRoom().getNorth());
                case SOUTH:
                    return move(execute.checkMovement(
                            getCurrentRoom(),
                            (r) -> {
                                return r.getSouth() != null;
                            },
                            (r) -> {
                                return r.getSouth().isAccessible();
                            }),
                            getCurrentRoom().getSouth());
                case EAST:
                    return move(execute.checkMovement(
                            getCurrentRoom(),
                            (r) -> {
                                return r.getEast() != null;
                            },
                            (r) -> {
                                return r.getEast().isAccessible();
                            }),
                            getCurrentRoom().getEast());
                case WEST:
                    return move(execute.checkMovement(
                            getCurrentRoom(),
                            (r) -> {
                                return r.getWest() != null;
                            },
                            (r) -> {
                                return r.getWest().isAccessible();
                            }),
                            getCurrentRoom().getWest());
                case HELP:
                    return execute.HELP;
                case EXAMINE:
                    return execute.examine(p, o -> {
                        return o.getObject1() != null
                                && o.getObject2() == null
                                && o.getInvObject1() == null && o.getInvObject2() == null
                                && o.getNpc1() == null && o.getNpc2() == null;
                    },
                            i -> {
                                return i.getObject1() == null
                                && i.getObject2() == null
                                && i.getInvObject1() != null && i.getInvObject2() == null
                                && i.getNpc1() == null && i.getNpc2() == null;
                            });
                case INVENTORY:
                    return execute.inventory(getInventory());
                case LOOK_AT:
                    return execute.look(getCurrentRoom());
                case PICK_UP:
                    return execute.pickUp(p, getInventory(), getRooms(), getCurrentRoom(), o -> {
                        return o.getObject1() != null
                                && o.getObject2() == null
                                && o.getInvObject1() == null && o.getInvObject2() == null
                                && o.getNpc1() == null && o.getNpc2() == null;
                    });
                case USE:
                    return execute.use(p, getInventory(), getCurrentRoom(), getRooms(), getNpcs(), getScore(),
                            o -> {
                                return o.getObject1() != null
                                && o.getObject2() == null
                                && o.getInvObject1() == null && o.getInvObject2() == null
                                && o.getNpc1() == null && o.getNpc2() == null;
                            },
                            i -> {
                                return i.getObject1() == null
                                && i.getObject2() == null
                                && i.getInvObject1() != null && i.getInvObject2() == null
                                && i.getNpc1() == null && i.getNpc2() == null;
                            });
                case KILL:
                    return "Non c'e' nessuno che puoi uccidere.";
                case OPEN:
                    return execute.open(p, getCurrentRoom(), getInventory());
                case CLOSE:
                    return execute.close(p, getCurrentRoom(), getInventory());
                case PUSH:
                    return execute.push(p, getRooms(), getNpcs(), getCommands(), getCurrentRoom(), getInventory());
                case DROP:
                    return execute.drop(p, getCurrentRoom(), getInventory());
                case SAVE:
                try {
                    return save();
                } catch (IOException e) {
                    return "Salvataggio fallito! Si e' verificata un'errore." + e.getMessage();
                }
                case LOAD:
                    try {
                    return load(engine);
                } catch (IOException | ClassNotFoundException ex) {
                    return "Caricamento fallito! Si e' verificata un'errore." + ex.getMessage();
                }

            }
        }
        return "Non ho capito";
    }

    /**
     * Funzione richiamata durante un comando di tipo 'spostamento'
     *
     * @param move
     * @param nextRoom
     * @return
     */
    private String move(byte move, Room nextRoom) {

        DBManager db = new DBManager();

        switch (move) {
            //se lo spostamento e' possibile cambia la stanza corrente e stampa la descrizione
            //della nuova stanza
            case 1:
                setCurrentRoom(nextRoom);

                //quando si esce dalla banca termina il gioco
                if (getCurrentRoom().getName().equals(RoomDescription.EXIT_ROOM_NAME)) {
                    getScore().getObjects().addAll(getInventory());
                    CommandManager c = new CommandManager();
                    return c.gameOver(getScore());
                }
                db.connect();
                String answer = db.getNameDesc(getCurrentRoom().getId(), RoomDescription.SELECT_NAME_DESCRIPTION_ROOM);
                db.disconnect();
                return answer;
            case 2:
                // Se la stanza e' bloccata ne stampa il motivo 
                db.connect();
                answer = RoomDescription.SEPARATOR 
                        + db.getLook(nextRoom.getId(), RoomDescription.SELECT_LOCK_ROOM)
                        + "\n" + RoomDescription.SEPARATOR;
                db.disconnect();
                return answer;
            case 3:
                // Se c'è un muro
                answer = "Da questa parte non si puo' andare, c'è un muro!";
                return answer;
        }
        return null;
    }

    /**
     * Funzione richiamata quando e' presente un npc 'in vita' nella stanza
     * corrente, esce da questo stato solo nel momento in cui il nemico viene
     * 'ucciso'
     *
     * @param p
     * @return
     */
    private String killGuard(ParserOutput p) {

        CommandManager command = new CommandManager();
        String wrongCommand = "La guardia ti colpisce!";
        String answer = new String();

        switch (p.getCommand().getType()) {
            case INVENTORY:
                answer = command.inventory(getInventory());
                answer += "\n" + wrongCommand;
                return answer;
            case LOOK_AT:
                answer = command.look(getCurrentRoom());
                answer += "\n" + wrongCommand;
                return answer;
            case HELP:
                answer = command.HELP;
                return answer;
            case END:
                command.end(System.out);
                break;
            case KILL:
                answer = command.kill(p, getCurrentRoom(), getNpcs());
                return answer;
            default:
                answer = wrongCommand;
                return answer;
        }
        return answer;
    }

    /**
     * Funzione per caricare la partita se e' stata salvata in precedenza
     *
     * @param engine
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public String load(Engine engine) throws FileNotFoundException, IOException, ClassNotFoundException {
        TheItalianJob game;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SALVATAGGIO_FILE_PATH))) {
            game = (TheItalianJob) in.readObject();
            engine.setGame(game);
            return "Caricamento partita completato.";
        }
    }

    /**
     * Metodo per il salvataggio della partita
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Override
    public String save() throws FileNotFoundException, IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SALVATAGGIO_FILE_PATH))) {
            out.writeObject(this);
            return "Salvataggio partita completato.";
        }
    }

}
