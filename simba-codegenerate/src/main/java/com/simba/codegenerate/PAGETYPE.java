package com.simba.codegenerate;

/**
 * 代码生成器生成的页面类型
 * 
 * @author caozj
 *
 */
public enum PAGETYPE {

	NONE("none", "不生成页面"),

	TABLE("table", "生成普通表格列表页面"),

	// 此类型的对象必须包含id , text , parentID字段，且存入数据库
	TREETABLE("treeTable", "生成左树右表页面");
	
	

	private String name;

	private String desc;

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	private PAGETYPE(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

}
