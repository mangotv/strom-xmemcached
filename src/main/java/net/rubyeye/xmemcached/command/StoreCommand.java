package net.rubyeye.xmemcached.command;

import java.io.Serializable;

/**
 * A store command interface for STORE commands such as SET,ADD
 *
 * @author apple
 */
public interface StoreCommand extends Serializable {

    public void setValue(Object value);

    public Object getValue();
}
