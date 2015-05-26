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

package com.base.engine.components;

import com.base.engine.core.math.Attenuation;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Shader;

public class SpotLight extends PointLight {
	private float m_cutoff;

	public SpotLight(final Vector3f color, final float intensity, final Attenuation attenuation, final float cutoff) {
		super(color, intensity, attenuation);
		m_cutoff = cutoff;

		SetShader(new Shader("forward-spot"));
	}

	public Vector3f GetDirection() {
		return GetTransform().GetTransformedRot().GetForward();
	}

	public float GetCutoff() {
		return m_cutoff;
	}

	public void SetCutoff(final float cutoff) {
		m_cutoff = cutoff;
	}
}
