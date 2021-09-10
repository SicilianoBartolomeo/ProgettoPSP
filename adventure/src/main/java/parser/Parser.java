/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;
import type.AdvObject;
import type.Command;
import type.Npc;
import java.util.List;
import java.util.Set;

/**
 * Control
 * Classe che implementa il parser, si occupa di prendere una frase
 * in input e restituire un oggetto di tipo ParserOutput, se corretta.
 * Se la frase non e' corretta genera eccezioni.
 * Il parser accetta frasi contenenti un verbo seguito da
 * due entita' (tra oggetti presenti nella stanza, oggetti dell'inventario, npc),
 * dove non importa l'ordine in cui sono inserite.
 * Il parser accetta frasi con maiuscole o minuscole, segni di punteggiatura,
 * simboli speciali, articoli, preposizioni e aggettivi.
 * @author Barty
 */
public class Parser {

    //insieme delle parole da 'saltare' contenute su file
    private final Set<String> stopwords;

    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    /**
     * Funzione che controlla se il token passato in input e' presente
     * nella lista dei comandi, in caso positivo restituisce l'indice
     * della posizione in cui si trova nella lista, altrimenti ritorna -1
     * @param token
     * @param commands
     * @return 
     */
    private int checkForCommand(String token, List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getAlias() != null) {
                if (commands.get(i).getName().equals(token) || commands.get(i).getAlias().contains(token)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Funzione che controlla se il token passato in input e' presente
     * nella lista degli oggetti, in caso positivo restituisce l'indice
     * della posizione in cui si trova nella lista, altrimenti ritorna -1
     * @param token
     * @param commands
     * @return 
     */
    private int checkForObject(String token, List<AdvObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getAlias() != null) {
                if (objects.get(i).getName().equals(token) || objects.get(i).getAlias().contains(token)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Funzione che controlla se il token passato in input e' presente
     * nella lista degli npcs, in caso positivo restituisce l'indice
     * della posizione in cui si trova nella lista, altrimenti ritorna -1
     * @param token
     * @param commands
     * @return 
     */
    private int checkForNpc(String token, List<Npc> npc) {
        for (int i = 0; i < npc.size(); i++) {
            if (npc.get(i).getAlias() != null) {
                if (npc.get(i).getName().equals(token) || npc.get(i).getAlias().contains(token)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isNull(int n) {
        return n < 0;
    }

    /**
     * Funzione che rimuove la punteggiatura e i caratteri speciali dalla
     * stringa in input, converte i caratteri in minuscolo e divide la stringa
     * quando incontra gli spazi.
     *
     * @param string
     * @param stopwords
     * @return
     */
    public List<String> parseString(String string, Set<String> stopwords) {
        List<String> tokens = new ArrayList<>();
        String[] split = string.replaceAll("[^a-zA-Zùìèéàò]", " ").toLowerCase().split("\\s+");
        for (String t : split) {
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        return tokens;
    }

    /**
     * Funzione che in base agli indici passati in input mostra le entita'
     * trovate (es. oggetti, oggetti inventario, npc)
     *
     * @param obj1
     * @param objInv1
     * @param obj2
     * @param objInv2
     * @param npc1
     * @param npc2
     * @return
     */
    private short wordsFound(int obj1, int objInv1, int obj2, int objInv2, int npc1, int npc2) {
        short x = 0;
        //oggetto
        if (!(isNull(obj1)) && isNull(obj2) && isNull(objInv1) && isNull(objInv2) && isNull(npc1) && isNull(npc2)) {
            x = 1;
        }          //oggetto, oggetto
        if (!(isNull(obj1)) && !(isNull(obj2)) && isNull(objInv1) && isNull(objInv2) && isNull(npc1) && isNull(npc2)) {
            x = 2;
        }          //oggetto, oggettoInventario 
        if (!(isNull(obj1)) && isNull(obj2) && isNull(objInv1) && !(isNull(objInv2) && isNull(npc1) && isNull(npc2))) {
            x = 3;
        }          //oggetto, npc 
        if (!(isNull(obj1)) && isNull(obj2) && isNull(objInv1) && isNull(objInv2) && isNull(npc1) && !isNull(npc2)) {
            x = 4;
        }          //oggettoInventario
        if ((isNull(obj1)) && isNull(obj2) && !(isNull(objInv1)) && isNull(objInv2) && isNull(npc1) && isNull(npc2)) {
            x = 5;
        }          //oggettoInventario, oggettoInventario
        if ((isNull(obj1)) && isNull(obj2) && !(isNull(objInv1)) && !(isNull(objInv2)) && isNull(npc1) && isNull(npc2)) {
            x = 6;
        }          //oggetto inventario, oggetto
        if ((isNull(obj1)) && !(isNull(obj2)) && !(isNull(objInv1)) && isNull(objInv2) && isNull(npc1) && isNull(npc2)) {
            x = 7;
        }          //oggettoInventario, npc 
        if (isNull(obj1) && isNull(obj2) && !isNull(objInv1) && isNull(objInv2) && isNull(npc1) && !isNull(npc2)) {
            x = 8;
        }          //npc
        if (isNull(obj1) && isNull(obj2) && isNull(objInv1) && isNull(objInv2) && !(isNull(npc1)) && isNull(npc2)) {
            x = 9;
        }          //npc oggetto
        if (isNull(obj1) && !(isNull(obj2)) && isNull(objInv1) && isNull(objInv2) && !isNull(npc1) && isNull(npc2)) {
            x = 10;
        }          //npc, oggettoInventario
        if (isNull(obj1) && isNull(obj2) && isNull(objInv1) && !(isNull(objInv2)) && !(isNull(npc1)) && isNull(npc2)) {
            x = 11;
        }
        return x;
    }

    /**
     * override della funzione precedente usato per giochi che non possiedono gli npc
     * @param obj1
     * @param objInv1
     * @param obj2
     * @param objInv2
     * @return 
     */
    private short wordsFound(int obj1, int objInv1, int obj2, int objInv2) {
        short x = 0;
        //oggetto
        if (!(isNull(obj1)) && isNull(obj2) && isNull(objInv1) && isNull(objInv2)) {
            x = 1;
        }          //oggetto, oggetto
        if (!(isNull(obj1)) && !(isNull(obj2)) && isNull(objInv1) && isNull(objInv2)) {
            x = 2;
        }          //oggetto, oggettoInventario 
        if (!(isNull(obj1)) && isNull(obj2) && isNull(objInv1) && !(isNull(objInv2))) {
            x = 3;
        }         //oggettoInventario
        if ((isNull(obj1)) && isNull(obj2) && !(isNull(objInv1)) && isNull(objInv2)) {
            x = 4;
        }          //oggettoInventario, oggettoInventario
        if ((isNull(obj1)) && isNull(obj2) && !(isNull(objInv1)) && !(isNull(objInv2))) {
            x = 5;
        }          //oggetto inventario, oggetto
        if ((isNull(obj1)) && !(isNull(obj2)) && !(isNull(objInv1)) && isNull(objInv2)) {
            x = 6;
        }
        return x;
    }

    /**
     * Il parser prende in input la stanza corrente con gli oggetti al suo
     * interno, l'inventario, gli npcs, l'insieme dei comandi e la frase scritta
     * dall'utente. Si occupa di riconoscere frasi dove e' presente un
     * comando eventualmente accompaganto da due parole del tipo oggetto,
     * oggettoInventario o npc combinate tra loro a coppie, inoltre non e' key
     * sensitive e ignora tutto cio' che non e' rilevante ad esempio articoli,
     * preposizioni, aggettivi e caratteri speciali.
     *
     * @param sentence
     * @param commands
     * @param objects
     * @param inventory
     * @param npc
     * @return un oggetto della classe ParserOutput
     * @throws InvalidSentenceException nel caso nella frase non sia presente un
     * comando o parole valide
     * @throws parser.InvalidCommandException nel caso sia present epiu' di un verbo
     */
    public ParserOutput parse(String sentence, List<Command> commands, List<AdvObject> objects, List<AdvObject> inventory,
            List<Npc> npc) throws InvalidSentenceException, InvalidCommandException {
        List<String> tokens = parseString(sentence, stopwords);

        int ic = -1;
        int ic2 = -1;
        int iObj1 = -1;
        int iObjInv1 = -1;
        int iObj2 = -1;
        int iObjInv2 = -1;
        int iNpc1 = -1;
        int iNpc2 = -1;
        if (!(tokens.isEmpty()) && tokens.size() < 4) {

            ic = checkForCommand(tokens.get(0), commands);
            if (ic >= 0) {
                if (tokens.size() > 1) {
                    for (int i = 1; i < tokens.size(); i++) {
                        ic2 = checkForCommand(tokens.get(i), commands);
                        if (isNull(ic2)) {
                            //ricerca prima parola chiave (oggetto, oggetto inventario o npc)
                            if (isNull(iObj1) && isNull(iObjInv1) && isNull(iNpc1)) {
                                iObj1 = checkForObject(tokens.get(i), objects);
                                if (isNull(iObj1)) {
                                    iObjInv1 = checkForObject(tokens.get(i), inventory);
                                    if (isNull(iObj1)) {
                                        iNpc1 = checkForNpc(tokens.get(i), npc);
                                    }
                                }
                            } else {
                                //ricerca seconda parola chiave
                                iObj2 = checkForObject(tokens.get(i), objects);
                                if (isNull(iObj2)) {
                                    iObjInv2 = checkForObject(tokens.get(i), inventory);
                                    if (isNull(iObjInv2)) {
                                        iNpc2 = checkForNpc(tokens.get(i), npc);
                                    }
                                }
                            }
                        } else {
                            throw new InvalidCommandException();
                        }
                    }
                    short x = wordsFound(iObj1, iObjInv1, iObj2, iObjInv2, iNpc1, iNpc2);
                    switch (x) {
                        case 1:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), null, null, null, null, null);
                        case 2:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), objects.get(iObj2), null, null, null, null);
                        case 3:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), null, null, inventory.get(iObjInv2), null, null);
                        case 4:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), null, null, null, null, npc.get(iNpc2));
                        case 5:
                            return new ParserOutput(commands.get(ic), null, null, inventory.get(iObjInv1), null, null, null);
                        case 6:
                            return new ParserOutput(commands.get(ic), null, null, inventory.get(iObjInv1), inventory.get(iObjInv2), null, null);
                        case 7:
                            return new ParserOutput(commands.get(ic), null, objects.get(iObj2), inventory.get(iObjInv1), null, null, null);
                        case 8:
                            return new ParserOutput(commands.get(ic), null, null, inventory.get(iObjInv1), null, null, npc.get(iNpc2));
                        case 9:
                            return new ParserOutput(commands.get(ic), null, null, null, null, npc.get(iNpc1), null);
                        case 10:
                            return new ParserOutput(commands.get(ic), null, objects.get(iObj2), null, null, npc.get(iNpc1), null);
                        case 11:
                            return new ParserOutput(commands.get(ic), null, null, null, inventory.get(iObjInv2), npc.get(iNpc1), null);
                    }
                }
                return new ParserOutput(commands.get(ic), null, null, null, null, null, null);
            } else {
                //comando non trovato  
                throw new InvalidSentenceException();
            }
        } else {
            //vettore tokens vuoto
            throw new InvalidSentenceException();
        }
    }

    /**
     * Parser con il medesimo funzionamento che non prende in input gli npc.
     * Usato per avventure che non presentano npc.
     *
     * @param sentence
     * @param commands
     * @param objects
     * @param inventory
     * @return
     * @throws InvalidSentenceException
     * @throws InvalidCommandException
     */
    public ParserOutput parse(String sentence, List<Command> commands, List<AdvObject> objects, List<AdvObject> inventory)
            throws InvalidSentenceException, InvalidCommandException {
        List<String> tokens = parseString(sentence, stopwords);

        int ic = -1;
        int ic2 = -1;
        int iObj1 = -1;
        int iObjInv1 = -1;
        int iObj2 = -1;
        int iObjInv2 = -1;
        if (!tokens.isEmpty()) {

            ic = checkForCommand(tokens.get(0), commands);
            if (ic >= 0) {
                if (tokens.size() > 1) {
                    for (int i = 1; i < tokens.size(); i++) {
                        ic2 = checkForCommand(tokens.get(i), commands);
                        if (isNull(ic2)) {
                            //ricerca prima parola chiave (oggetto, oggetto inventario o npc)
                            if (isNull(iObj1) && isNull(iObjInv1)) {
                                iObj1 = checkForObject(tokens.get(i), objects);
                                if (isNull(iObj1)) {
                                    iObjInv1 = checkForObject(tokens.get(i), inventory);
                                }
                            } else {
                                //ricerca seconda parola chiave
                                iObj2 = checkForObject(tokens.get(i), objects);
                                if (isNull(iObj2)) {
                                    iObjInv2 = checkForObject(tokens.get(i), inventory);
                                }
                            }
                        } else {
                            throw new InvalidCommandException();
                        }
                    }
                    short x = wordsFound(iObj1, iObjInv1, iObj2, iObjInv2);
                    switch (x) {
                        case 1:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), null, null, null, null, null);
                        case 2:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), objects.get(iObj2), null, null, null, null);
                        case 3:
                            return new ParserOutput(commands.get(ic), objects.get(iObj1), null, null, inventory.get(iObjInv2), null, null);
                        case 4:
                            return new ParserOutput(commands.get(ic), null, null, inventory.get(iObjInv1), null, null, null);
                        case 5:
                            return new ParserOutput(commands.get(ic), null, null, inventory.get(iObjInv1), inventory.get(iObjInv2), null, null);
                        case 6:
                            return new ParserOutput(commands.get(ic), null, objects.get(iObj2), inventory.get(iObjInv1), null, null, null);
                    }
                }
                return new ParserOutput(commands.get(ic), null, null, null, null, null, null);
            } else {
                //comando non trovato  
                throw new InvalidSentenceException();
            }
        } else {
            //vettore tokens vuoto
            throw new InvalidSentenceException();
        }
    }

}
