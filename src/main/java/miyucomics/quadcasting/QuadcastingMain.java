package miyucomics.quadcasting;

import at.petrak.hexcasting.api.casting.math.HexDir;
import net.fabricmc.api.ModInitializer;

import java.util.NavigableMap;
import java.util.TreeMap;

public class QuadcastingMain implements ModInitializer {
	public static NavigableMap<Double, HexDir> map = new TreeMap<>();

	@Override
	public void onInitialize() {
		map.put(0.0, HexDir.EAST);
		map.put(Math.PI / 4, HexDir.NORTH_EAST);
		map.put(Math.PI / 2, HexDir.NORTH_WEST);
		map.put(Math.PI, HexDir.WEST);
		map.put(5 * Math.PI / 4, HexDir.SOUTH_WEST);
		map.put(3 * Math.PI / 2, HexDir.SOUTH_EAST);
		map.put(2 * Math.PI, HexDir.EAST);
	}
}