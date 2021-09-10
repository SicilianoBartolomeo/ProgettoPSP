/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa e gestisce il punteggio gella partita.
 * @author Barty
 */
public class Score implements Serializable{
    
    private int score;
    
    private int moves;
    
    private List<AdvObject> objects =  new ArrayList<>();

    public Score() {
        this.score = 60;
        this.moves = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }

    public void setObjects(List<AdvObject> objects) {
        this.objects = objects;
    }
    
    public int getTotalScore(){
        calculateScore();
        return getScore();
    }
    
    /**
     * Il punteggio parte da 60, per ogni oggetto 'speciale' con cui si e'
     * interagito si aggiungono 10 punti, quando il numero di mosse supera
     * il numero minimo per ogni mossa in piu' toglie un punto.
     */
    private void calculateScore(){
        if (getMoves() > 60){
          int moreMove = getMoves() - 60;
                setScore(getScore() - moreMove);   
        }
        for (AdvObject o : getObjects()) {
            if(o.getSpecialObj()!=null){
                switch (o.getSpecialObj()){
                    case PHONE:
                        setScore(getScore() + 10);
                        break;
                    case COMPUTER:
                        setScore(getScore() + 10);
                        break;
                    case JEWERLY:
                        setScore(getScore() + 10);
                        break;
                }
            }
        }
    }
    
    
}
