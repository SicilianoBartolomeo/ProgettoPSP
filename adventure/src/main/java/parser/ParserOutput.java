/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import type.AdvObject;
import type.Command;
import type.Npc;

/**
 *
 * @author Barty
 */
public class ParserOutput {

    private Command command;

    private AdvObject object1;
    
    private AdvObject object2;
    
    private AdvObject invObject1;
   
    private AdvObject invObject2;
    
    private Npc npc1;
    
    private Npc npc2;

    public ParserOutput(Command command, AdvObject object1, AdvObject object2, AdvObject invObject1, AdvObject invObject2, Npc npc1, Npc npc2) {
        this.command = command;
        this.object1 = object1;
        this.object2 = object2;
        this.invObject1 = invObject1;
        this.invObject2 = invObject2;
        this.npc1 = npc1;
        this.npc2 = npc2;
    }

    public Command getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    public AdvObject getObject1() {
        return object1;
    }

    public void setObject1(AdvObject object1) {
        this.object1 = object1;
    }

    public AdvObject getObject2() {
        return object2;
    }

    public void setObject2(AdvObject object2) {
        this.object2 = object2;
    }

    /**
     *
     * @return
     */
    public AdvObject getInvObject1() {
        return invObject1;
    }

    public void setInvObject1(AdvObject invObject1) {
        this.invObject1 = invObject1;
    }

    public AdvObject getInvObject2() {
        return invObject2;
    }

    public void setInvObject2(AdvObject invObject2) {
        this.invObject2 = invObject2;
    }

    public Npc getNpc1() {
        return npc1;
    }

    public void setNpc1(Npc npc1) {
        this.npc1 = npc1;
    }

    public Npc getNpc2() {
        return npc2;
    }

    public void setNpc2(Npc npc2) {
        this.npc2 = npc2;
    }
   
}
