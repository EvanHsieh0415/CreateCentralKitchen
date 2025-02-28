package plus.dragons.createcentralkitchen.foundation.mixin.common.create;

import com.simibubi.create.content.contraptions.components.actors.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementBehaviour;
import com.simibubi.create.content.contraptions.components.structureMovement.MovementContext;
import com.simibubi.create.foundation.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plus.dragons.createcentralkitchen.content.contraptions.components.actor.HarvesterMovementBehaviourExtension;

@Mixin(value = HarvesterMovementBehaviour.class, remap = false)
public abstract class HarvesterMovementBehaviorMixin implements MovementBehaviour {
    
    @Inject(method = "visitNewPosition", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/components/actors/HarvesterMovementBehaviour;isValidCrop(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), cancellable = true)
    private void cck$handleMovementBehaviorExtension(MovementContext context, BlockPos pos, CallbackInfo ci) {
        Level level = context.world;
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (HarvesterMovementBehaviourExtension.REGISTRY.containsKey(block)) {
            boolean replant = AllConfigs.SERVER.kinetics.harvesterReplants.get();
            boolean partial = AllConfigs.SERVER.kinetics.harvestPartiallyGrown.get();
            HarvesterMovementBehaviourExtension.REGISTRY.get(block)
                .harvest(((HarvesterMovementBehaviour)(Object)this), context, pos, state, replant, partial);
            ci.cancel();
        }
    }
    
}