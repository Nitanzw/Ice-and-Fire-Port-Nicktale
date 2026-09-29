package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import net.minecraft.core.BlockPos;

import java.util.Objects;
import java.util.Set;

/** Client-side sink for pathfinding debug data received in common payloads. */
public final class PathfindingDebugSync {
    private static volatile ClientHandler clientHandler;

    private PathfindingDebugSync() {
    }

    /** Registers the client renderer's handler without linking common payload code to client classes. */
    public static void registerClientHandler(ClientHandler handler) {
        clientHandler = Objects.requireNonNull(handler, "handler");
    }

    static void syncPath(Set<MNode> visited, Set<MNode> notVisited, Set<MNode> path) {
        ClientHandler handler = clientHandler;
        if (handler != null) {
            handler.onPathSync(visited, notVisited, path);
        }
    }

    static void syncReached(Set<BlockPos> reached) {
        ClientHandler handler = clientHandler;
        if (handler != null) {
            handler.onPathReached(reached);
        }
    }

    public interface ClientHandler {
        void onPathSync(Set<MNode> visited, Set<MNode> notVisited, Set<MNode> path);

        void onPathReached(Set<BlockPos> reached);
    }
}
