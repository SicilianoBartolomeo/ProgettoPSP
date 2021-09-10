/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameEntity;

/**
 * Interfaccia contenete costanti per il salvateggio e recupero da database delle
 * informazioni delle stanze del gioco.
 *
 * @author Barty
 */
public interface RoomDescription {

    //query per l'aggiornamento e l'interrogazione della tabella delle stanze
    public static final String CREATE_ROOM_TABLE = "CREATE TABLE IF NOT EXISTS room (id INT PRIMARY KEY, name VARCHAR(64), desc VARCHAR(1024),"
            + " look VARCHAR (1024), lock VARCHAR (1024))";

    public static final String INSERT_ROOM = "INSERT INTO room VALUES(?,?,?,?,?)";

    public static final String RESET_ROOM_TABLE = "TRUNCATE TABLE room";

    public static final String SELECT_NAME_DESCRIPTION_ROOM = "SELECT name, desc FROM room WHERE id = ? ";

    public static final String SELECT_LOOK_ROOM = "SELECT look FROM room WHERE id = ? ";

    public static final String SELECT_LOCK_ROOM = "SELECT lock FROM room WHERE id = ? ";

    public static final String SELECT_LOOK_ROOM_OBJ = "SELECT r.look, o.look FROM room r JOIN object o ON o.roomId = r.id WHERE "
            + "r.id = ? AND o.id = ?";

    public static final String SELECT_LOOK_ROOM_OBJ_NPC = "SELECT r.look, o.look, n.look FROM room r JOIN object o ON o.roomId = r.id"
            + " JOIN npc n ON n.roomId = r.id WHERE r.id = ? AND o.id = ? AND n.id = ?";

    public static final String INTRO =
            "===========================================================\n"
            + "                   THE ITALIAN JOB                       \n"
            + "Finalmente è arrivato il giorno 0… il giorno tanto atteso\n"
            + "per dare una svolta alla tua vita. La banca ti ha chiamato\n"
            + "qualche giorno fa in qualità di tecnico per risolvere un\n"
            + "guasto all’impianto elettrico; la giusta occasione per\n"
            + "compiere il colpo della vita, abbandonare la tua noiosa\n"
            + "routine da elettricista e fuggire alle Hawaii con il bottino…\n"
            + "Ti trovi davanti alla banca, entri, superi i controlli della\n"
            + "sicurezza e finalmente ti trovi nel salone principale pronto\n"
            + "per andare nella sala di sicurezza per risolvere il guasto,\n"
            + "ma per una volta non farai il tuo dovere…"
            + "\n===========================================================";

    //nomi e descrizioni delle stanze
    public static final String HALL_NAME = "Salone Principale";

    public static final String HALL_DESCRIPTION = "Sei nell'atrio della banca";

    public static final String HALL_LOOK = "Sei nel Salone principale, l'ingresso della banca dove devi fare il colpo \n"
            + "a nord vedi il Corridoio principale";

    public static final String SECURITY_ROOM_NAME = "Sala della sicurezza";

    public static final String SECURITY_ROOM_DESCRIPTION = "Sei nella Sala della sicurezza, da tutti questi schermi le guardie di solito controllano\n"
            + "ogni singolo angolo della banca. Non c'e' niente che puo' sfuggirli... o quasi.";

    public static final String SECURITY_ROOM_LOOK = "Sei nella Sala della sicurezza, sono presenti le telecamere"
            + " di videosorveglianza\nA nord, attraverso le scale puoi scendere nella sala caldaie, a sud, passando per la porta di sicurezza\n"
            + "puoi accedere al tanto desiderato caveau mentre a ovest torni nel corridoio.";

    public static final String SECURITY_ROOM_LOCK = "Non ti conviene entrare senza un valido travestimento da guardia della sicurezza\n"
            + "Ti beccherebbero subito non credi?";

    public static final String CORRIDOR_NAME = "Corridoio principale";

    public static final String CORRIDOR_DESCRIPTION = "Sei nel lungo corridoio della banca";

    public static final String CORRIDOR_LOOK = "Sei nel Corridoio principale della banca che collega le stanze tra loro:\n"
            + "a ovest puoi andare nella sala relax, a est si trova la sala della sicurezza\nmentre a sud torni nel salone principale.";

    public static final String RELAX_ROOM_NAME = "Sala relax";

    public static final String RELAX_ROOM_DESCRIPTION = "Sei nella sala relax, qui di solito i dipendenti della banca\n"
            + "passano la loro triste e breve pausa pranzo.";

    public static final String RELAX_ROOM_LOOK = "Sei nella Sala relax, a nord puoi trovare la sala del direttore,\n"
            + "a sud la sala della corrente mentre e est torni nel corridoio.";

    public static final String ELECTRICITY_ROOM_NAME = "Sala della corrente";

    public static final String ELECTRICITY_ROOM_DESCRIPTION = "Sei nella sala della manutenzione della corrente";

    public static final String ELECTRICITY_ROOM_LOOK = "Qui di solito i tecnici fanno manutenzione all'impianto elettrico della banca,\n"
            + "se vai verso nord torni nella sala relax.\n"
            + "Sarebbe un peccato se premessi l'interrutore sbagliato sul contatore...";

    public static final String DIRECTOR_ROOM_NAME = "Ufficio del direttore";

    public static final String DIRECTOR_ROOM_DESCRIPTION = "Sei nell'Ufficio del direttore, dall'arredamento direi che se la passa bene qui dentro.";

    public static final String DIRECTOR_ROOM_LOOK = "Sei nell'ufficio del direttore, c'e' una classica scrivania con la poltrona e diverse librerie\n"
            + "Se vai a sud torni nella sala relax.";

    public static final String DIRECTOR_ROOM_LOCK = "Non conviene entrare qui, dentro c'e' il direttore della banca protetto da due\n"
            + "delle sue guardie meglio addestrate, devi trovare il modo di farlo uscire...\nforse con un po' di calore...";

    public static final String BOILER_ROOM_NAME = "Locale caldaie";

    public static final String BOILER_ROOM_DESCRIPTION = "Attraverso una lunga e buia rampa di scale scendi all'umida sala caldaie";

    public static final String BOILER_ROOM_LOOK = "Sei nel locale caldaie, da qui i tecnici regolano tutto cio' che riguarda l'impianto"
            + " termico della banca.\nA nord, se sali per le scale, torni nella sala della sicurezza.";

    public static final String CAVEAU_NAME = "Caveau";

    public static final String CAVEAU_DESCRIPTION = "Dopo aver usato il pass del direttore e inserito la password, nell'apposito tastierino,\n"
            + "l'enorme porta di ferro si apre e dopo aver percorso un corridoio sei nell'enorme Caveau blindato,\n"
            + "se vai nord ritorni nella sala della sicurezza.";

    public static final String CAVEAU_LOOK = "Sei nell'enorme caveau, sei circondato da montagne di soldi,\n"
            + "che pero' occuperebbero troppo spazio per essere portati con te,\n"
            + "cio' che ti interessa veramente si trova nella cassaforte.";

    public static final String CAVEAU_LOCK = "Sarebbe troppo facile entrare cosi' nel caveau. Tra te e le immense ricchezze si trova\n"
            + "una gigantesca porta blindata, l'unico modo per aprirla e' trovare il pass del direttore.";

    public static final String SECRET_ROOM_NAME = "Stanza segreta del direttore";

    public static final String SECRET_ROOM_DESCRIPTION = "Chi avrebbe mai pensato che dietro la libreria si nascondesse\n"
            + "una stanza segreta";

    public static final String SECRET_ROOM_LOOK = "Sembra proprio un bunker segreto del direttore usato in caso di emergenza\n"
            + "A ovest torni nell'ufficio del direttore.";

    public static final String SECRET_ROOM_LOCK = "Da questa parte non si puo' andare, c'è un muro!";

    public static final String EXIT_ROOM_NAME = "uscita";

    public static final String EXIT_ROOM_DESCRIPTION = "";

    public static final String EXIT_ROOM_LOOK = "";

    public static final String EXIT_ROOM_LOCK = "Non puoi scappare senza il bottino!";

    public static final String SEPARATOR = "===========================================================";
    
}
