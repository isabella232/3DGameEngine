package com.base.engine.rendering.animation;

import java.util.ArrayList;
import java.util.List;

import com.base.engine.core.math.Matrix4f;
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Vector4f;

public class BoneDeformationGroup {
	Integer boneId;
	List<VertexDeformation> vertexGroup;
	Matrix4f matrix;

	public BoneDeformationGroup(){
		boneId = null;
		vertexGroup = new ArrayList<VertexDeformation>();
		matrix=new Matrix4f();
	}
	
	public BoneDeformationGroup(Integer boneId){
		this.boneId = boneId;
		this.vertexGroup = new ArrayList<VertexDeformation>();
		matrix=new Matrix4f();
	}
	
	public BoneDeformationGroup(Integer boneId, List<VertexDeformation> vertexGroup){
		this.boneId = boneId;
		this.vertexGroup = vertexGroup;
	}
	
	public void addVertexDeformation(int vId, float weight){
		VertexDeformation vd = new VertexDeformation(vId, weight);
		vertexGroup.add(vd);
	}
	
//	public void rotate(float r, float x, float y, float z){
//		Matrix4f mtx = new Matrix4f();
//		mtx.rotate(r, new Vector3f(x,y,z));
//		for(VertexDeformation vd:vertexGroup){
//			Vector4f vt = new Vector4f();
//			Matrix4f.transform(mtx, new Vector4f(vd.vertex.position.x,vd.vertex.position.y,vd.vertex.position.z,0), vt);
//			vd.vertex.position.x=vt.x;
//			vd.vertex.position.y=vt.y;
//			vd.vertex.position.z=vt.z;
//		}
//	}
//	
//	public void rotateAround(float r, float px,float py,float pz,float vx, float vy, float vz){
//		Matrix4f mtx = new Matrix4f();
//		//
//		mtx.translate(new Vector3f(px,py,pz));
//		mtx.rotate(r, new Vector3f(vx,vy,vz));
//		mtx.translate(new Vector3f(-px,-py,-pz));
//
//		//mtx.translate(new Vector3f(-px,-py,-pz));
//		for(VertexDeformation vd:vertexGroup){
//			Vector4f vt = new Vector4f();
//			Matrix4f.transform(mtx, new Vector4f(vd.vertex.position.x,vd.vertex.position.y,vd.vertex.position.z,1), vt);
//			vd.vertex.mov.x = vt.x;
//			vd.vertex.mov.y = vt.y;
//			vd.vertex.mov.z = vt.z;
//			//vd.vertex.mov=vd.vertex.position.copy();
//
//		}
//		
//		System.out.println("Rotating around "+vx+","+vy+","+vz);
//	}
}
