package mod.flatcoloredblocks.network;

import java.io.IOException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

/**
 * Base Packet to be implemented.
 */
@SuppressWarnings("rawtypes")
public abstract class ModPacket implements IPacket {

    ServerPlayerEntity serverEntity = null;

    public void server(final ServerPlayerEntity playerEntity) {
        throw new RuntimeException(getClass().getName() + " is not a server packet.");
    }

    public void client() {
        throw new RuntimeException(getClass().getName() + " is not a client packet.");
    }

    public abstract void getPayload(PacketBuffer buffer);

    public abstract void readPayload(PacketBuffer buffer);

    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        readPacketData(buf);
    }

    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        getPayload(buf);
    }

    @Override
    public void processPacket(final INetHandler handler) {
        if (serverEntity == null) {
            client();
        } else {
            server(serverEntity);
        }
    }
}
