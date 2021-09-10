/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 * Eccezione generata quando nella frase e' presente piu' di un comando per volta.
 * @author Barty
 */
public class InvalidCommandException extends Exception{
    
    @Override
    public String getMessage(){
        return "Errore! Nella frase deve essere presente un solo comando per volta.\n"
                + "In caso di necessita richiamare l'help dei comandi.";
    }
    
}
