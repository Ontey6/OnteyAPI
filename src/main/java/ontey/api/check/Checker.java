package ontey.api.check;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class used to check various conditions
 */

public class Checker {
	
	private Checker() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise
	 * throws an {@link IllegalArgumentException} with the
	 * {@code errorMessage} String which is formatted using
	 * {@link #lenientFormat(String, Object...)}.
	 *
	 * @param expression The expression that is required to be true
	 * @param errorMessage The error message sent if the {@code expression} is false
	 * @param arguments The arguments used to format the {@code errorMessage}
	 */
	
	public static void checkArgument(boolean expression, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) throws IllegalArgumentException {
		if(!expression)
			throw new IllegalArgumentException(lenientFormat(errorMessage, arguments));
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise throws an
	 * {@link IllegalArgumentException} with the {@code errorMessage} String.
	 *
	 * @param expression The expression that is required to be true
	 * @param errorMessage The error message sent if the {@code expression} is false
	 */
	
	public static void checkArgument(boolean expression, @NonNull String errorMessage) throws IllegalArgumentException {
		if(!expression)
			throw new IllegalArgumentException(errorMessage);
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise throws an {@link IllegalArgumentException}.
	 *
	 * @param expression The expression that is required to be true
	 */
	
	public static void checkArgument(boolean expression) throws IllegalArgumentException {
		if(!expression)
			throw new IllegalArgumentException();
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise
	 * throws an {@link IllegalStateException} with the
	 * {@code errorMessage} String which is formatted using
	 * {@link #lenientFormat(String, Object...)}.
	 *
	 * @param expression The expression that is required to be true
	 * @param errorMessage The error message sent if the {@code expression} is false
	 * @param arguments The arguments used to format the {@code errorMessage}
	 */
	
	public static void checkState(boolean expression, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) throws IllegalStateException {
		if(!expression)
			throw new IllegalStateException(lenientFormat(errorMessage, arguments));
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise throws an
	 * {@link IllegalStateException} with the {@code errorMessage} String.
	 *
	 * @param expression The expression that is required to be true
	 * @param errorMessage The error message sent if the {@code expression} is false
	 */
	
	public static void checkState(boolean expression, @NonNull String errorMessage) throws IllegalStateException {
		if(!expression)
			throw new IllegalStateException(errorMessage);
	}
	
	/**
	 * Requires {@code expression} to be true, otherwise throws an {@link IllegalStateException}.
	 *
	 * @param expression The expression that is required to be true
	 */
	
	public static void checkState(boolean expression) throws IllegalStateException {
		if(!expression)
			throw new IllegalStateException();
	}
	
	/**
	 * Requires the input to be any number from {@code min} to {@code max}.
	 * Otherwise, throws an {@link IndexOutOfBoundsException} with
	 * the {@code errorMessage} String which is formatted using
	 * {@link #lenientFormat(String, Object...)}.
	 *
	 * @param min The minimum
	 * @param max The maximum
	 * @param input The {@code int} to be tested
	 * @param errorMessage The error message sent if the {@code expression} is false
	 * @param arguments The arguments used to format the {@code errorMessage}
	 */
	
	public static void checkBounds(int min, int max, int input, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) throws IndexOutOfBoundsException {
		checkArgument(min <= max, "Argument max has to be higher equal min!");
		
		if(input < min || input > max)
			throw new IndexOutOfBoundsException(lenientFormat(errorMessage, arguments));
	}
	
	/**
	 * Requires the input to be any number from {@code min} to {@code max}.
	 * Otherwise, throws an {@link IndexOutOfBoundsException} with
	 * the {@code errorMessage} String.
	 *
	 * @param min The minimum
	 * @param max The maximum
	 * @param input The {@code int} to be tested
	 * @param errorMessage The error message sent if the {@code expression} is false
	 */
	
	public static void checkBounds(int min, int max, int input, @NonNull String errorMessage) throws IndexOutOfBoundsException {
		checkArgument(min <= max, "Argument max has to be higher equal min!");
		
		if(input < min || input > max)
			throw new IndexOutOfBoundsException(errorMessage);
	}
	
	/**
	 * Requires the input to be any number from {@code min} to {@code max}.
	 * Otherwise, throws an {@link IndexOutOfBoundsException}.
	 *
	 * @param min The minimum
	 * @param max The maximum
	 * @param input The {@code int} to be tested
	 */
	
	public static void checkBounds(int min, int max, int input) throws IndexOutOfBoundsException {
		checkArgument(min <= max, "Argument max has to be higher equal min!");
		
		if(input < min || input > max)
			throw new IndexOutOfBoundsException();
	}
	
	/**
	 * Asserts that {@code obj} is not null.
	 * Otherwise, throws an {@link NullPointerException} with
	 * the {@code errorMessage} String which is formatted using
	 * {@link #lenientFormat(String, Object...)}.
	 *
	 * @param obj The object tested for nullity
	 * @param errorMessage The error message sent if the {@code expression} is false
	 * @param arguments The arguments used to format the {@code errorMessage}
	 */
	
	public static void checkNonNull(@Nullable Object obj, @NonNull String errorMessage, @NonNull Object @NonNull ... arguments) throws NullPointerException {
		if(obj == null)
			throw new NullPointerException(lenientFormat(errorMessage, arguments));
	}
	
	/**
	 * Asserts that {@code obj} is not null.
	 * Otherwise, throws an {@link NullPointerException} with
	 * the {@code errorMessage} String.
	 *
	 * @param obj The object tested for nullity
	 * @param errorMessage The error message sent if the {@code expression} is false
	 */
	
	public static void checkNonNull(@Nullable Object obj, @NonNull String errorMessage) throws NullPointerException {
		if(obj == null)
			throw new NullPointerException(errorMessage);
	}
	
	/**
	 * Asserts that {@code obj} is not null.
	 * Otherwise, throws an {@link NullPointerException}.
	 *
	 * @param obj The object tested for nullity
	 */
	
	public static void checkNonNull(@Nullable Object obj) throws NullPointerException {
		if(obj == null)
			throw new NullPointerException();
	}
	
	/**
	 * @apiNote Will be moved to an appropriate StringUtils or similar class.
	 */
	
	/*
	 * Copyright (C) 2010 The Guava Authors
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
	 * in compliance with the License. You may obtain a copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software distributed under the License
	 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
	 * or implied. See the License for the specific language governing permissions and limitations under
	 * the License.
	 */
	protected static String lenientFormat(@NonNull String template, @Nullable Object @NonNull ... args) {
		// start substituting the arguments into the '%s' placeholders
		StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
		int templateStart = 0;
		int i = 0;
		while(i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if(placeholderStart == -1)
				break;
			
			builder.append(template, templateStart, placeholderStart);
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template, templateStart, template.length());
		
		// if we run out of placeholders, append the extra args in square braces
		if(i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while(i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}
		
		return builder.toString();
	}
}
