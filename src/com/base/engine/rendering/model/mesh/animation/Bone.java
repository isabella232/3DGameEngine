package com.base.engine.rendering.model.mesh.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.base.engine.core.Transform;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;

public class Bone {

	private Transform transform = new Transform();
	private Bone parent;
	private int boneId;
	private List<Bone> childs;
	private HashMap<String, List<Keyframe>> frames;

	private String name;

	public Bone() {

		parent = null;
		childs = new ArrayList<Bone>();
		frames = new HashMap<String, List<Keyframe>>();
		name = null;
	}

	public void addChild(Bone bone) {

		childs.add(bone);
		bone.setParent(this);
	}

	public void setPosition(float x, float y, float z) {
		getTransform().setPos(new Vector3f(x, y, z));
	}

	public void setRotation(float x, float y, float z, float angle) {
		getTransform().setRot(new Quaternion(new Vector3f(x, y, z), angle));
	}

	public Bone getParent() {

		return parent;
	}

	public void setParent(Bone parent) {
		
		this.parent = parent;
		transform.setParent(parent.getTransform());

	}

	public Transform getTransform() {
		return transform;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public List<Bone> getChilds() {
		if (childs == null) {
			childs = new ArrayList<Bone>();
		}
		return childs;
	}

	public List<Keyframe> getFrames(String action) {

		return frames.get(action);
	}

	public boolean isAnimated() {

		return (frames != null && frames.size() > 0);
	}

	public void setFrames(String action, List<Keyframe> frameList) {

		this.frames.put(action, frameList);
	}

	public Integer getBoneId() {

		return boneId;
	}

	public void setBoneId(Integer boneId) {

		this.boneId = boneId;
	}

	public Bone copy() {

		Bone bone = new Bone();
		bone.boneId = boneId;
		bone.name = name;
		bone.parent = parent;
		bone.transform = transform;
		return bone;
	}

	public Keyframe getFrame(String action, int frameIndex) {

		try {
			return frames.get(action).get(frameIndex);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(this.name + ":" + getTransform().getPos() + "\n");
		for (Bone child : childs) {
			sb.append(child);
		}

		return sb.toString();
	}

	public List<Bone> getPlainHierarchy() {

		List<Bone> lstBones = new ArrayList<Bone>();

		lstBones.add(this);

		if (this.childs != null) {
			for (Bone child : childs) {
				lstBones.addAll(child.getPlainHierarchy());
			}
		}

		return lstBones;
	}

}