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
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	private MeshResource resource;
	private final String fileName;

	public Mesh(final String fileName) {
		this.fileName = fileName;
		final MeshResource oldResource = Mesh.loadedModels.get(fileName);

		if (oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			loadMesh(fileName);
			Mesh.loadedModels.put(fileName, resource);
		}
	}

	public Mesh(final Vertex[] vertices, final int[] indices) {
		this(vertices, indices, false);
	}

	public Mesh(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		fileName = "";
		addVertices(vertices, indices, calcNormals);
	}

	@Override
	protected void finalize() {
		if (resource.removeReference() && !fileName.isEmpty()) {
			Mesh.loadedModels.remove(fileName);
		}
	}

	private void addVertices(final Vertex[] vertices, final int[] indices, final boolean calcNormals) {
		if (calcNormals) {
			calcNormals(vertices, indices);
		}

		resource = new MeshResource(indices.length);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, resource.getVbo());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}

	public void draw() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, resource.getVbo());
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 12);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 20);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.SIZE * 4, 32);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		GL11.glDrawElements(GL11.GL_TRIANGLES, resource.getSize(), GL11.GL_UNSIGNED_INT, 0);

		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
	}

	private void calcNormals(final Vertex[] vertices, final int[] indices) {
		for (int i = 0; i < indices.length; i += 3) {
			final int i0 = indices[i];
			final int i1 = indices[i + 1];
			final int i2 = indices[i + 2];

			final Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			final Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

			final Vector3f normal = v1.cross(v2).normalized();

			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}

		for (final Vertex vertice : vertices) {
			vertice.setNormal(vertice.getNormal().normalized());
		}
	}

	private Mesh loadMesh(final String fileName) {
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];

		if (!ext.equals("obj")) {
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		final OBJModel test = new OBJModel("./res/models/" + fileName);
		final IndexedModel model = test.toIndexedModel();

		final ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for (int i = 0; i < model.getPositions().size(); i++) {
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i), model.getTangents().get(i)));
		}

		final Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		final Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false);

		return this;
	}
}
