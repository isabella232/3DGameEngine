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

import java.io.File;

import org.lwjgl.Sys;

import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.FreeLook;
import com.base.engine.components.FreeMove;
import com.base.engine.components.MeshRenderer;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.formats.obj.ObjLoader;
import com.base.engine.core.formats.ogre.OgreLoader;
import com.base.engine.core.math.Attenuation;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.model.mesh.Model;

public class TestGame extends Game {
	@Override
	public void init() {
		
		
		final Material material2 = new Material(new Texture("bricks.jpg"), 1, 8, new Texture("bricks_normal.jpg"), new Texture("bricks_disp.png"), 0.03f, -0.5f);
		final Model floor = ObjLoader.loadObjModel("plane3.obj", material2);
		
		final Material material = new Material(new Texture("oto1024.jpg"), 1, 8, new Texture("bricks2_normal.png"), new Texture("bricks2_disp.jpg"), 0.04f, -0.0f);
		//final Model monkey = ObjLoader.loadObjModel("monkey3.obj", material2);
		

		final GameObject planeObject = new GameObject();
		planeObject.addComponent(floor);
		planeObject.getTransform().getPos().set(0, -1, 5);

		
		final GameObject elephantObject = new GameObject();

		final Model animatedModel = OgreLoader.loadOgreModel("OtoNew", material);
		elephantObject.getTransform().getPos().set(0, -1, 5);
		elephantObject.addComponent(animatedModel);
		
		
		final GameObject directionalLightObject = new GameObject();
		final DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0, 0, 1), 0.8f);

		directionalLightObject.addComponent(directionalLight);

		final GameObject pointLightObject = new GameObject();
		pointLightObject.addComponent(new PointLight(new Vector3f(0, 1, 0), 0.8f, new Attenuation(0, 0, 1)));

		final SpotLight spotLight = new SpotLight(new Vector3f(0, 1, 1), 0.8f, new Attenuation(0, 0, 0.1f), 0.7f);

		final GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(spotLight);

		spotLightObject.getTransform().getPos().set(5, 0, 5);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(90.0f)));

		addObject(planeObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);
		addObject(elephantObject);

		//final GameObject testMesh3 = new GameObject().addComponent(new LookAtComponent()).addComponent(new MeshRenderer(tempMesh, material));

		addObject(
		// AddObject(
		new GameObject().addComponent(new FreeLook(0.5f, false)).addComponent(new FreeMove(10.0f, 4.0f)).addComponent(new Camera(new Matrix4f().initPerspective((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f))));

		//addObject(testMesh3);

		//testMesh3.getTransform().getPos().set(5, 5, 5);
		//testMesh3.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(-70.0f)));

		//addObject(new GameObject().addComponent(new MeshRenderer(new Mesh("monkey3.obj"), material2)));

		directionalLight.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
	}
}
