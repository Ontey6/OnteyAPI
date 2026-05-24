package ontey.api.scheduler;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@AllArgsConstructor(onConstructor_ = @ApiStatus.Internal)
public class OnteyScheduler {
	
	private static final BukkitScheduler scheduler = Bukkit.getScheduler();
	
	private final Plugin plugin;
	
	/**
	 * Calls a method on the main thread and returns a Future object. This
	 * task will be executed by the main server thread.
	 * <br><br>
	 * Note:
	 * <ul>
	 * <li>The Future.get() methods must NOT be called from the main thread.
	 * <li>There is at least an average of 10ms latency until the isDone() method returns true.
	 * </ul>
	 *
	 * @param task Task to be executed
	 * @return Future object related to the task
	 */
	
	public @NonNull <T> Future<T> callSyncMethod(@NonNull Callable<T> task) {
		return scheduler.callSyncMethod(plugin, task);
	}
	
	/**
	 * Removes all tasks associated with this scheduler's plugin from the scheduler.
	 */
	
	public void cancelTasks() {
		scheduler.cancelTasks(plugin);
	}
	
	/**
	 * Check if the task currently running.
	 * <br>
	 * A repeating task might not be running currently, but will be running in
	 * the future. A task that has finished, and does not repeat, will not be
	 * running ever again.
	 * <br>
	 * Explicitly, a task is running if there exists a thread for it, and that
	 * thread is alive.
	 *
	 * @param taskId The task to check.
	 * <br>
	 * @return If the task is currently running.
	 */
	
	public boolean isCurrentlyRunning(int taskId) {
		return scheduler.isCurrentlyRunning(taskId);
	}
	
	/**
	 * Check if the task queued to be run later.
	 * <br>
	 * If a repeating task is currently running, it might not be queued now
	 * but could be in the future. A task that is not queued, and not running,
	 * will not be queued again.
	 *
	 * @param taskId The task to check.
	 * <br>
	 * @return If the task is queued to be run.
	 */
	
	public boolean isQueued(int taskId) {
		return scheduler.isQueued(taskId);
	}
	
	/**
	 * Returns a list of all active workers.
	 * <br>
	 * This list contains async tasks that are being executed by separate
	 * threads.
	 *
	 * @return Active workers
	 */
	
	public @NonNull List<BukkitWorker> getActiveWorkers() {
		return scheduler.getActiveWorkers();
	}
	
	/**
	 * Returns a list of all pending tasks. The ordering of the tasks is not
	 * related to their order of execution.
	 *
	 * @return Active workers
	 */
	
	public @NonNull List<BukkitTask> getPendingTasks() {
		return scheduler.getPendingTasks();
	}
	
	/**
	 * Returns a task that will run on the next server tick.
	 *
	 * @param task the task to be run
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTask(@NonNull Runnable task) throws IllegalArgumentException {
		return scheduler.runTask(plugin, task);
	}
	
	/**
	 * Returns a task that will run on the next server tick.
	 *
	 * @param task the task to be run
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTask(@NonNull Consumer<? super BukkitTask> task) throws IllegalArgumentException {
		scheduler.runTask(plugin, task);
	}
	
	/**
	 * @param task the task to be run
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTask(@NonNull BukkitRunnable task) throws IllegalArgumentException {
		return task.runTask(plugin);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will run asynchronously.
	 *
	 * @param task the task to be run
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskAsync(@NonNull Runnable task) throws IllegalArgumentException {
		return scheduler.runTaskAsynchronously(plugin, task);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will run asynchronously.
	 *
	 * @param task the task to be run
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTaskAsync(@NonNull Consumer<? super BukkitTask> task) throws IllegalArgumentException {
		scheduler.runTaskAsynchronously(plugin, task);
	}
	
	/**
	 * @param task the task to be run
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskAsync(@NonNull BukkitRunnable task) throws IllegalArgumentException {
		return task.runTaskAsynchronously(plugin);
	}
	
	/**
	 * Returns a task that will run after the specified number of server
	 * ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskLater(@NonNull Runnable task, long delay) throws IllegalArgumentException {
		return scheduler.runTaskLater(plugin, task, delay);
	}
	
	/**
	 * Returns a task that will run after the specified number of server
	 * ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTaskLater(@NonNull Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {
		scheduler.runTaskLater(plugin, task, delay);
	}
	
	/**
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskLater(@NonNull BukkitRunnable task, long delay) throws IllegalArgumentException {
		return task.runTaskLater(plugin, delay);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will run asynchronously after the specified number
	 * of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskLaterAsync(@NonNull Runnable task, long delay) throws IllegalArgumentException {
		return scheduler.runTaskLaterAsynchronously(plugin, task, delay);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will run asynchronously after the specified number
	 * of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTaskLaterAsync(@NonNull Consumer<? super BukkitTask> task, long delay) throws IllegalArgumentException {
		scheduler.runTaskLaterAsynchronously(plugin, task, delay);
	}
	
	/**
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskLaterAsync(@NonNull BukkitRunnable task, long delay) throws IllegalArgumentException {
		return task.runTaskLaterAsynchronously(plugin, delay);
	}
	
	/**
	 * Returns a task that will repeatedly run until cancelled, starting after
	 * the specified number of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskTimer(@NonNull Runnable task, long delay, long period) throws IllegalArgumentException {
		return scheduler.runTaskTimer(plugin, task, delay, period);
	}
	
	/**
	 * Returns a task that will repeatedly run until cancelled, starting after
	 * the specified number of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @param period the ticks to wait between runs
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTaskTimer(@NonNull Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {
		scheduler.runTaskTimer(plugin, task, delay, period);
	}
	
	/**
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskTimer(@NonNull BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
		return task.runTaskTimer(plugin, delay, period);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will repeatedly run asynchronously until cancelled,
	 * starting after the specified number of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task for the first
	 * time
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskTimerAsync(@NonNull Runnable task, long delay, long period) throws IllegalArgumentException {
		return scheduler.runTaskTimerAsynchronously(plugin, task, delay, period);
	}
	
	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit.</b> <b>Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <br>
	 * Returns a task that will repeatedly run asynchronously until cancelled,
	 * starting after the specified number of server ticks.
	 *
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task for the first
	 * time
	 * @param period the ticks to wait between runs
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public void runTaskTimerAsync(@NonNull Consumer<? super BukkitTask> task, long delay, long period) throws IllegalArgumentException {
		scheduler.runTaskTimerAsynchronously(plugin, task, delay, period);
	}
	
	/**
	 * @param task the task to be run
	 * @param delay the ticks to wait before running the task for the first
	 * time
	 * @param period the ticks to wait between runs
	 * @return a BukkitTask that contains the id number
	 * @throws IllegalArgumentException if plugin is null
	 * @throws IllegalArgumentException if task is null
	 */
	
	public @NonNull BukkitTask runTaskTimerAsync(@NonNull BukkitRunnable task, long delay, long period) throws IllegalArgumentException {
		return task.runTaskTimerAsynchronously(plugin, delay, period);
	}
	
	/**
	 * Returns an executor that will run tasks on the next server tick.
	 *
	 * @return an executor associated with the given plugin
	 */
	
	public @NonNull Executor getMainThreadExecutor() {
		return scheduler.getMainThreadExecutor(plugin);
	}
}
