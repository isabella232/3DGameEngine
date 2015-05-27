package com.base.engine.rendering.model.mesh;

import com.base.engine.rendering.Mesh;


public class Model {
	
	private Mesh mesh;
	private Skeleton skeleton;
	
	public Model(Mesh mesh, Skeleton skeleton) {
		this.mesh = mesh;
		this.skeleton = skeleton;
	}
	
	
	public boolean hasMesh() {
		return getMesh() != null;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public boolean hasSkeleton() {
		return getSkeleton() != null;
	}
	
	public Skeleton getSkeleton() {
		return skeleton;
	}
	
	/*
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
	*/
	
}
