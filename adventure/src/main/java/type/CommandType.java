/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package type;

/**
 *
 * @author 
 */
public enum CommandType {
    END, INVENTORY, NORD,

    /**
     *
     */
    SOUTH,

    /**
     *
     */
    EAST, WEST, OPEN, CLOSE, PUSH, PULL, WALK_TO, PICK_UP, DROP,

    /**
     *
     */
    TALK_TO, GIVE, USE, LOOK_AT, TURN_ON, TURN_OFF, KILL, HELP, EXAMINE, SAVE, LOAD, TIME;
}
