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

package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class LookAtComponent extends GameComponent {
	private RenderingEngine renderingEngine;

	@Override
	public void update(final float delta) {
		if (renderingEngine != null) {
			final Quaternion newRot = getTransform().getLookAtRotation(renderingEngine.getMainCamera().getTransform().getTransformedPos(), new Vector3f(0, 1, 0));
			// GetTransform().GetRot().GetUp());

			getTransform().setRot(getTransform().getRot().nlerp(newRot, delta * 5.0f, true));
			// GetTransform().SetRot(GetTransform().GetRot().SLerp(newRot, delta
			// * 5.0f, true));
		}
	}

	@Override
	public void render(final Shader shader, final RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
}
