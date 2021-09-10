/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameEntity;

/**
 * Interfaccia contenente costanti per le interrogazioni al db
 * @author Barty
 */
public interface NpcDescription {

    //query per l'aggiornamento e l'interrogazione della tabella delle stanze
    public static final String CREATE_NPC_TABLE = "CREATE TABLE IF NOT EXISTS npc (id INT PRIMARY KEY, name VARCHAR(64), desc VARCHAR(1024),"
            + " look VARCHAR (1024), kill VARCHAR (1024), roomId INT)";

    public static final String ADD_FOREIGN_KEY = "ALTER TABLE OBJECT ADD CONSTRAINT FK FOREIGN KEY (roomId) REFERENCES room(id)";

    public static final String DROP_FOREIGN_KEY = "ALTER TABLE OBJECT DROP CONSTRAINT FK";

    public static final String INSERT_NPC = "INSERT INTO npc VALUES(?,?,?,?,?,?)";

    public static final String RESET_NPC_TABLE = "TRUNCATE TABLE npc";

    public static final String SELECT_NAME_DESCRIPTION_NPC = "SELECT name, desc FROM npc WHERE id = ? ";

    public static final String SELECT_LOOK_NPC = "SELECT look FROM npc WHERE id =? ";
    
    public static final String SELECT_DEATH_NPC = "SELECT kill FROM npc WHERE id =? ";

    //descrizioni npcs
    public static final String GUARD1_NAME = "Guardia di sicurezza";

    public static final String GUARD1_DESCRIPTION = "Una guardia armata della banca.";

    public static final String GUARD1_LOOK = "E' appena entrata una guardia a controllare il motivo del blackout.\n"
            + "Uccidila prima che lei faccia lo stesso con te o chiami i rinforzi!";
    
    public static final String GUARD1_DEATH = "Appena in tempo, il blackout e' terminato ed e' tornata la luce.\n"
                + "Forse dovresti avvertire la polizia che il blackout era programmato\n"
                + "altrimenti potrebbero insospettirsi.";
    
    public static final String GUARD2_LOOK = "Per il forte rumore del trapano e' appena entrata una guardia\n"
            + "Uccidila prima che lei faccia lo stesso con te o chiami i rinforzi.";
    
    public static final String GUARD2_DEATH = "Per fortuna ce l'ho fatta, pensavo davvero che per me fosse finita...\n"
            + "Finalmente posso prendere il bottino!";
    
}
