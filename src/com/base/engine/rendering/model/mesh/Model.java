package com.base.engine.rendering.model.mesh;

import com.base.engine.components.MeshRenderer;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;


public class Model extends MeshRenderer {
	
	private Mesh mesh;
	private Skeleton skeleton;
	private Material material;
	
	public Model(Mesh mesh, Skeleton skeleton, Material material) {
		super(mesh, material);
		this.mesh = mesh;
		this.skeleton = skeleton;
		this.material = material;
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
	
	public Material getMaterial() {
		return material;
	}
	
}
