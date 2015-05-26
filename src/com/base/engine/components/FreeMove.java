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
import com.base.engine.core.math.Vector3f;

public class FreeMove extends GameComponent {
	private final float m_speed;
	private final int m_forwardKey;
	private final int m_backKey;
	private final int m_leftKey;
	private final int m_rightKey;
	private final int m_rotLeftKey;
	private final int m_rotRightKey;

	private final float m_sensitivity; // Used for affecting roll speed.

	/**
	 * {@link FreeMove} constructor for fixed axis free look. See
	 * {@link FreeLook} for more information
	 * 
	 * @param speed
	 *            - The movement speed.
	 */
	public FreeMove(final float speed) {
		this(speed, 0f);
	}

	/**
	 * {@link FreeMove} constructor for full gimble rotation.
	 *
	 * @param speed
	 *            - The movement speed
	 * @param rotationSensitivity
	 *            - The rotational sensitivity of the camera
	 */
	public FreeMove(final float speed, final float rotationSensitivity) {
		this(speed, rotationSensitivity, Input.KEY_W, Input.KEY_S, Input.KEY_A, Input.KEY_D, Input.KEY_Q, Input.KEY_E);
	}

	/**
	 * {@link FreeMove} constructor for fixed axis free look that does NOT allow
	 * rotation keys to be set. Use the FreeMove(float speed, int forwardKey,
	 * int backKey, int leftKey, int rightKey, int rotLeft, int rotRight); if
	 * you would like to be able to rotate the camera.
	 *
	 * See {@link FreeLook} for more information.
	 *
	 * @param speed
	 *            - The movement speed.
	 * @param forwardKey
	 *            - Key used to move forward. See {@link Input} for values.
	 * @param backKey
	 *            - Key used to move back. See {@link Input} for values.
	 * @param leftKey
	 *            - Key used to strafe left. See {@link Input} for values.
	 * @param rightKey
	 *            - Key used to strafe right. See {@link Input} for values.
	 */
	public FreeMove(final float speed, final int forwardKey, final int backKey, final int leftKey, final int rightKey) {
		this(speed, forwardKey, backKey, leftKey, rightKey, 0, 0);
	}

	/**
	 * {@link FreeMove} constructor for fixed axis free look that allows
	 * rotation keys to be set. See {@link FreeLook} for more information.
	 *
	 * @param speed
	 *            - The movement speed.
	 * @param forwardKey
	 *            - Key used to move forward. See {@link Input} for values.
	 * @param backKey
	 *            - Key used to move back. See {@link Input} for values.
	 * @param leftKey
	 *            - Key used to strafe left. See {@link Input} for values.
	 * @param rightKey
	 *            - Key used to strafe right. See {@link Input} for values.
	 * @param rotLeft
	 *            - Key used to rotate left. See {@link Input} for values.
	 * @param rotRight
	 *            - Key used to rotate right. See {@link Input} for values.
	 */
	public FreeMove(final float speed, final int forwardKey, final int backKey, final int leftKey, final int rightKey, final int rotLeft, final int rotRight) {
		this(speed, 0f, forwardKey, backKey, leftKey, rightKey, rotLeft, rotRight);
	}

	/**
	 * {@link FreeMove} constructor for full gimble free look that allows
	 * rotation keys to be set. See {@link FreeLook} for more information.
	 *
	 * @param speed
	 *            - The movement speed.
	 * @param rotationSensitivity
	 *            - The rotational sensitivity of the camera
	 * @param forwardKey
	 *            - Key used to move forward. See {@link Input} for values.
	 * @param backKey
	 *            - Key used to move back. See {@link Input} for values.
	 * @param leftKey
	 *            - Key used to strafe left. See {@link Input} for values.
	 * @param rightKey
	 *            - Key used to strafe right. See {@link Input} for values.
	 * @param rotLeft
	 *            - Key used to rotate left. See {@link Input} for values.
	 * @param rotRight
	 *            - Key used to rotate right. See {@link Input} for values.
	 */
	public FreeMove(final float speed, final float rotationSensitivity, final int forwardKey, final int backKey, final int leftKey, final int rightKey, final int rotLeft, final int rotRight) {
		m_speed = speed;
		m_forwardKey = forwardKey;
		m_backKey = backKey;
		m_leftKey = leftKey;
		m_rightKey = rightKey;
		m_rotLeftKey = rotLeft;
		m_rotRightKey = rotRight;
		m_sensitivity = rotationSensitivity;
	}

	@Override
	public void Input(final float delta) {
		final float movAmt = m_speed * delta;

		if (Input.GetKey(m_forwardKey)) {
			Move(GetTransform().GetRot().GetForward(), movAmt);
		}
		if (Input.GetKey(m_backKey)) {
			Move(GetTransform().GetRot().GetForward(), -movAmt);
		}
		if (Input.GetKey(m_leftKey)) {
			Move(GetTransform().GetRot().GetLeft(), movAmt);
		}
		if (Input.GetKey(m_rightKey)) {
			Move(GetTransform().GetRot().GetRight(), movAmt);
		}

		if (Input.GetKey(m_rotLeftKey)) {
			GetTransform().Rotate(GetTransform().GetRot().GetForward(), (float) Math.toRadians(movAmt * m_sensitivity));
		}
		if (Input.GetKey(m_rotRightKey)) {
			GetTransform().Rotate(GetTransform().GetRot().GetForward(), (float) Math.toRadians(-movAmt * m_sensitivity));
		}

	}

	private void Move(final Vector3f dir, final float amt) {
		GetTransform().SetPos(GetTransform().GetPos().Add(dir.Mul(amt)));
	}
}
