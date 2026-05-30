package ontey.api.util;

import lombok.NonNull;
import ontey.api.check.Checker;
import org.jetbrains.annotations.Range;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for formatting durations into a String.
 */

public class DurationFormatter {
	
	/**
	 * Formats the given duration into a human-readable format.
	 * Days, hours, minutes, seconds and milliseconds are used and represented by d/h/m/s/ms.
	 * If one of these values (like days) is {@code 0}, it is left out.
	 * Joins the parts with a space.
	 *
	 * @param duration The non-negative duration to format
	 */
	
	public static String formatHumanReadable(@NonNull Duration duration) {
		if(duration.isNegative())
			throw new IllegalArgumentException("Duration cannot be negative");
		
		if(duration.isZero())
			return "0s";
		
		return formatHumanReadable(
		  duration.toDaysPart(),
		  duration.toHoursPart(),
		  duration.toMinutesPart(),
		  duration.toSecondsPart(),
		  duration.toMillisPart()
		);
	}
	
	/**
	 * Days, hours, minutes, seconds and milliseconds are used and represented by d/h/m/s/ms.
	 * If one of these values (like days) is {@code 0}, it is left out.
	 * Joins the parts with a space.
	 */
	
	public static String formatHumanReadable(@Range(from = 0, to = Long.MAX_VALUE) long days,
	                                         @Range(from = 0, to = Integer.MAX_VALUE) int hours,
	                                         @Range(from = 0, to = Integer.MAX_VALUE) int minutes,
	                                         @Range(from = 0, to = Integer.MAX_VALUE) int seconds,
	                                         @Range(from = 0, to = Integer.MAX_VALUE) int millis) {
		Checker.checkBounds(0, Integer.MAX_VALUE, hours);
		Checker.checkBounds(0, Integer.MAX_VALUE, minutes);
		Checker.checkBounds(0, Integer.MAX_VALUE, seconds);
		Checker.checkBounds(0, Integer.MAX_VALUE, millis);
		
		List<String> parts = new ArrayList<>();
		
		if(days > 0) parts.add(days + "d");
		if(hours > 0) parts.add(hours + "h");
		if(minutes > 0) parts.add(minutes + "m");
		if(seconds > 0) parts.add(seconds + "s");
		if(millis > 0) parts.add(millis + "ms");
		
		return String.join(" ", parts);
	}
}