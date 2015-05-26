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

package com.base.engine.rendering.resourceManagement;

import java.util.HashMap;

import com.base.engine.core.math.Vector3f;

public abstract class MappedValues {
	private final HashMap<String, Vector3f> m_vector3fHashMap;
	private final HashMap<String, Float> m_floatHashMap;

	public MappedValues() {
		m_vector3fHashMap = new HashMap<String, Vector3f>();
		m_floatHashMap = new HashMap<String, Float>();
	}

	public void AddVector3f(final String name, final Vector3f vector3f) {
		m_vector3fHashMap.put(name, vector3f);
	}

	public void AddFloat(final String name, final float floatValue) {
		m_floatHashMap.put(name, floatValue);
	}

	public Vector3f GetVector3f(final String name) {
		final Vector3f result = m_vector3fHashMap.get(name);
		if (result != null) {
			return result;
		}

		return new Vector3f(0, 0, 0);
	}

	public float GetFloat(final String name) {
		final Float result = m_floatHashMap.get(name);
		if (result != null) {
			return result;
		}

		return 0;
	}
}
