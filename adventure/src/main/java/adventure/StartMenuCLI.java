/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import gameEntity.RoomDescription;
import games.TheItalianJob;
import java.io.PrintStream;
import java.util.Scanner;
import parser.InvalidCommandException;
import parser.InvalidSentenceException;
import parser.ParserOutput;
import type.CommandType;

/**
 * Boundary
 * Classe per avviare il programma tramite linea di comando
 *
 * @author Barty
 */
public class StartMenuCLI {

    /**
     * Metodo per iniziare il programma: stampa un menu iniziale dal quale e'
     * possibile iniziare una partita, caricare una partita salvata o terminare
     * il programma
     *
     * @param out printstream usato per stampare su terminale
     */
    public static void startMenu(PrintStream out) {
        
        out.println("============================================================================");
        out.println("|                          THE ITALIAN JOB                                 |");
        out.println("============================================================================");
        out.println("|                                                                          |");
        out.println("|  [ 1 ] Inizia una nuova partita.                                         |");
        out.println("|  [ 2 ] Caricare una partita salvata.                                     |");
        out.println("|  [ 3 ] Esci dal gioco                                                    |");
        out.println("|                                                                          |");
        out.println("____________________________________________________________________________");
        out.println("============================================================================");

        Scanner scanner = new Scanner(System.in);
        Engine engine = new Engine(new TheItalianJob());

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (!command.isEmpty()) {
                switch (command) {
                    case "1":
                        out.println(RoomDescription.INTRO);
                        execute(engine);
                        break;
                    case "2":
                         try {
                        ParserOutput pa = engine.getParser().parse("carica", engine.getGame().getCommands(),
                                engine.getGame().getCurrentRoom().getObjects(), engine.getGame().getInventory(),
                                engine.getGame().getNpcs());
                        String answer = engine.getGame().nextMove(pa, engine);
                        if (answer.contains("fallito")) {
                            out.println(answer);
                        } else {
                            out.println(answer);
                            execute(engine);
                        }
                    } catch (InvalidSentenceException | InvalidCommandException e) {

                    }
                    break;
                    case "3":
                        out.println("Addio...");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        out.println("Seleziona una delle 3 possibili opzioni.");
                        break;
                }
            }
        }
    }

    /**
     * Metodo per gestire input-output con l'utente
     * prende una frase in input, la manda al parser e se e'
     * corretta la invia al metodo nectMove() del gioco implementato
     * il quale esegue la mossa, se inserita correttamente, e restituisce
     * una stringa di risposta che viene stampata
     * @param engine 
     */
    public static void execute(Engine engine) {
        
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            
            if (!command.isEmpty()) {
                try {
                    ParserOutput p = engine.getParser().parse(command, engine.getGame().getCommands(),
                            engine.getGame().getCurrentRoom().getObjects(), engine.getGame().getInventory(), engine.getGame().getNpcs());
                    //se l'utente vuole uscire dal gioco
                    if (p.getCommand().getType() == CommandType.END) {
                        System.out.println("Sei sicuro? I progressi non salvati andranno persi\n"
                                + "Digita 'si' per confermare -->");
                        command = scanner.nextLine();
                        if (command.toLowerCase().equals("si")) {
                            System.out.println("Addio...");
                            scanner.close();
                            System.exit(0);
                        }
                        
                    } else {
                        String answer = engine.getGame().nextMove(p, engine);
                        System.out.println(answer);
                        //se la partita e' terminata (se si finisce la trama)
                        if (answer.contains("#")) {
                            scanner.close();
                            System.exit(0);
                        }
                    }
                } catch (InvalidSentenceException | InvalidCommandException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("Non hai digitato niente. Digita qualcosa!");
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        startMenu(System.out);
    }

}
