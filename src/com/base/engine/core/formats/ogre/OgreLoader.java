package com.base.engine.core.formats.ogre;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.base.engine.core.Util;
import com.base.engine.core.formats.ogre.mesh.OgreFace;
import com.base.engine.core.formats.ogre.mesh.OgreMesh;
import com.base.engine.core.formats.ogre.mesh.Submesh;
import com.base.engine.core.formats.ogre.mesh.Vertexboneassignment;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.core.math.Vertex;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.model.mesh.Model;
import com.base.engine.rendering.model.mesh.Skeleton;
import com.base.engine.rendering.model.mesh.animation.BoneDeformationGroup;

public class OgreLoader {

	private static void loadGeometry(List<com.base.engine.core.formats.ogre.mesh.Vertexbuffer> vertexBuffer, List<Vertex> vertexList) {

		for (com.base.engine.core.formats.ogre.mesh.Vertexbuffer vb : vertexBuffer) {

			if (vb.getPositions().equalsIgnoreCase("true")) {

				for (com.base.engine.core.formats.ogre.mesh.Vertex v : vb.getVertex()) {

					Vector3f normal = null;
					Vector2f tx = null;

					Vector3f position = new Vector3f(Float.parseFloat(v.getPosition().getX()), Float.parseFloat(v.getPosition().getY()), Float.parseFloat(v.getPosition().getZ()));

					if (vb.getNormals().equals("true")) {
						normal = new Vector3f(Float.parseFloat(v.getNormal().getX()), Float.parseFloat(v.getNormal().getY()), Float.parseFloat(v.getNormal().getZ()));
					}

					if (vb.getTextureCoords().equals("1")) {
						tx = new Vector2f(Float.parseFloat(v.getTexcoord().get(0).getU()), Float.parseFloat(v.getTexcoord().get(0).getV()));
					}

					Vertex vertex = new Vertex(position, tx, normal);

					// System.out.println("Adding vertex: " +
					// vertexList.size());
					vertexList.add(vertex);
				}
			}
		}
	}

	private static void loadVertexBoneAssignments(List<Vertexboneassignment> vertexBoneAssignment, HashMap<Integer, BoneDeformationGroup> deformationGroups) {

		// Load bone assignments
		for (Vertexboneassignment vba : vertexBoneAssignment) {

			Integer boneId = Integer.parseInt(vba.getBoneindex());
			float weight = Float.parseFloat(vba.getWeight());

			if (!deformationGroups.containsKey(boneId)) {
				deformationGroups.put(boneId, new BoneDeformationGroup(boneId));
			}
			
			deformationGroups.get(boneId).addVertexDeformation(Integer.parseInt(vba.getVertexindex()), weight);

		}

		System.out.println("Added " + deformationGroups.size() + " Bone Deformation Groups ");

	}

	private static Mesh loadMesh(File ogreMeshFile) throws IllegalStateException {
		HashMap<Integer, BoneDeformationGroup> deformationGroups = new HashMap<Integer, BoneDeformationGroup>();
		List<Vertex> vertexList = new ArrayList<Vertex>();
		int[] indices = null;

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("com.base.engine.core.formats.ogre.mesh");
			Unmarshaller u = jc.createUnmarshaller();
			OgreMesh element = (OgreMesh) u.unmarshal(ogreMeshFile);
			
			assert (element.getSubmeshes().getSubmesh().size() == 1);

			for (Submesh sm : element.getSubmeshes().getSubmesh()) {

				// Check to see if we have 'usesharedvertices'
				if (sm.getUsesharedvertices().equalsIgnoreCase("true")) {

					// Load all Shared Geometry
					assert (element.getSharedgeometry().getVertexbuffer().size() == 1);
					vertexList = new ArrayList<Vertex>(Integer.parseInt(element.getSharedgeometry().getVertexcount()));
					loadGeometry(element.getSharedgeometry().getVertexbuffer(), vertexList);

					// Load bone assignments for deformation groups.
					loadVertexBoneAssignments(element.getBoneassignments().getVertexboneassignment(), deformationGroups);

				} else {
					// We are not using shared vertices so load from geometry.

					// Load all Geometry
					assert (sm.getGeometry().getVertexbuffer().size() == 1);
					vertexList = new ArrayList<Vertex>(Integer.parseInt(sm.getGeometry().getVertexcount()));

					loadGeometry(sm.getGeometry().getVertexbuffer(), vertexList);

					System.out.println("VB Size: " + vertexList.size());

					// Load bone assignments
					loadVertexBoneAssignments(sm.getBoneassignments().getVertexboneassignment(), deformationGroups);

				}


				// Load all faces and extract the indices.

				indices = new int[Integer.parseInt(sm.getFaces().getCount()) * 3];
				int index = 0;
				
				for (OgreFace f : sm.getFaces().getFace()) {
					
					indices[index++] = Integer.parseInt(f.getV1());
					indices[index++] = Integer.parseInt(f.getV2());
					indices[index++] = Integer.parseInt(f.getV3());
					
				}
				
				break; // TODO: Load more than one submesh
			}
			
		} catch (JAXBException e) {
			throw new IllegalStateException("Error unmarshalling xml data.");
		}
		
		return new Mesh(vertexList.toArray(new Vertex[0]), indices, true);

	}

	private static Skeleton loadSkeleton(File skeletonFile) {
		return null;
	}

	public static Model loadOgreModel(final String modelName) throws IllegalStateException {

		final File meshFile = getMeshFile(modelName);
		final File skeletonFile = getSkeletonFile(modelName);

		Mesh mesh = null;
		Skeleton skeleton = null;

		if (meshFile != null) {
			mesh = loadMesh(meshFile);
		}

		if (mesh == null) {
			throw new IllegalStateException("Mesh file contained no mesh data.");
		}

		if (skeletonFile != null) {
			skeleton = loadSkeleton(skeletonFile);
		}

		return new Model(mesh, skeleton);
	}

	/**
	 * Find an ogre file following the ogre format naming conventions.
	 * 
	 * <pre>
	 * (model name).(type).xml
	 * </pre>
	 * 
	 * @param name
	 *            - Name of the ogre model
	 * @param type
	 *            - Type of the file you are looking for
	 * @return <code>null</code> if the requested file could not be found.
	 */
	private static File getOgreModelFile(String name, final String type) {

		if (name == null || name.isEmpty()) {
			return null;
		}

		if (!name.endsWith("." + type + ".xml")) {
			name += "." + type + ".xml";
		}

		if (!name.startsWith("res/models/")) {
			name = "res/models/" + name;
		}

		File ogreFile = new File(name);

		if (Util.isFileReadable(ogreFile)) {
			return null;
		}

		return ogreFile;

	}

	/**
	 * Get a mesh file if it exists and can be read from.
	 * 
	 * @param name
	 *            - Ogre model name
	 * @return <code>null</code> if the mesh could not be found.
	 */
	private static File getMeshFile(String name) {
		return getOgreModelFile(name, "mesh");
	}

	/**
	 * Get a skeleton file if it exists and can be read from.
	 * 
	 * @param name
	 *            - Ogre model name
	 * @return <code>null</code> if the mesh could not be found.
	 */
	private static File getSkeletonFile(String name) {
		return getOgreModelFile(name, "skeleton");
	}

}
