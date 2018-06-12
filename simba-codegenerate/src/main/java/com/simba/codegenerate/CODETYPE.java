package com.simba.codegenerate;

/**
 * 代码生成器生成的后台代码类型
 * 
 * @author caozj
 *
 */
public enum CODETYPE {

	JDBC("jdbc");

	private String folderName;

	public String getFolderName() {
		return folderName;
	}

	private CODETYPE(String folderName) {
		this.folderName = folderName;
	}
}
