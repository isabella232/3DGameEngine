package com.base.engine.rendering.model.mesh;

import static org.lwjgl.opengl.GL11.*;

import java.util.Map.Entry;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.Transform;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.model.mesh.animation.Bone;
import com.base.engine.rendering.model.mesh.animation.Keyframe;

public class Model extends MeshRenderer {
	
	private Mesh mesh;
	private Skeleton skeleton;
	private Material material;
	private boolean showSkeleton = false;
	
	public Model(Mesh mesh, Skeleton skeleton, Material material) {
		super(mesh, material);
		this.mesh = mesh;
		this.skeleton = skeleton;
		this.material = material;
	}
	
	
	@Override
	public void update(float delta) {
		super.update(delta);
	}
	
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		
		if (!showSkeleton) {
			super.render(shader, renderingEngine);
		} else {
			/* glMatrixMode(GL_MODELVIEW);
			glPushMatrix();
			glLoadIdentity();
			Transform t = getTransform();
			Vector3f pos = t.getPos();
	        glScalef(1, 1, 1);
	        glTranslatef(pos.getX(), pos.getY(), pos.getZ());
	        // Rotate the model for the correct orientation.
	        glRotatef(90.0f, 1, 0, 0);
	        glRotatef(90.0f, 0, 1, 0);

	        for (Entry<Integer, Bone> bone : getSkeleton().getBoneHierarchy().entrySet()) {
	        	
	            drawBone(bone.getValue(), "Walk", 0);
	        }
	        glPopMatrix();*/
		}
		
	}
	
	public void showSkeleton(boolean showSkeleton) {
		this.showSkeleton = showSkeleton;
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
