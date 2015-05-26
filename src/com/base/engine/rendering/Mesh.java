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
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector3f;
import com.base.engine.core.math.Vertex;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;

public class Mesh {
	private static HashMap<String, MeshResource> s_loadedModels = new HashMap<String, MeshResource>();
	private MeshResource m_resource;
	private final String m_fileName;

	public Mesh(final String fileName) {
		m_fileName = fileName;
		final MeshResource oldResource = Mesh.s_loadedModels.get(fileName);

		if (oldResource != null) {
			m_resource = oldResource;
			m_resource.AddReference();
		} else {
			LoadMesh(fileName);
			Mesh.s_loadedModels.put(fileName, m_resource);
		}
	}

	public Mesh(final Vertex[] vertices, final int[] indices) {
		this(vertices, indices, false);
	}

	public Mesh(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		m_fileName = "";
		AddVertices(vertices, indices, calcNormals);
	}

	@Override
	protected void finalize() {
		if (m_resource.RemoveReference() && !m_fileName.isEmpty()) {
			Mesh.s_loadedModels.remove(m_fileName);
		}
	}

	private void AddVertices(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		if (calcNormals) {
			CalcNormals(vertices, indices);
		}

		m_resource = new MeshResource(indices.length);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_resource.GetVbo());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.CreateFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.CreateFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}

	public void Draw() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, m_resource.GetVbo());
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 12);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 20);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 32);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, m_resource.GetIbo());
		GL11.glDrawElements(GL11.GL_TRIANGLES, m_resource.GetSize(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
	}

	private void CalcNormals(final Vertex[] vertices, final int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			final int i0 = indices[i];
			final int i1 = indices[i + 1];
			final int i2 = indices[i + 2];

			final Vector3f v1 = vertices[i1].GetPos().Sub(vertices[i0].GetPos());
			final Vector3f v2 = vertices[i2].GetPos().Sub(vertices[i0].GetPos());

			final Vector3f normal = v1.Cross(v2).Normalized();

			vertices[i0].SetNormal(vertices[i0].GetNormal().Add(normal));
			vertices[i1].SetNormal(vertices[i1].GetNormal().Add(normal));
			vertices[i2].SetNormal(vertices[i2].GetNormal().Add(normal));
		}

		for (final Vertex vertice : vertices) {
			vertice.SetNormal(vertice.GetNormal().Normalized());
		}
	}

	private Mesh LoadMesh(final String fileName) {
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];

		if (!ext.equals("obj")) {
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		final OBJModel test = new OBJModel("./res/models/" + fileName);
		final IndexedModel model = test.ToIndexedModel();

		final ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for (int i = 0; i < model.GetPositions().size(); i++) {
			vertices.add(new Vertex(model.GetPositions().get(i), model.GetTexCoords().get(i), model.GetNormals().get(i), model.GetTangents().get(i)));
		}

		final Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		final Integer[] indexData = new Integer[model.GetIndices().size()];
		model.GetIndices().toArray(indexData);

		AddVertices(vertexData, Util.ToIntArray(indexData), false);

		return this;
	}
}
