package miyucomics.quadcasting.mixin;

import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import miyucomics.quadcasting.QuadcastingMain;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = GuiSpellcasting.class, remap = false)
public class GuiSpellcastingMixin {
	@Inject(method = "drawMove", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/math/HexCoord;plus(Lat/petrak/hexcasting/api/casting/math/HexDir;)Lat/petrak/hexcasting/api/casting/math/HexCoord;"))
	private void modifySnappingCalculation(double mxOut, double myOut, CallbackInfoReturnable<Boolean> cir, @Local(name = "newdir") LocalRef<HexDir> newDirection, @Local(name = "delta") Vec2f delta) {
		double angle = Math.atan2(-delta.y, delta.x);
		if (angle < 0)
			angle += 2 * Math.PI;
		newDirection.set(findClosestAngle(angle));
	}

	@Unique
	private static HexDir findClosestAngle(double angle) {
		Map.Entry<Double, HexDir> floor = QuadcastingMain.map.floorEntry(angle);
		Map.Entry<Double, HexDir> ceiling = QuadcastingMain.map.ceilingEntry(angle);
		if (floor == null) return ceiling.getValue();
		if (ceiling == null) return floor.getValue();

		double distanceFromFloor = Math.abs(angle - floor.getKey());
		double distanceFromCeiling = Math.abs(angle - ceiling.getKey());

		return distanceFromFloor <= distanceFromCeiling ? floor.getValue() : ceiling.getValue();
	}
}