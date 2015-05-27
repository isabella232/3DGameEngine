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
	private final Matrix4f projection;

	public Camera(final Matrix4f projection) {
		this.projection = projection;
	}

	public Matrix4f getViewProjection() {
		final Matrix4f cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
		final Vector3f cameraPos = getTransform().getTransformedPos().mul(-1);

		final Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	@Override
	public void addToEngine(final CoreEngine engine) {
		engine.getRenderingEngine().addCamera(this);
	}
}
