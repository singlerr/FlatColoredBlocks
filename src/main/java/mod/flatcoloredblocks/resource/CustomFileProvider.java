package mod.flatcoloredblocks.resource;

import com.google.gson.JsonObject;
import mod.flatcoloredblocks.FlatColoredBlocks;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class CustomFileProvider extends ResourcePack {

    Map<String, byte[]> fakeFiles = new HashMap<String, byte[]>();
    String prefix = "assets/flatcoloredblocks/";

    public CustomFileProvider() {
        super(new File("internal_generator"));
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(
            ResourcePackType resourcePackType, String s, String s1, int i, Predicate<String> predicate) {
        return Collections.emptyList();
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type) {
        return Collections.singleton(FlatColoredBlocks.MODID);
    }

    @Override
    public void close() {
    }

    @Override
    protected InputStream getInputStream(String resourcePath) throws IOException {
        if (resourcePath.startsWith(prefix)) {
            byte[] value = fakeFiles.get(resourcePath.substring(prefix.length()));
            return new ByteArrayInputStream(fakeFiles.get(resourcePath.substring(prefix.length())));
        }

        throw new IOException("No such stream.");
    }

    @Override
    public <T> T getMetadata(IMetadataSectionSerializer<T> metaReader) {
        String name = metaReader.getSectionName();
        if (metaReader.getSectionName().equals("pack")) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", 5);
            object.addProperty("description", "runtime resource pack");
            return metaReader.deserialize(object);
        }
        System.out.println("'" + metaReader.getSectionName() + "' is an unsupported metadata key!");
        return metaReader.deserialize(new JsonObject());
    }

    @Override
    protected boolean resourceExists(String resourcePath) {
        if (resourcePath.startsWith(prefix)) {
            return fakeFiles.containsKey(resourcePath.substring(prefix.length()));
        }

        return false;
    }
}
