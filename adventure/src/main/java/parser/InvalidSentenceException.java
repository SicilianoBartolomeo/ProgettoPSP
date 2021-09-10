/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 * Eccezione generata quando nella frase non sono presenti parole valide o non e' presente un comando valido.
 * @author Barty
 */
public class InvalidSentenceException extends Exception{
    
    @Override
    public String getMessage() {
        return "La frase inserita non è valida.\nIn caso di necessita' richiamare l'help dei comandi.";
    }
    
}
