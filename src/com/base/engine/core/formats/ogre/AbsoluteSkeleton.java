package com.base.engine.core.formats.ogre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbsoluteSkeleton {
	List<Bone> bones;
	private HashMap<Integer,Bone> hsBones;
	
	public AbsoluteSkeleton(){
		bones=new ArrayList<Bone>();
		hsBones=new HashMap<Integer, Bone>();
	}
	

	public List<Bone> getBones(){
		return bones;
	}
	
	public void setBones(List<Bone> bones){
		this.bones=bones;
	}
	
	public void hashBones() throws Exception{
		List<Bone> lstBones = new ArrayList<Bone>();
		for(Bone b:this.bones){
			lstBones.addAll(b.getPlainHierarchy());
		}
		
		for(Bone b:lstBones){
			if(hsBones.containsKey(b.getBoneId())){
				throw new Exception("Bone "+b.getBoneId()+" was already in the list");
			}
			
			hsBones.put(b.getBoneId(), b);
		}

	}
	
	public Bone getBoneById(Integer id){
		try{
			return hsBones.get(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public String toString(){
		StringBuffer sb= new StringBuffer();
		for(Bone child:bones){
			sb.append(child);
		}
		
		return sb.toString();
	}
}
