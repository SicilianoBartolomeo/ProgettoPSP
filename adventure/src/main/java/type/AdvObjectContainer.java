/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta gli oggetti contenitori che eredita da Advobject
 * @author psp
 */
public class AdvObjectContainer extends AdvObject {

    //lista degli oggetti contenuti
    private List<AdvObject> list = new ArrayList<>();

    public AdvObjectContainer(int id) {
        super(id);
    }

    public AdvObjectContainer(int id, String name) {
        super(id, name);
    }

    public List<AdvObject> getList() {
        return list;
    }

    public void setList(List<AdvObject> list) {
        this.list = list;
    }

    public void add(AdvObject o) {
        list.add(o);
    }

    public void remove(AdvObject o) {
        list.remove(o);
    }

}
