package mod.flatcoloredblocks.mixin;

import mod.flatcoloredblocks.client.ClientSide;
import mod.flatcoloredblocks.resource.CustomResourceInjector;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class ReloadableResourceManagerMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    public abstract void addResourcePack(IResourcePack resourcePack);

    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void registerARRPs(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs,
                               CallbackInfoReturnable<IAsyncReloader> cir) {
        System.out.println("Loading custom runtime resourcepacks....");
        ClientSide.instance.createResources();
        addResourcePack(CustomResourceInjector.generatedFiles);
    }
}
