/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import gameEntity.NpcDescription;
import gameEntity.ObjectDescription;
import gameEntity.RoomDescription;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Classe utilizzata per la gestione del db
 * contiene i metodi per la connessione e disconnessione al db
 * e per le interrogazioni di selezione e aggiornamento delle tabelle
 * 
 * @author Barty
 */
public class DBManager {
 
    private Connection conn;
    
    //file contenente la stringa di connessione del database 
    private static final String PATH_FILE_STRING_CONNECTION = "./resources/file/stringConnectionDB";

    /**
     * Metodo per la connessione al database, imposta nome utente e password per la connessione
     * il metodo e' indipendente dal tipo di DB poiche' basta cambiare la stringa di connessione
     * presente su file per cambiare DB, il metodo crea la tabella delle stanze della mappa,
     * degli oggetti e degli npc se non sono state già create.
     * In caso di errore di connessione termina il programma.
     */
    public void connect() {

        try {
            Properties dbprops = new Properties();
            dbprops.setProperty("user", "psp");
            dbprops.setProperty("password", "projectMap2021");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE_STRING_CONNECTION));
                String connection = reader.readLine().trim();
                conn = DriverManager.getConnection(connection, dbprops);
            } catch (IOException e) {
                System.err.println("IO Exception: " + e.getMessage()
                        + "\n Impossibile collegarsi al db senza una stringa di connessione valida."
                        + "\n Programma terminato.");
                System.exit(0);
            }
            Statement stm = conn.createStatement();
            stm.executeUpdate(RoomDescription.CREATE_ROOM_TABLE);
            stm.executeUpdate(ObjectDescription.CREATE_OBJ_TABLE);
            stm.executeUpdate(NpcDescription.CREATE_NPC_TABLE);
            stm.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nImpossibile collegarsi al db.\nProgramma terminato");
            System.exit(0);
        }

    }
    
    /**
     * Metodo per aggiumgere vincoli alle tabelle
     */
    public void addConstraint() {
        try{
            Statement stm = conn.createStatement();
            stm.executeUpdate(ObjectDescription.ADD_FOREIGN_KEY);
            stm.close();            
        }catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nImpossibile collegarsi al db.\nProgramma terminato");
            System.exit(0);
        }
    }

    //tipicamente usato per inserire le stanze

    /**
     * Metodo per inserimento delle tuple
     * tipicamente usato per l'inserimento delle stanze
     * utilizza query preimpostate
     * @param queryUpdate
     * @param id codice identificativo
     * @param name  nome 
     * @param desc descrizione usata quando si entra in una stanza
     * @param look descizione usata quando si osserva una stanza
     * @param lock
     */
    public void insert(String queryUpdate, int id, String name, String desc, String look, String lock) {
 
        try {
            PreparedStatement pstm = conn.prepareStatement(queryUpdate);
            pstm.setInt(1, id);
            pstm.setString(2, name);
            pstm.setString(3, desc);
            pstm.setString(4, look);
            pstm.setString(5, lock);
            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nell'inserimento dei dati nella tabella.\nProgramma terminato");
            System.exit(0);
        }

    }
    
    //tipicamente usato per inserire gli npc
    public void insert(String queryUpdate, int id, String name, String desc, String look, int roomId) {

        try {
            PreparedStatement pstm = conn.prepareStatement(queryUpdate);
            pstm.setInt(1, id);
            pstm.setString(2, name);
            pstm.setString(3, desc);
            pstm.setString(4, look);
            pstm.setInt(5, roomId);
            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nell'inserimento dei dati nella tabella.\nProgramma terminato");
            System.exit(0);
        }

    }   
    
    //tipicamente usato per inserire gli oggetti
    public void insert(String queryUpdate, int id, String name, String desc, String look, String answrOnUse, int roomId) {

        try {
            PreparedStatement pstm = conn.prepareStatement(queryUpdate);
            pstm.setInt(1, id);
            pstm.setString(2, name);
            pstm.setString(3, desc);
            pstm.setString(4, look);
            pstm.setString(5, answrOnUse);
            pstm.setInt(6, roomId);
            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nell'inserimento dei dati nella tabella.\nProgramma terminato");
            System.exit(0);
        }

    }   

    /**
     * Metodo per recuperare i dati relativi al nome e alla desccrizione 
     * di un entita' attraverso il codice identificativo
     * @param id
     * @param querySelection
     * @return
     */
    public String getNameDesc(int id, String querySelection) {

        String result = "";
        try {
            PreparedStatement pstm = conn.prepareStatement(querySelection);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                result +=  RoomDescription.SEPARATOR + "\n" +
                        rs.getString(1) +
                        "\n"
                        + rs.getString(2) +
                        "\n" + RoomDescription.SEPARATOR;
            }
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nel recupero delle informazioni.");
        }
        return result;

    }

    /**
     * Metodo per recuperare le descrizioni legate tipicamente
     * al comando di tipo 'look at'
     * @param id
     * @param querySelection
     * @return 
     */
    public String getLook(int id, String querySelection) {

        String result = "";
        try {
            PreparedStatement pstm = conn.prepareStatement(querySelection);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                result += "\n" + rs.getString(1);
            }

        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nel recupero delle informazioni.");
        }
        return result;

    }
    
    /**
     * Metodo per cancellare il contenuto delle tabelle ad ogni avvio 
     * del programma, senza di esso verrebbero violati i vincoli di chiave
     * poichè ad ogni avvio verrebbero reinserite le stesse tuple
     * @param resetTable 
     */
    public void resetTable(String resetTable) {
        
        try {
            Statement stm = conn.createStatement();
            stm.execute(resetTable);
            stm.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nel reset della tabella.");
        }
        
    }

    /**
     * Metodo per la disconnesione del database
     */
    public void disconnect() {

        try {
            conn.close();
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore di disconnessione dal db.\nProgramma terminato");
            System.exit(0);
        }

    }  

    public String getLookRoomObj(int roomId, int objId, String querySelection) {

        String result = "";
        try {
            PreparedStatement pstm = conn.prepareStatement(querySelection);
            pstm.setInt(1, roomId);
            pstm.setInt(2, objId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                result += rs.getString(1) + "\n" + rs.getString(2);
            }
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nel recupero delle informazioni.");
        }
        return result;

    }
    
    public String getLookRoomObjNpc(int roomId, int objId, int npcId, String querySelection) {

        String result = "";
        try {
            PreparedStatement pstm = conn.prepareStatement(querySelection);
            pstm.setInt(1, roomId);
            pstm.setInt(2, objId);
            pstm.setInt(3, npcId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                result += rs.getString(1) + "\n" + rs.getString(2 + "\n" + rs.getString(3)) ;
            }
        } catch (SQLException er) {
            System.err.println("SQL Exception: " + er.getMessage() + "\nSQL state: " + er.getSQLState()
                    + "\nErrore nel recupero delle informazioni.");
        }
        return result;

    }

}
