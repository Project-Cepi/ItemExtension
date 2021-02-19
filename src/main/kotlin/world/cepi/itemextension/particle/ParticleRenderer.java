package world.cepi.itemextension.particle;

import net.minestom.server.MinecraftServer;
import net.minestom.server.Viewable;
import net.minestom.server.bossbar.BossBar;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.time.UpdateOption;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an object that can generate particles.
 * It can contain any number of properties,
 * from a Player's refrence to link positions,
 * to color and other properties.
 */
public abstract class ParticleRenderer implements Viewable {

    private final AtomicInteger lifespan = new AtomicInteger();
    private final int totalLifespan;
    private final UpdateOption renderInterval;
    private Task schedule;

    private final Set<Player> viewers = new CopyOnWriteArraySet<>();
    private final Set<Player> unmodifiableViewers = Collections.unmodifiableSet(viewers);

    /**
     * Creates a new ParticleRenderer with the set lifespan and render times
     *
     * @param lifespan How long this renderer will last.
     * @param renderInterval The time it takes before the next rendering session takes place.
     */
    public ParticleRenderer(int lifespan, UpdateOption renderInterval) {
        this.totalLifespan = lifespan;
        this.renderInterval = renderInterval;
    }

    /**
     * Generates all packets per the render cycle. Stored in a list
     *
     * @return The list of built ParticlePackets.
     */
    public abstract List<ParticlePacket> generatePackets();

    /**
     * Starts rendering this particle to the viewers.
     */
    public void render() {
        schedule = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (lifespan.incrementAndGet() >= totalLifespan) {
                schedule.cancel();
                return;
            }

            List<ParticlePacket> particles = generatePackets();

            for (ParticlePacket packet : particles) {
                PacketUtils.sendGroupedPacket(getViewers(), packet);
            }


        }).repeat(renderInterval.getValue(), renderInterval.getTimeUnit()).schedule();
    }

    /**
     * Stops rendering this ParticleRenderer completely.
     */
    public void stop() {
        schedule.cancel();
    }

    @Override
    public synchronized boolean addViewer(@NotNull Player player) {
        return viewers.add(player);
    }

    @Override
    public synchronized boolean removeViewer(@NotNull Player player) {
        return this.viewers.remove(player);
    }

    @NotNull
    @Override
    public Set<Player> getViewers() {
        return unmodifiableViewers;
    }

}
