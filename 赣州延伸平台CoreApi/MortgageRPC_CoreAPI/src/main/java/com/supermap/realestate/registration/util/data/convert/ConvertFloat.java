package com.supermap.realestate.registration.util.data.convert;

import java.util.Date;

public class ConvertFloat {
	public static float toFloat(String value) {
		return Float.parseFloat(value);
	}

	public static float toFloat(Date value) {
		return (float) value.getTime();
	}

	public static float toFloat(char value) {
		return value;
	}

	public static float toFloat(short value) {
		return value;
	}

	public static float toFloat(boolean value) {
		if (value) {
			return 1.0F;
		}
		return 0.0F;
	}

	public static float toFloat(float value) {
		return value;
	}

	public static float toFloat(double value) {
		return (float) value;
	}

	public static float toFloat(byte value) {
		return value;
	}

	public static float toFloat(int value) {
		return value;
	}

	public static float toFloat(long value) {
		return (float) value;
	}
}
