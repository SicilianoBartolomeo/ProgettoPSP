/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameEntity;

/**
 * Interfaccia contenente le query per le inerrogazione al db
 * e descrizioni degli oggetti da caricare su db
 * @author Barty
 */
public interface ObjectDescription {

    //query per l'aggiornamento e l'interrogazione della tabella delle stanze
    public static final String CREATE_OBJ_TABLE = "CREATE TABLE IF NOT EXISTS object (id INT PRIMARY KEY, name VARCHAR(64), desc VARCHAR(1024),"
            + " look VARCHAR (1024), answrOnUse VARCHAR (1024), roomId INT)";

    public static final String ADD_FOREIGN_KEY = "ALTER TABLE OBJECT ADD CONSTRAINT FK FOREIGN KEY (roomId) REFERENCES room(id)";

    public static final String DROP_FOREIGN_KEY = "ALTER TABLE OBJECT DROP CONSTRAINT FK";

    public static final String INSERT_OBJ = "INSERT INTO object VALUES(?,?,?,?,?,?)";

    public static final String RESET_OBJ_TABLE = "TRUNCATE TABLE object";

    public static final String SELECT_NAME_DESCRIPTION_OBJ = "SELECT name, desc FROM object WHERE id = ? ";

    public static final String SELECT_LOOK_OBJ = "SELECT look FROM object WHERE id = ? ";

    public static final String SELECT_ANSWR_ON_USE = "SELECT answrOnUse FROM object WHERE id = ? ";
    
    //dati degli oggetti da caricare su db
    public static final String KNIFE_NAME = "coltello";

    public static final String KNIFE_DESCRIPTION = "Sembra un semplice coltello da cucina anche se in realta' e' molto affilato\n"
            + "attento a non farti male!";

    public static final String KNIFE_LOOK = "Pare che sul tavolo qualcuno abbia lasciato un coltello.";
    
    public static final String KNIFE_USED = "Hai appena infilzato la guardia con un fendente in gola\n"
            + "il sangue sta schizzando ovunque... ";

    public static final String BUTTON_ELECTRIC_METER_NAME = "interruttore";

    public static final String BUTTON_ELECTRIC_METER_DESC = "Interruttore di emergenza del contatore elettrico della banca";

    public static final String BUTTON_ELECTRIC_METER_LOOK = "Sul contatore elettrico sembra esserci un interruttore rosso di emergenza\n"
            + "con su scritto 'ATTENZIONE'\n"
            + "probabilmente viene utilizzato per staccare la corrente in tutto l'edificio.";

    public static final String BUTTON_ELECTRIC_METER_USED = "Hai attivato l'interruttore del contatore della corrente.\n"
            + "Si e' appena generato un blackout nella banca, fra pochi minuti\n"
            + "la corrente verra' riaccesa dall'impianto di emergenza.";

    public static final String SILENCER_NAME = "silenziatore";

    public static final String SILENCER_DESCRIPTION = "Silenziatore una pistola calibro 9\n"
            + "spero che non arrivi il bisogno di usarlo";

    public static final String SILENCER_LOOK = "Alla guardia e' caduto un silenziatore per la pistola\n"
            + "...strano visto che non aveva una pistola con se.";

    public static final String CAMOUFLAGE_NAME = "travestimento";

    public static final String CAMOUFLAGE_DESCRIPTION = "classico smoking nero da guardia della banca con questo non ti beccheranno mai\n"
            + "... o almeno spero";

    public static final String CAMOUFLAGE_LOOK = "Finalmente puoi prendere i vestiti della guardia cosi' da poter"
            + " girare\nliberamente per la banca";

    public static final String CAMOUFLAGE_USED = "Ora puoi girare per la banca senza che nessuno ti scopra.";

    public static final String PHONE_NAME = "telefono";

    public static final String PHONE_DESCRIPTION = "Telefono della stanza della sicurezza,\n"
            + "viene usato per parlare con la polizia in caso ci fosse un pericolo per la banca.";

    public static final String PHONE_LOOK = "Sopra la scrivania c'e' il telefono usato per parlare con la polizia,"
            + "\nforse conviene avvisarli e dirli che e' tutto apposto.";

    public static final String PHONE_USED = "Per fortuna hai avvisato la polizia, tranquillizzandola per il blackout\n"
            + "ora credono che fosse dovuto a semplice manutenzione ordinaria.\n"
            + "Chi l'avrebbe mai detto che ci sarebbero cascati cosi' facilmente...";

    public static final String COMPUTER_NAME = "computer";

    public static final String COMPUTER_DESCRIPTION = "Un computer connesso a diversi schermi"
            + " dove in ognuno si vede cio' che vedono le telecamere.";

    public static final String COMPUTER_LOOK = "Di fronte a te c'e' un computer che probabilmente gestisce"
            + " tutto l'impianto di videosorveglianza della banca\nattraveso esso puoi mettere fuoriuso tutte le telecamere.";

    public static final String COMPUTER_USED = "Ben fatto!! Con le tue doti segrete da hacker hai messo fuori uso tutte le telecamere\n"
            + "e hai distrutto tutti gli hard disk cosi' da eliminare tutto cio' che fosse stato ripreso.";

    public static final String BOILER_BUTTON_NAME = "pulsante";

    public static final String BOILER_BUTTON_DESCRIPTION = "Pulsane dell'impianto termico della banca, se lo premi farai alzare"
            + " a dismisura\nla temperatura della stanza del direttore.";

    public static final String BOILER_BUTTON_LOOK = "Sulla gigantesca caldaia noti diversi pulsanti tra cui uno che regola"
            + " la temperatura nella stanza del direttore\npremilo per farla aumentare!";

    public static final String BOILER_BUTTON_USED = "Hai attivato il pulsante che fa aumentare al massimo la temperatura nella "
            + "stanza del direttore.\n"
            + "La temperatura nella stanza sta aumentando vertiginosamente.\n"
            + "Mi sa che li' dentro fara' un po' caldo...\n"
            + "Il direttore, con le sue guardie del corpo, e' sicuramente andanto via.";

    public static final String LIGHT_BUTTON_NAME = "luce";
    
    public static final String LIGHT_BUTTON_DESCRIPTION = "";
    
    public static final String LIGHT_BUTTON_LOOK = "";

    public static final String LIGHT_BUTTON_USED = "Finalmente hai acceso la luce\nnon si vedeva niente";

    public static final String PASS_NAME = "pass";

    public static final String PASS_DESCRIPTION = "Il pass del direttore...interessante. Ci servira' per aprire il caveau.";

    public static final String PASS_LOOK = "Sembra esserci usa strana tessera qui dentro";

    public static final String DRAWER_NAME = "cassetto";

    public static final String DRAWER_DESCRIPTION = "Una scrivania in legno, sembra avere un cassetto apribile";

    public static final String DRAWER_LOOK = "La scrivania sembra avere un cassetto...";

    public static final String CARPET_NAME = "tappeto";

    public static final String CARPET_DESCRIPTION = "Sembra un tappeto di seta persiano chissa' se nasconde qualcosa";

    public static final String CARPET_LOOK = "Per terra c'e' un grande tappeto persiano che sembra essere stato ricamato a mano\n"
            + "chissa' se nasconde qualcosa...";

    public static final String CARPET_USED = "Hai spostato il tappeto.\nGuarda!! Sembra che stesse nascondendo una botola segreta";

    public static final String SECRET_BUTTON_NAME = "leva";

    public static final String SECRET_BUTTON_DESCRIPTION = "Sembra una leva di ferro che fa azionare un meccanismo segreto.";

    public static final String SECRET_BUTTON_LOOK = "Sotto la botola c'e' una leva segreta\nchissa' che succede tirandola...";

    public static final String SECRET_BUTTON_USED = "Hai tirato la leva segreta.\n"
            + "Guarda! La libreria sembra che si stia iniziando ad aprire come "
            + "se ad EST ci fosse un passaggio segreto\n"
            + "Chissa' cosa c'e' dall'altro lato.";

    public static final String TRAP_DOOR_NAME = "botola";

    public static final String TRAP_DOOR_DESCRIPTION = "Sembra una strana botola di legno, chissa' cosa nasconde...";

    public static final String TRAP_DOOR_LOOK = "";

    public static final String PASSWORD_NAME = "password";

    public static final String PASSWORD_DESCRIPTION = "Sul foglio c'e' scritto uno strano codice,\n"
            + "probabilmente e' la password del caveau che stavamo cercando.";

    public static final String PASSWORD_LOOK = "Si intravede uno strano foglio, chissa' cosa c'e' scritto...";

    public static final String DRILL_NAME = "trapano";

    public static final String DRILL_DESCRIPTION = "Un classico trapano 'Black & Decker' con una punta elicoidale\n"
            + "per forare il metallo, meglio di cosi' non poteva andare!";

    public static final String DRILL_LOOK = "C'e' un trapano arancione e nero con una lunga punta.";
    
    public static final String DRILL_USED = "C'e' voluto del tempo, ma alla fine anche il ferro piu' spesso si buca\n"
            + "la serratura della cassaforte e' andata!";
    
    public static final String PISTOL_NAME = "pistola";
    
    public static final String PISTOL_DESCRIPTION = "Una Beretta calibro 9, una classica pistola per le guardie.";
    
    public static final String PISTOL_LOOK = "Nella vetrina c'e' una Beretta 92, spero non avrai bisogno di usarla...";
    
    public static final String PISTOL_USED = "Complimenti! Hai centrato la guardia con un colpo alla testa degno del migliore cecchino!";
    
    public static final String CASKET_NAME = "cofanetto";
    
    public static final String CASKET_DESCRIPTION = "Un cofanetto portagioielli vecchio stile.";
    
    public static final String CASKET_LOOK = "Quello sembra essere un cofanetto portagioielli,\n"
            + "forse c'e' qualcosa di interessante al suo interno.";
    
    public static final String NECKLACE_NAME = "collana";
    
    public static final String NECKLACE_DESCRIPTION = "Una collana in oro massiccio, sembra essere bella pesante...";
    
    public static final String NECKLACE_LOOK = "Vedo brillare una collana, sembra essere costosa.";
    
    public static final String WATCH_NAME = "orologio";
    
    public static final String WATCH_DESCRIPTION = "Un magnifico Patek Philippe Nautilus, questo si che vale una fortuna!";
    
    public static final String WATCH_LOOK = "C'e' un prezioso orologio meglio non farselo scappare.";
    
    public static final String CABINET_NAME = "vetrina";
    
    public static final String CABINET_DESCRIPTION = "Un mobile di vetro, sicuramente contiene qualcosa di importante.";
    
    public static final String CABINET_LOOK = "Sulla sinistra si nota una vetrina con su scritto 'NON APRIRE'.";

    public static final String SAFE_NAME = "cassaforte";

    public static final String SAFE_DESCRIPTION = "E' una gigantesca cassaforte di ferro hai bisogno di un trapano per aprirla.";

    public static final String SAFE_LOOK = "Quest'ultima e' un'enorme cassaforte nera in acciaio massiccio con chiusura elettronica,\n"
            + "senza l'impronta del direttore ti conviene trapanare la serratura per aprirla.";
    
    public static final String BOX_NAME = "cassetta";
    
    public static final String BOX_DESCRIPTION = "Una cassetta degli attrezzi, aprila per vedere il suo contenuto.";
    
    public static final String BOX_LOOK = "A terra noto una cassetta degli attrezzi, forse contiene qualcosa...";
    
    public static final String DIAMONDS_NAME = "diamanti";
    
    public static final String DIAMONDS_DESCRIPTION = "Una sacca piena dei diamanti piu' brillanti che io abbia mai visto\n"
            + "con questi ci faro' una fortuna.";
    
    public static final String DIAMONDS_LOOK = "C'e' una luce splendente riflessa dalla magnifica purezza dei diamanti\n"
            + "finalmente sono miei!";
    
}
