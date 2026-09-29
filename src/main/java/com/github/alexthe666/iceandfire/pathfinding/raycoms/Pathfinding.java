package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.neoforged.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;


/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor executor;

    private Pathfinding() {
        //Hides default constructor.
    }

    public static boolean isDebug() {
        return IafConfig.pathfindingDebug;
    }

    /**
     * Creates a new thread pool for pathfinding jobs
     *
     * @return the threadpool executor.
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, IafConfig.dragonPathfindingThreads, 10, TimeUnit.SECONDS, jobQueue, new IafThreadFactory());
        }
        return executor;
    }

    /**
     * Ice and Fire specific thread factory.
     */
    public static class IafThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        @Override
        public Thread newThread(final @NotNull Runnable runnable) throws RuntimeException {
            MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
            ClassLoader classLoader = server != null && server.getRunningThread() != null
                ? server.getRunningThread().getContextClassLoader() : Thread.currentThread().getContextClassLoader();
            final Thread thread = new Thread(runnable, "Ice and Fire Pathfinding Worker #" + (id++));
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            if (thread.getContextClassLoader() != classLoader) {
                IceAndFire.LOGGER.info("Corrected CCL of new Ice and Fire Pathfinding Thread, was: " + thread.getContextClassLoader().toString());
                thread.setContextClassLoader(classLoader);
            }
            thread.setUncaughtExceptionHandler((thread1, throwable) -> IceAndFire.LOGGER.error("Ice and Fire Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
