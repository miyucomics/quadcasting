package miyucomics.quadcasting.mixin;

import at.petrak.hexcasting.api.casting.math.HexCoord;
import at.petrak.hexcasting.api.utils.HexUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HexUtils.class)
public class HexUtilsMixin {
	@WrapMethod(method = "coordToPx")
	private static Vec2f modifyCoordToPx(HexCoord coord, float size, Vec2f offset, Operation<Vec2f> original) {
		return new Vec2f(coord.getQ(), coord.getR()).multiply(size * 1.5f).add(offset);
	}

	@WrapMethod(method = "pxToCoord")
	private static HexCoord modifyPxToCoord(Vec2f px, float size, Vec2f offset, Operation<HexCoord> original) {
		Vec2f offsetted = px.add(offset.negate());
		int q = Math.round(offsetted.x / size / 1.5f);
		int r = Math.round(offsetted.y / size / 1.5f);
		return new HexCoord(q, r);
	}
}