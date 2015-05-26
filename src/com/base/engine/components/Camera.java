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

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;

public class Camera extends GameComponent {
	private final Matrix4f m_projection;

	public Camera(final Matrix4f projection) {
		m_projection = projection;
	}

	public Matrix4f GetViewProjection() {
		final Matrix4f cameraRotation = GetTransform().GetTransformedRot().Conjugate().ToRotationMatrix();
		final Vector3f cameraPos = GetTransform().GetTransformedPos().Mul(-1);

		final Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());

		return m_projection.Mul(cameraRotation.Mul(cameraTranslation));
	}

	@Override
	public void AddToEngine(final CoreEngine engine) {
		engine.GetRenderingEngine().AddCamera(this);
	}
}
