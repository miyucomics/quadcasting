package miyucomics.quadcasting;

import at.petrak.hexcasting.api.casting.math.HexCoord;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

// because certain useful fields have been kept from us,
// we have to resort to reflection nonsense. I have relegated it
// to this class so I never need to think about it
public class DrawStateAccessor {
	private static final Class<?> JUST_STARTED;
	private static final Class<?> DRAWING;
	private static final Method GET_START;
	private static final Method GET_CURRENT;
	private static final Field DRAW_STATE;

	static {
		try {
			JUST_STARTED = Class.forName("at.petrak.hexcasting.client.gui.GuiSpellcasting$PatternDrawState$JustStarted");
			DRAWING = Class.forName("at.petrak.hexcasting.client.gui.GuiSpellcasting$PatternDrawState$Drawing");
			GET_START = JUST_STARTED.getMethod("getStart");
			GET_CURRENT = DRAWING.getMethod("getCurrent");
			DRAW_STATE = GuiSpellcasting.class.getDeclaredField("drawState");
			DRAW_STATE.setAccessible(true);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Optional<HexCoord> getAnchor(GuiSpellcasting gui) {
		try {
			Object state = DRAW_STATE.get(gui);
			if (JUST_STARTED.isInstance(state))
				return Optional.of((HexCoord) GET_START.invoke(state));
			if (DRAWING.isInstance(state))
				return Optional.of((HexCoord) GET_CURRENT.invoke(state));
		} catch (Exception ignored) {}
		return Optional.empty();
	}
}