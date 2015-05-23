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

package com.base.engine.core;

import com.base.engine.components.GameComponent;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;

public class GameObject
{
	private ArrayList<GameObject> m_children;
	private ArrayList<GameComponent> m_components;
	private Transform m_transform;
	private CoreEngine m_engine;

	public GameObject()
	{
		m_transform = new Transform();
		m_engine = null;
	}

	/**
	 * Check to see if any {@link GameObject} are attached to this {@link GameObject}
	 * @return true if m_children is not null or empty
	 */
	public boolean HasChildren() 
	{
		return m_children != null && !m_children.isEmpty();
	}
	
	/**
	 * Check to see if there are any {@link GameComponent}s attached to this object
	 * @return true if m_children is not null or empty
	 */
	public boolean HasComponents() 
	{
		return m_components != null && !m_components.isEmpty();
	}
	
	/**
	 * Check to see if there is anything attached to this {@link GameObject}
	 * @return true if m_children is not null or empty
	 */
	public boolean HasAttached() 
	{
		return HasChildren() || HasComponents();
	}
	
	public GameObject AddChild(GameObject child)
	{
		
		if (m_children == null)
		{
			m_children = new ArrayList<GameObject>();
		}
		
		m_children.add(child);
		child.SetEngine(m_engine);
		child.GetTransform().SetParent(m_transform);
		
		return this;
	}

	public GameObject AddComponent(GameComponent component)
	{
		
		if (m_components == null)
		{
			m_components = new ArrayList<GameComponent>();
		}
		
		m_components.add(component);
		component.SetParent(this);

		return this;
	}

	public void InputAll(float delta)
	{
		Input(delta);

		if (!HasChildren())
			return;
		
		for(GameObject child : m_children)
			child.InputAll(delta);
	}

	public void UpdateAll(float delta)
	{
		Update(delta);

		if (!HasChildren())
			return;
		
		for(GameObject child : m_children)
			child.UpdateAll(delta);
	}

	public void RenderAll(Shader shader, RenderingEngine renderingEngine)
	{
		Render(shader, renderingEngine);

		if (!HasChildren())
			return;
		
		for(GameObject child : m_children)
			child.RenderAll(shader, renderingEngine);
	}

	public void Input(float delta)
	{
		m_transform.Update();

		if (!HasComponents())
			return;
		
		for(GameComponent component : m_components)
			component.Input(delta);
	}

	public void Update(float delta)
	{

		if (!HasComponents())
			return;
		
		for(GameComponent component : m_components)
			component.Update(delta);
	}

	public void Render(Shader shader, RenderingEngine renderingEngine)
	{

		if (!HasComponents())
			return;
		
		for(GameComponent component : m_components)
			component.Render(shader, renderingEngine);
	}

	public ArrayList<GameObject> GetAllAttached()
	{
		ArrayList<GameObject> result = new ArrayList<GameObject>();

		if (!HasChildren())
			return result;
		
		for(GameObject child : m_children)
			result.addAll(child.GetAllAttached());

		result.add(this);
		return result;
	}

	public Transform GetTransform()
	{
		return m_transform;
	}

	public void SetEngine(CoreEngine engine)
	{
		if(this.m_engine != engine)
		{
			this.m_engine = engine;

			if (HasComponents())
				for(GameComponent component : m_components)
					component.AddToEngine(engine);


			if (HasChildren())
				for(GameObject child : m_children)
					child.SetEngine(engine);
		}
	}
}
