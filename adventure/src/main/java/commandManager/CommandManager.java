/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandManager;

import database.DBManager;
import gameEntity.NpcDescription;
import gameEntity.ObjectDescription;
import gameEntity.RoomDescription;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import parser.ParserOutput;
import type.AdvObject;
import type.AdvObjectContainer;
import type.Command;
import type.Npc;
import type.Room;
import type.Score;

/**
 * Control Classe che si occupa dell'esecuzione dei comandi, implementa la
 * logica del gico.
 *
 * @author Barty
 */
public class CommandManager {

    private final DBManager db;

    private String answer;

    public CommandManager() {
        this.db = new DBManager();
        answer = new String();
    }

    /**
     * Funzione che esegue i controlli per verificare se lo spostaamento e'
     * legale
     *
     * @param currentRoom
     * @param roomDestination
     * @param isAccessible
     * @return
     */
    public byte checkMovement(Room currentRoom, Predicate<Room> roomDestination, Predicate<Room> isAccessible) {

        if (roomDestination.test(currentRoom)) {
            if (isAccessible.test(currentRoom)) {
                byte move = 1;
                return move; // Hai cambiato stanza
            } else {
                byte move = 2;
                return move; // La stanza è chiusa a chiave
            }
        } else {
            byte move = 3;
            return move; // C'è un muro
        }

    }

    /**
     * Funzione che esegue il comando di tipo 'look at' e ritorna una stringa di
     * risposta per l'utente.
     *
     * @param currentRoom
     * @return
     */
    public String look(Room currentRoom) {

        answer = "";
        if (currentRoom.isVisible()) {
            answer = RoomDescription.SEPARATOR;
            db.connect();
            answer += db.getLook(currentRoom.getId(), RoomDescription.SELECT_LOOK_ROOM);
            //se sono presenti nella stanza concatena la descrizione degli oggetti e degli npc
            answer = currentRoom.getObjects()
                    .stream()
                    .filter(o -> o != null)
                    .map(o -> db.getLook(o.getId(), ObjectDescription.SELECT_LOOK_OBJ))
                    .reduce(answer, String::concat);

            answer = currentRoom.getNpcs()
                    .stream()
                    .filter(n -> n != null)
                    .map(n -> db.getLook(n.getId(), NpcDescription.SELECT_LOOK_NPC))
                    .reduce(answer, String::concat);

            db.disconnect();
            answer += "\n" + RoomDescription.SEPARATOR;
            return answer;

        } else {
            answer = "Non vedo niente, la luce è spenta. Bisogna accendere la luce!";
            return answer;
        }

    }

    /**
     * Funzioe che esegua il comando di tipo 'pick up' e ritorna una stringa di
     * risposta all'utente.
     *
     * @param p
     * @param inventory
     * @param rooms
     * @param currentRoom
     * @param check
     * @return
     */
    public String pickUp(ParserOutput p, List<AdvObject> inventory, List<Room> rooms,
            Room currentRoom, Predicate<ParserOutput> check) {

        answer = "";
        if (check.test(p)) {
            answer = checkPickupable(p.getObject1(), inventory, currentRoom, rooms);
            return answer;
        } else {
            answer = "Devi specificare un oggetto per volta che si puo' prendere ed e' presente nella stanza.";
            return answer;
        }

    }

    /**
     * Funzione che controlla se il comando inserito e' corretto, controlla che
     * il comando e' nella forma 'comando-oggetto'
     *
     * @param object
     * @param inventory
     * @param currentRoom
     * @param rooms
     * @return
     */
    private String checkPickupable(AdvObject object, List<AdvObject> inventory, Room currentRoom, List<Room> rooms) {

        answer = "";
        if (object.isPickupable()) {
            inventory.add(object);
            currentRoom.getObjects().remove(object);
            answer = "Hai raccolto: \n";
            db.connect();
            answer += db.getNameDesc(object.getId(), ObjectDescription.SELECT_NAME_DESCRIPTION_OBJ);
            db.disconnect();

            //quando si raccoglie il pass del direttore e la passwoerd sblocca il caveau
            if (unlockCaveau(inventory) && !rooms.get(8).isAccessible()) {
                answer += "\nFinalmente tutto il necessario per entrare nel caveau e' sato raccolto!";
                rooms.get(8).setAccessible(true);
            }

            //quando si raccoglie il contenuto della cassaforte sblocca l'uscita della banca
            if (object.getSpecialObj() != null) {
                switch (object.getSpecialObj()) {
                    case DIAMONDS:
                        answer += "\nFinalmente il bottino e' tuo! Non ti resta che fuggire!";
                        rooms.get(9).setAccessible(true);
                }
            }
            return answer;
        } else {
            answer = "Non puoi raccogliere questo oggetto.";
            return answer;
        }

    }

    /**
     * Funzione che controlla se e' possibile sbloccare il caveau, controlla se
     * nell'inventario e' pressente il pass e la password.
     *
     * @param inventory
     * @return
     */
    private boolean unlockCaveau(List<AdvObject> inventory) {

        boolean pass = false;
        boolean password = false;

        for (AdvObject o : inventory) {
            if (o.getSpecialObj() != null) {
                switch (o.getSpecialObj()) {
                    case PASS:
                        pass = true;
                        break;
                    case PASSWORD:
                        password = true;
                        break;
                }
                if (pass && password) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Funzione che esegue il comando di tipo 'inventory': ritorna una stringa
     * contenente i nomi e le descrizioni degli oggeti contenuti
     * nell'inventario, se presenti.
     *
     * @param inventory
     * @return
     */
    public String inventory(List<AdvObject> inventory) {

        answer = "";

        if (!inventory.isEmpty()) {
            answer += "Nel tuo inventario c'è: ";
            db.connect();

            answer = inventory
                    .stream()
                    .map(o -> db.getNameDesc(o.getId(), ObjectDescription.SELECT_NAME_DESCRIPTION_OBJ))
                    .reduce(answer, String::concat);

            db.disconnect();
            return answer;
        } else {
            answer = "L'inventario e' vuoto.";
            return answer;
        }

    }

    /**
     * Funzione che implementa il comando di tipo 'examine': controlla se dopo
     * il comando di tipo 'examine' e presente un oggetto della stanza corrente
     * o dell'inventario e in caso affermativo ritorna una stringa contenente la
     * descrizione.
     *
     * @param p
     * @param obj
     * @param invObj
     * @return
     */
    public String examine(ParserOutput p, Predicate<ParserOutput> obj, Predicate<ParserOutput> invObj) {

        answer = "";
        if (obj.test(p)) {
            db.connect();
            answer = db.getNameDesc(p.getObject1().getId(), ObjectDescription.SELECT_NAME_DESCRIPTION_OBJ);
            db.disconnect();
            return answer;
        }
        if (invObj.test(p)) {
            db.connect();
            answer = db.getNameDesc(p.getInvObject1().getId(), ObjectDescription.SELECT_NAME_DESCRIPTION_OBJ);
            db.disconnect();
            return answer;
        }
        answer = "Seleziona un oggetto da esaminare presente nella stanza o nell'inventario.";
        return answer;

    }

    /**
     * Funzione che esegue il comando di tipo 'open': ritorna una stringa di
     * risposta all'utente.
     *
     * @param p
     * @param currentRoom
     * @param inventory
     * @return
     */
    public String open(ParserOutput p, Room currentRoom, List<AdvObject> inventory) {

        if (p.getObject2() != null || p.getInvObject2() != null || p.getNpc2() != null) {
            answer = "Puoi aprire solo un oggetto alla volta!";
            return answer;
        } else {
            if (p.getObject1() != null) {
                answer = checkOpen(p.getObject1(), currentRoom, null);

            } else if (p.getInvObject1() != null) {
                answer = checkOpen(p.getInvObject1(), null, inventory);
                return answer;

            } else if (p.getNpc1() != null) {
                answer = "Non puoi aprire un personaggio...";
                return answer;
            }

        }
        if (p.getObject1() == null && p.getInvObject1() == null) {
            answer = "Devi specificare un oggetto che si puo' aprire\n"
                    + "presente nella stanza o nell'inventario!";
        }
        return answer;

    }

    /**
     * Funzione che esegue il comando: controlla se l'oggetto e apribile e non
     * e' aperto e in caso affermativo lo apre e ritorna una stringa contenente
     * gli oggetti contenuti se presenti
     *
     * @param object
     * @param currentRoom
     * @param inventory
     * @return
     */
    private String checkOpen(AdvObject object, Room currentRoom, List<AdvObject> inventory) {

        if (object.isOpenable()) {
            if (!object.isOpen()) {
                if (object instanceof AdvObjectContainer) {
                    object.setOpen(true);
                    answer = RoomDescription.SEPARATOR + "\nHai aperto: " + object.getName();
                    AdvObjectContainer c = (AdvObjectContainer) object;
                    if (!c.getList().isEmpty()) {
                        answer += "\n" + c.getName() + " contiene: ";
                        Iterator<AdvObject> it = c.getList().iterator();
                        while (it.hasNext()) {
                            AdvObject next = it.next();
                            if (currentRoom != null) {
                                currentRoom.getObjects().add(next);
                            } else if (inventory != null) {
                                inventory.add(next);
                            }
                            answer += "\n- " + next.getName();
                            it.remove();
                        }
                    }
                    answer += "\n" + RoomDescription.SEPARATOR;
                    return answer;
                }
            } else {
                answer = "Questo oggetto e' gia' aperto!";
                return answer;
            }
        } else {
            answer = "Questo oggetto non si puo' aprire!";
            return answer;
        }
        return answer;

    }

    /**
     * Funzione che implementa il comando di tipo 'close': controlla se dopo il
     * comando e' presente un oggetto della stanza o dell'inventario e controlla
     * se e possibile chiuderlo.
     *
     * @param p
     * @param currentRoom
     * @param inventory
     * @return
     */
    public String close(ParserOutput p, Room currentRoom, List<AdvObject> inventory) {

        if (p.getObject2() != null || p.getInvObject2() != null || p.getNpc2() != null) {
            answer = "Puoi chiudere solo un oggetto alla volta!";
            return answer;
        } else {
            if (p.getObject1() != null) {
                answer = checkClose(p.getObject1(), currentRoom.getObjects(), null);
                return answer;
            } else if (p.getInvObject1() != null) {
                answer = checkClose(p.getInvObject1(), null, inventory);
                return answer;
            } else if (p.getNpc1() != null) {
                answer += "Non puoi chiudere un personaggio...";
                return answer;
            }
        }
        if (p.getObject1() == null && p.getInvObject1() == null) {
            answer = "Devi specificare un oggetto che si puo' chiudere\n"
                    + "presente nella stanza o nell'inventario!";
        }
        return answer;

    }

    /**
     * Controlla se l'oggeto da chiudere e' aperto e in caso affermativo se
     * l'oggetto de chiudere e' dell'inventario rimuve gli oggetti da
     * quest'ultimo e li aggiunge all'oggetto contenitore, mentre se l'oggetto
     * contenitore e' della stanza rimuove gli oggetti dalla stanza e li
     * aggiunge al contenitore.
     *
     * @param object
     * @param objRoom
     * @param inventory
     * @return
     */
    private String checkClose(AdvObject object, List<AdvObject> objRoom, List<AdvObject> inventory) {

        if (object.isOpenable()) {
            if (object.isOpen()) {
                if (object instanceof AdvObjectContainer) {
                    List<AdvObject> l = new ArrayList<>();
                    if (objRoom != null) {
                        l = objRoom;
                    }
                    if (inventory != null) {
                        l = inventory;
                    }
                    if (!l.isEmpty()) {

                        Iterator<AdvObject> it = l.iterator();
                        while (it.hasNext()) {
                            AdvObject next = it.next();
                            if (object.getId() != next.getId()) {
                                //gli oggetti contenitore hanno l'id uguale all'attributo 'conteied' degli oggetti che erano contenuti
                                if (object.getId() == next.getContained()) {
                                    ((AdvObjectContainer) object).add(next);
                                    it.remove();
                                }
                            }
                        }
                        object.setOpen(false);
                    }
                } else {
                    object.setOpen(false);
                }
                answer = RoomDescription.SEPARATOR + "\nHai chiuso: " + object.getName()
                        + "\n" + RoomDescription.SEPARATOR;
                return answer;
            } else {
                answer = "Questo oggetto e' gia' chiuso!";
                return answer;
            }
        } else {
            answer = "Questo oggetto non si puo' chiudere!";
            return answer;
        }
    }

    /**
     * Funzione che esegue il comando di tipo 'usa'
     *
     * @param p
     * @param inventory
     * @param currentRoom
     * @param rooms
     * @param npcs
     * @param score
     * @param obj
     * @param invObj
     * @return
     */
    public String use(ParserOutput p, List<AdvObject> inventory, Room currentRoom, List<Room> rooms,
            List<Npc> npcs, Score score, Predicate<ParserOutput> obj, Predicate<ParserOutput> invObj) {

        //controlla se l'oggetto da usare e' un oggetto della stanza
        if (obj.test(p)) {
            answer = RoomDescription.SEPARATOR + "\n";
            answer += useAnswer(p.getObject1(), currentRoom.getObjects(), checkUsable(p.getObject1(), rooms, npcs, score), npcs);
            answer += "\n" + RoomDescription.SEPARATOR;
            return answer;
        } //controlla se l'oggetto da usare e' dell'inventario
        else if (invObj.test(p)) {
            answer = RoomDescription.SEPARATOR + "\n";
            answer = useAnswer(p.getInvObject1(), inventory, checkUsable(p.getInvObject1(), rooms, npcs, score), npcs);
            answer += "\n" + RoomDescription.SEPARATOR;
            return answer;
        }
        // se non viene specificato nessun oggetto da utilizzare
        answer = "Seleziona un oggetto utilizzabile presente nella stanza o nell'inventario.";
        return answer;

    }

    /**
     * restituisce la stringa di risposta all'utente in base all'oggetto
     * utilizzato, se e' possiblie utilizzarlo.
     */
    private String useAnswer(AdvObject obj, List<AdvObject> objects, short flag, List<Npc> npcs) {

        if (flag > 0) {
            answer = "Hai utilizzato: " + obj.getName();

            switch (flag) {
                case 1:      //travestimento
                    objects.remove(obj);
                    db.connect();
                    answer += db.getLook(obj.getId(), ObjectDescription.SELECT_ANSWR_ON_USE);
                    db.disconnect();
                    return answer;
                case 2:     //telefono - computer - tappeto
                    obj.setUsable(false);
                    db.connect();
                    answer += db.getLook(obj.getId(), ObjectDescription.SELECT_ANSWR_ON_USE);
                    db.disconnect();
                    return answer;
                case 3:     //oggetto qualsiasi
                    obj.setUsable(false);
                    return answer;
                case 4:  //trapano
                    obj.setUsable(false);
                    db.connect();
                    answer += db.getLook(obj.getId(), ObjectDescription.SELECT_ANSWR_ON_USE)
                            + db.getLook(npcs.get(0).getId(), NpcDescription.SELECT_LOOK_NPC);
                    db.disconnect();
            }
        } else {     //oggetto non utilizzabile
            answer = "Questo oggetto non e' utilizzabile o e' gia' stato utilizzato."
                    + "\nSeleziona un oggetto utilizzabile presente nella stanza o nell'inventario.";
            return answer;
        }
        return answer;
    }

    /**
     * Controlla se l' oggetto passato si puo' usare e in caso affermativo
     * ritorna un numero in base al tipo di oggetto usato.
     */
    private short checkUsable(AdvObject obj, List<Room> rooms, List<Npc> npcs, Score score) {

        if (obj.isUsable()) {
            if (obj.getSpecialObj() != null) {

                switch (obj.getSpecialObj()) {
                    case CAMOUFLAGE:
                        rooms.get(6).setAccessible(true);
                        return 1;
                    case PHONE:
                        score.getObjects().add(obj);
                        return 2;
                    case COMPUTER:
                        score.getObjects().add(obj);
                        return 2;
                    case CARPET:
                        return 2;
                    //quando si usa il trapano aggiunge un npc nel caveau
                    case DRILL:
                        rooms.get(8).getObjects().get(0).setOpenable(true);
                        npcs.get(0).setLive(true);
                        rooms.get(8).getNpcs().add(npcs.get(0));
                        return 4;
                }

            } else {
                return 3;
            }
        }
        return -1;

    }

    /**
     * Funzione che implementa il comando di tipo 'push' e ritorna una stringa
     * di risposta all'utente.
     *
     * @param p
     * @param rooms
     * @param npcs
     * @param commands
     * @param currentRoom
     * @param inv
     * @return
     */
    public String push(ParserOutput p, List<Room> rooms, List<Npc> npcs, List<Command> commands, Room currentRoom, List<AdvObject> inv) {

        if (p.getObject1() != null && p.getObject2() == null
                && p.getInvObject1() == null && p.getInvObject2() == null
                && p.getNpc1() == null && p.getNpc2() == null) { // Se l'oggetto è nella stanza
            if (p.getObject1().isPushable()) { // Se si può premere
                if (!p.getObject1().isPush()) { //se non e' stato premuto
                    p.getObject1().setPush(true);
                    answer += RoomDescription.SEPARATOR;
                    db.connect();
                    answer += db.getLook(p.getObject1().getId(), ObjectDescription.SELECT_ANSWR_ON_USE);
                    db.disconnect();
                    switch (p.getObject1().getSpecialObj()) {
                        //quando viene premuto il pulsante della stanza della corrente aggiunge un npc nella stanza
                        // e genera il blackout spegnendo la luce che si riaccende quando l'npc viene eliminato
                        case ELECTRICITY:
                            rooms.get(3).getObjects().remove(p.getObject1());
                            rooms.get(3).setVisible(false);
                            npcs.get(0).setLive(true);
                            rooms.get(3).getNpcs().add(npcs.get(0));
                            db.connect();
                            answer += db.getLook(npcs.get(0).getId(), NpcDescription.SELECT_LOOK_NPC);
                            db.disconnect();
                            break;
                        //accende la luce alla sala caldaie
                        case LIGHT:
                            rooms.get(7).setVisible(true);
                            break;
                        //quando viene premuto il pulsante nella sala caldaie sblocca l'ufficio del direttore
                        case BOILER:
                            rooms.get(4).setAccessible(true);
                            break;
                        //quando viene tirata la leva nell'ufficio del direttore sblocca la stanza segreta 
                        case SECRET_BUTTON:
                            rooms.get(5).setAccessible(true);
                            break;
                    }
                    answer += "\n" + RoomDescription.SEPARATOR;
                    return answer;
                } else {
                    answer = "Questo oggetto e' gia' stato premuto.";
                    return answer;
                }

            } else { // Se non si può premere
                answer = "Non puoi premere o attivare questo oggetto.";
                return answer;
            }
        } else {
            answer = "Devi specificare un oggetto premibile presente nella stanza.";
            return answer;
        }
    }

    /**
     * Funzione che esegue il comando di tipo 'kill': controlla se il comando e'
     * stato inserito correttamente e in caso affermativo rimuove l'npc dalla
     * stanza, rilascia gli oggetti che aveva con se e ritorna una stringa di
     * risposta all'utente.
     *
     * @param p
     * @param currentRoom
     * @param npcs
     * @return
     */
    public String kill(ParserOutput p, Room currentRoom, List<Npc> npcs) {

        Npc npc = null;
        //controlla se l'npc da uccidere nel comando e' nella forma <kill> <npc> <obj> o viceversa 
        //e se l'oggetto specificato puo' esserre usato per uccidere
        if ((p.getObject1() != null && p.getObject2() == null //oggetto, npc
                && p.getInvObject1() == null && p.getInvObject2() == null
                && p.getNpc1() == null && p.getNpc2() != null && p.getObject1().isToKill())
                || (p.getObject1() == null && p.getObject2() != null //npc,oggetto
                && p.getInvObject1() == null && p.getInvObject2() == null
                && p.getNpc1() != null && p.getNpc2() == null && p.getObject2().isToKill())
                || (p.getObject1() == null && p.getObject2() == null //oggettoInv, npc
                && p.getInvObject1() != null && p.getInvObject2() == null
                && p.getNpc1() == null && p.getNpc2() != null && p.getInvObject1().isToKill())
                || (p.getObject1() == null && p.getObject2() == null //npc, oggettoInv
                && p.getInvObject1() == null && p.getInvObject2() != null
                && p.getNpc1() != null && p.getNpc2() == null && p.getInvObject2().isToKill())) {
            if (p.getNpc1() != null) {
                npc = p.getNpc1();
            } else if (p.getNpc2() != null) {
                npc = p.getNpc2();
            }
            AdvObject obj = checkObject(p);
            answer = RoomDescription.SEPARATOR + "\n===GUARDIA ELIMINATA===\n";
            db.connect();
            answer += db.getLook(obj.getId(), ObjectDescription.SELECT_ANSWR_ON_USE);
            //npc.getId non sara' nullo poiche' in precedenza viene controllato se e' stato specificato un npc valido
            answer += db.getLook(npc.getId(), NpcDescription.SELECT_DEATH_NPC);
            db.disconnect();
            answer += "\n" + RoomDescription.SEPARATOR;
            npc.dropObj(currentRoom);
            npc.setLive(false);
            currentRoom.getNpcs().remove(npc);
            npcs.remove(npc);
            //accende la luce in caso e' spenta come nel caso della stanza della corrente 
            //a seguito del blackout
            if (!currentRoom.isVisible()) {
                currentRoom.setVisible(true);
            }
            return answer;
        } else {
            answer = "Per uccidere devi specificare chi uccidere e l'oggetto con cui uccidere\n"
                    + "quest'ultimo puo' essere dell'inventario o della stanza, non importa l'ordine\n"
                    + "basta che si possa usare per uccidere.";
            return answer;
        }

    }

    private AdvObject checkObject(ParserOutput p) {
        if (p.getObject1() != null) {
            return p.getObject1();
        }
        if (p.getObject2() != null) {
            return p.getObject2();
        }
        if (p.getInvObject1() != null) {
            return p.getInvObject1();
        }
        if (p.getInvObject2() != null) {
            return p.getInvObject2();
        }
        return null;
    }

    /**
     * Funzione che esegue il comando di tipo 'drop': controlla se l'oggeto
     * inserito dopo il comando si trova nell'invetario e in caso affermativo lo
     * toglie dall'inventario e lo aggiunge alla stanza.
     *
     * @param p
     * @param currentRoom
     * @param inventory
     * @return
     */
    public String drop(ParserOutput p, Room currentRoom, List<AdvObject> inventory) {

        if (p.getObject2() != null || p.getInvObject2() != null || p.getNpc2() != null) {
            answer = "Puoi lasciare un solo oggetto alla volta.";
            return answer;
        } else if (p.getObject1() != null) {
            answer = "Non puoi lasciare un oggetto che non hai nell'inventario.";
            return answer;
        } else if (p.getNpc1() != null) {
            answer = "Non puoi lasciare un personaggio!";
            return answer;
        } else if (p.getInvObject1() != null) {
            currentRoom.getObjects().add(p.getInvObject1());
            inventory.remove(p.getInvObject1());
            answer = RoomDescription.SEPARATOR;
            answer += "\nHai lasciato " + p.getInvObject1().getName() + " in: " + currentRoom.getName();
            answer += "\n" + RoomDescription.SEPARATOR;
            return answer;
        } else {
            answer = "Specifica cosa vuoi lasciare.";
            return answer;
        }

    }

    /**
     * Metodo per terminare l'applicazione quando viene avviato da linea di
     * comando
     *
     * @param out
     */
    public void end(PrintStream out) {

        boolean exit = false;

        do {
            out.println("Sei sicuro di voler uscire dal gioco? Tutti i progressi non salvati andranno perduti.\n");
            out.println("Digita SI o NO:");
            Scanner end = new Scanner(System.in);
            String confirm = end.next().toUpperCase();

            switch (confirm) {
                case "SI":
                    System.out.println("================================================================================================");
                    out.println("\nL'avventura per te... FINISCE QUI!");
                    System.exit(0);

                    break;
                case "NO":
                    exit = true;
                    break;
                default:
                    out.println("\nDigita SI o NO:");
                    exit = false;
            }

        } while (exit == false);
    }

    /**
     * Funzione che ritorna la stringa quando si finisce il gioco
     * @param score
     * @return 
     */
    public String gameOver(Score score) {

        answer = "##################################################################"
                + "\n\t  VITTORIA!!! Chi diceva che il crimine non ripaga?!"
                + "\n##################################################################"
                + "\n\t PUNTEGGIO TOTALE: " + score.getTotalScore()
                + "\n##################################################################";
        return answer;

    }

    public final String HELP = 
              "==============================================================================================\n"
            + "Ricordati che per spostarti e per eseguire altre azioni dovrai eseguire i seguenti comandi:\n"
            + "- NORD, SUD, OVEST, EST per spostarti. O più semplicemente N, S, O, E;\n"
            + "- Per guardarti attorno digita il comando OSSERVA o un suo sinonimo;\n"
            + "- Per esaminare qualcosa, digita il comando ESAMINA, o un suo sinonimo,\n"
            + "  seguito da un oggetto presente nella stanza o nell'inventario.\n"
            + "- Per raccogliere un oggetto usa il comando PRENDI, o un suo sinonimo,\n"
            + " seguito dall'oggetto che vuoi prendere\n"
            + "- Per lasciare un oggetto usa il comando LASCIA, o un suo sinonimo,\n"
            + " seguito dall'oggetto che vuoi lasciare presente nell'inventario\n"
            + "- Per usare un oggetto usa il comando USA, o un suo sinonimo,\n"
            + " seguito dall'oggetto che vuoi usare presente nella stanza o nell'inventario\n"
            + "- Il tuo borsone da lavoro può contenere vari oggetti,\n"
            + "  per vederne il contenuto digita INVENTARIO o un suo sinonimo'.\n"
            + "- Per aprire un oggetto digita il comando APRI o un suo sinonimo seguito\n"
            + " dall'oggetto da aprire presente nella stanza o nell'inventario\n."
            + "- Per uscire dalla partita corrente, digita il comando ESCI\n"
            + "- Ci potrebbero essere pericoli, ricordati di salvare la partita con il comando SALVA.\n"
            + "  Per caricare la partita precedente usa il comando CARICA.\n"
            + "Se dovessi dimenticare tutto digita 'help' per rivedere questa schermata."
            + "\n==============================================================================================";

}
