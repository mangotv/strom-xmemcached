package net.rubyeye.xmemcached.impl;

import net.rubyeye.xmemcached.MemcachedSessionLocator;

import java.io.Serializable;

/**
 * Abstract session locator
 * 
 * @author dennis
 * @date 2010-12-25
 */
public abstract class AbstractMemcachedSessionLocator implements
		MemcachedSessionLocator ,Serializable{

	protected boolean failureMode;

	public void setFailureMode(boolean failureMode) {
		this.failureMode = failureMode;

	}

}
