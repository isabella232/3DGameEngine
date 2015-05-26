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

package com.base.engine.rendering;

import java.util.HashMap;

import com.base.engine.rendering.resourceManagement.MappedValues;

public class Material extends MappedValues {
	private final HashMap<String, Texture> m_textureHashMap;

	public Material(final Texture diffuse, final float specularIntensity, final float specularPower, final Texture normal, final Texture dispMap, final float dispMapScale, final float dispMapOffset) {
		super();
		m_textureHashMap = new HashMap<String, Texture>();
		AddTexture("diffuse", diffuse);
		AddFloat("specularIntensity", specularIntensity);
		AddFloat("specularPower", specularPower);
		AddTexture("normalMap", normal);
		AddTexture("dispMap", dispMap);

		final float baseBias = dispMapScale / 2.0f;
		AddFloat("dispMapScale", dispMapScale);
		AddFloat("dispMapBias", -baseBias + baseBias * dispMapOffset);
	}

	public void AddTexture(final String name, final Texture texture) {
		m_textureHashMap.put(name, texture);
	}

	public Texture GetTexture(final String name) {
		final Texture result = m_textureHashMap.get(name);
		if (result != null) {
			return result;
		}

		return new Texture("test.png");
	}
}
