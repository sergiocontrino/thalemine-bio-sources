package org.intermine.bio.dataflow.config;

public class SourceMap {

	private String sourceUniqueName;
	private String targetUniqueName;
	private String targetClassName;
	private String targetName;
	
	
	public SourceMap(String sourceUniqueName, String targetUniqueName, String targetName, String targetClassName) {
		this.sourceUniqueName = sourceUniqueName;
		this.targetUniqueName = targetUniqueName;
		this.targetName = targetName;
		this.targetClassName = targetClassName;
	}
	
	public String getSourceUniqueName() {
		return sourceUniqueName;
	}
	public void setSourceUniqueName(String sourceUniqueName) {
		this.sourceUniqueName = sourceUniqueName;
	}
	public String getTargetUniqueName() {
		return targetUniqueName;
	}
	public void setTargetUniqueName(String targetUniqueName) {
		this.targetUniqueName = targetUniqueName;
	}
	public String getTargetClassName() {
		return targetClassName;
	}
	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}
	
	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
	public String toString() {
		return "SourceMap [sourceUniqueName=" + sourceUniqueName + ", targetUniqueName=" + targetUniqueName
				+ ", targetClassName=" + targetClassName + ", targetName=" + targetName + "]";
	}
	
	
	
}
