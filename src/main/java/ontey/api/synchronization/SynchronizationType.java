package ontey.api.synchronization;

/**
 * Either {@link #SYNCHRONOUS}, which indicates the main thread, or {@link #ASYNCHRONOUS},
 * which indicates a background thread; a different one than the main thread.
 */

public enum SynchronizationType {
	
	/**
	 * Something synchronous (sync) is run on the main thread
	 */
	
	SYNCHRONOUS,
	
	/**
	 * Something asynchronous (async) is run on a background thread
	 */
	
	ASYNCHRONOUS
}
