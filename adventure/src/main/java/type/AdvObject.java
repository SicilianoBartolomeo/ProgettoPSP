/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity
 * Classe che rappresenta gli oggetti
 * @author psp
 */
public class AdvObject implements Serializable {

    private final int id;

    private String name;

    private Set<String> alias;      //insieme dei sinonimi

    private specialObject specialObj;   //indica se e' un oggetto particolare influente nella storia del gioco

    private boolean toKill;             //indica se l'oggetto puo' essere utilizzato nel comando 'kill'

    private boolean openable;

    private boolean pickupable;

    private boolean pushable;

    private boolean usable;

    private boolean open;

    private boolean push;

    private int contained;      //attributo usato per chiudere un oggetto contenitore
                                //il valore contenuto e' pari all'id dell'oggetto che lo contiene
    
    public enum specialObject {
        ELECTRICITY, BOILER, LIGHT, SECRET_BUTTON, CAMOUFLAGE, DIAMONDS,
        PHONE, COMPUTER, CARPET, PASS, PASSWORD, DRILL, CASSAFORTE, JEWERLY;
    }

    public AdvObject(int id) {
        this.id = id;
        this.name = new String();
        this.specialObj = null;
        this.toKill = false;
        this.openable = false;
        this.pickupable = false;
        this.pushable = false;
        this.open = false;
        this.push = false;
        this.contained = -1;
    }
    
    public AdvObject(int id, String name) {
        this.id = id;
        this.name = name;
        this.specialObj = null;
        this.toKill = false;
        this.openable = false;
        this.pickupable = false;
        this.pushable = false;
        this.open = false;
        this.push = false;
        this.contained = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openable) {
        this.openable = openable;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public specialObject getSpecialObj() {
        return specialObj;
    }

    public void setSpecialObj(specialObject specialObj) {
        this.specialObj = specialObj;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    public int getId() {
        return id;
    }

    public boolean isToKill() {
        return toKill;
    }

    public void setToKill(boolean toKill) {
        this.toKill = toKill;
    }

    public int getContained() {
        return contained;
    }

    public void setContained(int contained) {
        this.contained = contained;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdvObject other = (AdvObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
