package com.base.engine.rendering.model.mesh;

import java.util.HashMap;
import java.util.Map;

import com.base.engine.rendering.model.mesh.animation.Bone;
import com.base.engine.rendering.model.mesh.animation.BoneDeformationGroup;

public class Skeleton {

	private final Map<Integer, Bone> boneHierarchy = new HashMap<Integer, Bone>();
	private final Map<Integer, BoneDeformationGroup> boneDeformationGroups;

	public Skeleton() {
		this(new HashMap<Integer, BoneDeformationGroup>());
	}

	public Skeleton(Map<Integer, BoneDeformationGroup> boneDeformationGroups) {
		this.boneDeformationGroups = boneDeformationGroups;
	}

	public void addBone(Bone bone) {
		getBoneHierarchy().put(bone.getBoneId(), bone);
	}

	public Bone getBone(int id) {
		return getBoneHierarchy().get(id);
	}

	public Bone getBone(String name) {

		if (name == null || name.isEmpty()) {
			return null;
		}

		for (Bone bone : getBoneHierarchy().values()) {

			if (name.equals(bone.getName())) {
				return bone;
			}

		}

		return null;
	}

	public Map<Integer, Bone> getBoneHierarchy() {
		return boneHierarchy;
	}

	public Map<Integer, BoneDeformationGroup> getBoneDeformationGroups() {
		return boneDeformationGroups;
	}


}
