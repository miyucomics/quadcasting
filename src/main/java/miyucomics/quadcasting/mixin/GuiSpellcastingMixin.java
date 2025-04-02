package miyucomics.quadcasting.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

@Mixin(value = GuiSpellcasting.class, remap = false)
public class GuiSpellcastingMixin {
	@Unique
	private static final double[][] ANGLE_REMAP = new double[][] {
		{0.0, 0.0},
		{0.5, 1.0 / 3.0},
		{0.75, 2.0 / 3.0},
		{1.0, 1.0},
		{1.5, 4.0 / 3.0},
		{1.75, 5.0 / 3.0}
	};

	@Redirect(method = "drawMove", at = @At(value = "INVOKE", target = "Ljava/lang/Math;atan2(DD)D"))
	private double modifyAngleCalculation(double y, double x) {
		double angle = Math.atan2(y, x);
		if (angle < 0)
			angle += 2 * PI;
		angle /= PI;

		double bestDiff = Double.MAX_VALUE;
		double snapped = angle;
		for (double[] pair : ANGLE_REMAP) {
			double diff = abs(angle - pair[0]);
			if (diff < bestDiff) {
				bestDiff = diff;
				snapped = pair[1];
			}
		}

		return snapped * PI;
	}
}