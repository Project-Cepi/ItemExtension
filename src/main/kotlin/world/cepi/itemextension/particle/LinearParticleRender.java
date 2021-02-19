package world.cepi.itemextension.particle;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.utils.time.UpdateOption;

import java.util.List;

/**
 * Represents a particle renderer that renders from position A to positin b.
 */
class LinearParticleRenderer extends ParticleRenderer {

    private final int density;

    public LinearParticleRenderer(ParticlePacket basePacket, int lifespan, int density, UpdateOption generationInterval) {

        super(lifespan, generationInterval);
        this.density = density;
    }

    @Override
    public List<ParticlePacket> generatePackets() {
        return null;
    }
}
