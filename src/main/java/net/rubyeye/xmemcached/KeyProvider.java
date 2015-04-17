package net.rubyeye.xmemcached;

import java.io.Serializable;

/**
 * Key provider to pre-process keys before sending to memcached.
 * 
 * @author dennis<killme2008@gmail.com>
 * @since 1.3.8
 */
public interface KeyProvider  extends Serializable {
	/**
	 * Processes key and returns a new key.
	 * 
	 * @param key
	 * @return
	 */
	public String process(String key);
}
