package com.base.engine.core.formats.ogre.mesh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sharedgeometry", "submeshes", "skeletonlink", "boneassignments", "levelofdetail", "submeshnames", "poses", "animations", "extremes" })
@XmlRootElement(name = "mesh")
public class OgreModelFile {
	protected Sharedgeometry sharedgeometry;
	@XmlElement(required = true)
	protected Submeshes submeshes;
	protected Skeletonlink skeletonlink;
	protected Boneassignments boneassignments;
	protected Levelofdetail levelofdetail;
	protected Submeshnames submeshnames;
	protected Poses poses;
	protected Animations animations;
	protected Extremes extremes;
	
}
