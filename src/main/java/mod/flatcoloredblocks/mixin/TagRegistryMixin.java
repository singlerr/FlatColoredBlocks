package mod.flatcoloredblocks.mixin;

import net.minecraft.tags.ITag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.tags.TagRegistry$NamedTag")
public abstract class TagRegistryMixin<T> {

    @Shadow
    protected abstract ITag<T> getTag();

    @Inject(method = "contains", at = @At(value = "HEAD"), cancellable = true)
    private void returnFalseOnError(T element, CallbackInfoReturnable<Boolean> cir) {
        try {
            getTag();
        } catch (IllegalStateException ex) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
