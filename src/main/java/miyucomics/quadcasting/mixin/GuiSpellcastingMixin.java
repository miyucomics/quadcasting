package miyucomics.quadcasting.mixin;

import at.petrak.hexcasting.api.casting.math.HexCoord;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.quadcasting.DrawStateAccessor;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

@Mixin(value = GuiSpellcasting.class, remap = false)
public class GuiSpellcastingMixin {
	@Unique private static final double TWO_PI = Math.PI * 2;
	@Unique private static final double[][] SQUARE_TO_HEX = {
		{ 0, 0 },
		{ Math.PI / 2, Math.PI / 3 },
		{ 3 * Math.PI / 4, 2 * Math.PI / 3 },
		{ Math.PI, Math.PI },
		{ 3 * Math.PI / 2, 4 * Math.PI / 3 },
		{ 7 * Math.PI / 4, 5 * Math.PI / 3 },
	};

	@WrapOperation(method = "drawMove", at = @At(value = "INVOKE", target = "Ljava/lang/Math;atan2(DD)D"))
	private double modifySnappingCalculation(double y, double x, Operation<Double> original) {
		double angle = (original.call(y, x)  % TWO_PI + TWO_PI) % TWO_PI;
		return Arrays.stream(SQUARE_TO_HEX)
				.min(Comparator.comparingDouble(entry -> {
					double diff = Math.abs(angle - entry[0]);
					return Math.min(diff, TWO_PI - diff); // handle the wrap-around case
				}))
				.map(entry -> entry[1])
				.orElseThrow();
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/client/render/RenderLib;drawSpot(Lorg/joml/Matrix4f;Lnet/minecraft/util/math/Vec2f;FFFFF)V"))
	private void dontDrawInaccessibleDots(Matrix4f matrix, Vec2f coordinates, float radius, float red, float green, float blue, float alpha, Operation<Void> original) {
		GuiSpellcasting screen = (GuiSpellcasting)(Object) this;
		Optional<HexCoord> centerPoint = DrawStateAccessor.getAnchor(screen);

		if (centerPoint.isPresent()) {
			HexCoord coord = screen.pxToCoord(coordinates);
			int dq = coord.getQ() - centerPoint.get().getQ();
			int dr = coord.getR() - centerPoint.get().getR();
			if (dq == dr && Math.abs(dr) == 1)
				return;
		}

		original.call(matrix, coordinates, radius, red, green, blue, alpha);
	}
}