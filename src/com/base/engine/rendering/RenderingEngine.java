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

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.core.GameObject;
import com.base.engine.core.Transform;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.resourceManagement.MappedValues;

public class RenderingEngine extends MappedValues {
	private final HashMap<String, Integer> m_samplerMap;
	private final ArrayList<BaseLight> m_lights;
	private BaseLight m_activeLight;

	private final Shader m_forwardAmbient;
	private Camera m_mainCamera;

	public RenderingEngine() {
		super();
		m_lights = new ArrayList<BaseLight>();
		m_samplerMap = new HashMap<String, Integer>();
		m_samplerMap.put("diffuse", 0);
		m_samplerMap.put("normalMap", 1);
		m_samplerMap.put("dispMap", 2);

		AddVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));

		m_forwardAmbient = new Shader("forward-ambient");

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		//
		// glEnable(GL_DEPTH_CLAMP);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void UpdateUniformStruct(final Transform transform, final Material material, final Shader shader, final String uniformName, final String uniformType) {
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
	}

	public void Render(final GameObject object) throws IllegalStateException {
		if (GetMainCamera() == null) {
			System.err.println("Error! Main camera not found. This is very very big bug, and game will crash.");
			throw new IllegalStateException("Camera was not attached to the RenderingEngine");
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		object.RenderAll(m_forwardAmbient, this);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glDepthMask(false);
		GL11.glDepthFunc(GL11.GL_EQUAL);

		for (final BaseLight light : m_lights) {
			m_activeLight = light;
			object.RenderAll(light.GetShader(), this);
		}

		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static String GetOpenGLVersion() {
		return GL11.glGetString(GL11.GL_VERSION);
	}

	public void AddLight(final BaseLight light) {
		m_lights.add(light);
	}

	public void AddCamera(final Camera camera) {
		m_mainCamera = camera;
	}

	public int GetSamplerSlot(final String samplerName) {
		return m_samplerMap.get(samplerName);
	}

	public BaseLight GetActiveLight() {
		return m_activeLight;
	}

	public Camera GetMainCamera() {
		return m_mainCamera;
	}

	public void SetMainCamera(final Camera mainCamera) {
		m_mainCamera = mainCamera;
	}
}
