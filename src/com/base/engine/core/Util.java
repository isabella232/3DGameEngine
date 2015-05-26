/*
 * Copyright (C) 2014 Benny Bobaganoosh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vertex;

public class Util {
	public static FloatBuffer CreateFloatBuffer(final int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	public static IntBuffer CreateIntBuffer(final int size) {
		return BufferUtils.createIntBuffer(size);
	}

	public static ByteBuffer CreateByteBuffer(final int size) {
		return BufferUtils.createByteBuffer(size);
	}

	public static IntBuffer CreateFlippedBuffer(final int... values) {
		final IntBuffer buffer = Util.CreateIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();

		return buffer;
	}

	public static FloatBuffer CreateFlippedBuffer(final Vertex[] vertices) {
		final FloatBuffer buffer = Util.CreateFloatBuffer(vertices.length * Vertex.SIZE);

		for (final Vertex vertice : vertices) {
			buffer.put(vertice.GetPos().GetX());
			buffer.put(vertice.GetPos().GetY());
			buffer.put(vertice.GetPos().GetZ());
			buffer.put(vertice.GetTexCoord().GetX());
			buffer.put(vertice.GetTexCoord().GetY());
			buffer.put(vertice.GetNormal().GetX());
			buffer.put(vertice.GetNormal().GetY());
			buffer.put(vertice.GetNormal().GetZ());
			buffer.put(vertice.GetTangent().GetX());
			buffer.put(vertice.GetTangent().GetY());
			buffer.put(vertice.GetTangent().GetZ());
		}

		buffer.flip();

		return buffer;
	}

	public static FloatBuffer CreateFlippedBuffer(final Matrix4f value) {
		final FloatBuffer buffer = Util.CreateFloatBuffer(4 * 4);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				buffer.put(value.Get(i, j));
			}
		}

		buffer.flip();

		return buffer;
	}

	/**
	 * Remove all empty strings from an array.
	 *
	 * @param data
	 *            - Array of {@link String}s to remove from.
	 * @return An array containing every non-empty string from <code>data</code>
	 */
	public static String[] RemoveEmptyStrings(final String... data) {
		final ArrayList<String> result = new ArrayList<String>();

		for (final String filtering : data) {
			if (filtering != null && !filtering.isEmpty()) {
				result.add(filtering);
			}
		}

		return result.toArray(new String[0]);
	}

	/**
	 * Convert an object representation of an Integer array to its primitive
	 * form.
	 *
	 * <code>
	 * Integer[] data = ...;
	 * int[] primativeData = Util.ToIntArray(data);
	 * </code>
	 * 
	 * @param data
	 *            - An array of integer objects
	 * @return An array with primitive representations of <code>data</code>
	 */
	public static int[] ToIntArray(final Integer... data) {
		final int[] result = new int[data.length];

		for (int i = 0; i < data.length; i++) {
			final Integer filtering = data[i];

			if (filtering == null) {
				continue;
			}

			result[i] = filtering;
		}

		return result;
	}
}
