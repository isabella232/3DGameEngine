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

import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Window;

public class FreeLook extends GameComponent {
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);

	private boolean mouseLocked = false;
	private final float sensitivity;
	private final int unlockMouseKey;
	private final boolean fixedAxis;

	/**
	 * Fixed axsis free look constructor.
	 *
	 * @param sensitivity
	 *            - Rotational speed of the camera
	 */
	public FreeLook(final float sensitivity) {
		this(sensitivity, true);
	}

	/**
	 * Full gimble free look constructor.
	 *
	 * @param sensitivity
	 *            - Rotational speed of the camera
	 * @param fixedAxis
	 *            - If set to false, use full gimble rotation. If true, use
	 *            fixed axis rotation.
	 */
	public FreeLook(final float sensitivity, final boolean fixedAxis) {
		this(sensitivity, Input.KEY_ESCAPE, fixedAxis);
	}

	/**
	 * Fixed axis free look constructor.
	 *
	 * @param sensitivity
	 *            - Rotational speed of the camera
	 * @param unlockMouseKey
	 *            - Key to release mouse from mouse capture. See {@link Input}
	 *            for values.
	 */
	public FreeLook(final float sensitivity, final int unlockMouseKey) {
		this(sensitivity, unlockMouseKey, true);
	}

	/**
	 * Full gimble free look constructor.
	 *
	 * @param sensitivity
	 *            - Rotational speed of the camera
	 * @param unlockMouseKey
	 *            - Key to release mouse from mouse capture. See {@link Input}
	 *            for values.
	 * @param fixedAxis
	 *            - If set to false, use full gimble rotation. If true, use
	 *            fixed axis rotation.
	 */
	public FreeLook(final float sensitivity, final int unlockMouseKey, final boolean fixedAxis) {
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
		this.fixedAxis = fixedAxis;
	}

	@Override
	public void input(final float delta) {
		final Vector2f centerPosition = Window.getCenterPosition();

		if (Input.getKey(unlockMouseKey)) {
			Input.setCursor(true);
			mouseLocked = false;
		}
		if (Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}

		if (mouseLocked) {
			final Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			final boolean rotY = deltaPos.getX() != 0;
			final boolean rotX = deltaPos.getY() != 0;

			if (rotY) {
				getTransform().rotate(fixedAxis ? FreeLook.Y_AXIS : getTransform().getRot().getUp(), (float) Math.toRadians(deltaPos.getX() * sensitivity));
			}
			if (rotX) {
				getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity)); // TODO:
																																		// Check
																																		// for
																																		// fixed
																																		// axis
																																		// lock.
			}

			if (rotY || rotX) {
				Input.setMousePosition(centerPosition);
			}
		}
	}
}
