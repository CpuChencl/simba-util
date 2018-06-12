package com.simba.codegenerate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simba.annotation.DescAnnotation;
import com.simba.annotation.DBFieldAnnotation;
import com.simba.framework.util.common.AnnotationUtil;
import com.simba.framework.util.common.ReflectUtil;
import com.simba.framework.util.common.StringUtil;
import com.simba.framework.util.file.PropertiesUtil;
import com.simba.framework.util.freemarker.FreemarkerUtil;
import com.simba.model.constant.ConstantData;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * 代码生成器工具类
 * 
 * @author caozj
 *
 */
public class CodeGenerateUtil {

	private static final Log logger = LogFactory.getLog(CodeGenerateUtil.class);

	private String packageName = null;

	private String srcPath = "/src/main/java/";

	private String resourcesPath = "/src/main/resources/";

	private String daoProjectSuffix = "-dao";

	private String serviceProjectSuffix = "-service";

	private static class CodeGenerateUtilHolder {
		private static CodeGenerateUtil instance = new CodeGenerateUtil();
	}

	private CodeGenerateUtil() {
		packageName = PropertiesUtil.read("/application.properties").get("code.generate.package");
	}

	public static CodeGenerateUtil getInstance() {
		return CodeGenerateUtilHolder.instance;
	}

	/**
	 * 生成页面文件
	 * 
	 * @param webPath
	 * @param param
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void generatePageFile(String webPath, Map<String, Object> param, PAGETYPE pageType) throws IOException, TemplateException {
		String firstLower = param.get("firstLower").toString();
		String jsDir = webPath + resourcesPath + "static/js/" + firstLower;
		String pageDir = webPath + resourcesPath + "templates/" + firstLower;
		String jsFile = jsDir + "/" + firstLower + ".js";
		String addFile = pageDir + "/add.ftl";
		String updateFile = pageDir + "/update.ftl";
		String listFile = pageDir + "/list.ftl";
		String tableFile = pageDir + "/table.ftl";
		String jsContent = getJs(param, pageType);
		String addContent = getAdd(param, pageType);
		String updateContent = getUpdate(param, pageType);
		String listContent = getList(param, pageType);
		String tableContent = getTable(param, pageType);
		FileUtils.writeStringToFile(new File(jsFile), jsContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + jsFile);
		FileUtils.writeStringToFile(new File(addFile), addContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + addFile);
		FileUtils.writeStringToFile(new File(updateFile), updateContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + updateFile);
		FileUtils.writeStringToFile(new File(listFile), listContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + listFile);
		FileUtils.writeStringToFile(new File(tableFile), tableContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + tableFile);
	}

	private String getTable(Map<String, Object> param, PAGETYPE pageType) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/page/table.ftl", param);
	}

	private String getList(Map<String, Object> param, PAGETYPE pageType) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/page/list.ftl", param);
	}

	private String getUpdate(Map<String, Object> param, PAGETYPE pageType) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/page/update.ftl", param);
	}

	private String getAdd(Map<String, Object> param, PAGETYPE pageType) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/page/add.ftl", param);
	}

	private String getJs(Map<String, Object> param, PAGETYPE pageType) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/js/js.ftl", param);
	}

	/**
	 * 构造参数
	 * 
	 * @param c
	 * @param searchFormC
	 * @return
	 */
	private Map<String, Object> buildParam(Class<?> c, Class<?> searchFromC, PAGETYPE pageType) {
		Map<String, Object> param = new HashMap<String, Object>();
		String className = c.getSimpleName();
		param.put("isSearch", false);
		if (searchFromC != null) {
			String searchFormClassName = searchFromC.getSimpleName();
			String searchFormFirstLower = StringUtil.getFirstLower(searchFormClassName);
			param.put("searchFormClassName", searchFormClassName);
			param.put("searchFormFirstLower", searchFormFirstLower);
			List<String> searchFormFieldNames = ReflectUtil.getAllPropertiesName(searchFromC);			
			List<Map<String,String>> searchFormFields = new ArrayList<>();
			param.put("importDateJs", false);
			for (String field : searchFormFieldNames) {
				Map<String,String> tableField = new HashMap<>(); 
				tableField.put("key", field);
				tableField.put("firstUpperKey", StringUtil.getFirstUpper(field));
				DBFieldAnnotation ta = AnnotationUtil.getFiledAnnotion(searchFromC, field, DBFieldAnnotation.class);
				Field f = null;
				try {
					f = searchFromC.getDeclaredField(field);
					if (f.getType() == Date.class) {
						param.put("importDateJs", true);
					}
					tableField.put("type", f.getType().getSimpleName());
				} catch (NoSuchFieldException e) {
					logger.error("获取字段： "+ field+" 的类型异常, error "+e.getMessage().toString());
				} catch (SecurityException e) {
					logger.error("获取字段： "+ field+" 的类型异常, error "+e.getMessage().toString());
				}
				tableField.put("desc", StringUtil.getFirstUpper(field));
				tableField.put("field", field);
				tableField.put("oper", "=");
				
				if (ta != null) {
					if (StringUtils.isNotEmpty(ta.field())) {
						tableField.put("field", ta.field());
					}
					if (StringUtils.isNotEmpty(ta.oper())) {
						tableField.put("oper", ta.oper());
					}					
					if (StringUtils.isNotEmpty(ta.desc())) {
						tableField.put("desc", ta.desc());
					}
				} 
				
				searchFormFields.add(tableField);
			}
			param.put("searchFormFields", searchFormFields);
			param.put("isSearch", true);
		}
		
		
		String firstLower = StringUtil.getFirstLower(className);
		DescAnnotation descAnnotation = AnnotationUtil.getClassAnnotation(c, DescAnnotation.class);
		if (descAnnotation == null) {
			param.put("classDesc", "");
		} else {
			param.put("classDesc", descAnnotation.desc());
		}
		param.put("className", className);
		param.put("firstLower", firstLower);
		param.put("packageName", packageName);
		param.put("pageType", pageType.getName());
		List<String> fields = ReflectUtil.getAllPropertiesName(c);
		List<Map<String, String>> filedsWithPage = new ArrayList<>();
		String updateProperties = "";
		String insertProperties = "";
		String params = "";
		String propertiesCount = "";
		for (String field : fields) {
			if ("id".equals(field) || "serialVersionUID".equals(field)) {
				continue;
			}
			if (!"parentID".equals(field)) {
				Map<String, String> fDesc = new HashMap<>(2);
				fDesc.put("key", field);
				DescAnnotation da = AnnotationUtil.getFiledAnnotion(c, field, DescAnnotation.class);
				if (da == null) {
					fDesc.put("desc", field);
				} else {
					fDesc.put("desc", da.desc());
				}
				filedsWithPage.add(fDesc);
			}
			updateProperties += " " + field + " = ? ,";
			insertProperties += " " + field + ",";
			propertiesCount += "?,";
			params += firstLower + ".get" + StringUtil.getFirstUpper(field) + "(),";
		}

		updateProperties = updateProperties.substring(0, updateProperties.length() - 1);
		insertProperties = insertProperties.substring(0, insertProperties.length() - 1);
		propertiesCount = propertiesCount.substring(0, propertiesCount.length() - 1);
		params = params.substring(0, params.length() - 1);
		param.put("updateProperties", updateProperties);
		param.put("insertProperties", insertProperties);
		param.put("params", params);
		param.put("propertiesCount", propertiesCount);
		param.put("filedsWithPage", filedsWithPage);
		String idType = "Integer";
		Field[] fs = c.getDeclaredFields();
		Map<String, String> typeMapping = new HashMap<>();
		typeMapping.put("int", "Integer");
		typeMapping.put("long", "Long");
		typeMapping.put("String", "String");
		typeMapping.put("Integer", "Integer");
		typeMapping.put("Long", "Long");
		for (Field f : fs) {
			if ("id".equals(f.getName())) {
				String type = f.getType().getSimpleName();
				idType = typeMapping.get(type);
				logger.info("id的类型为" + idType);
			}
		}
		param.put("idType", idType);
		String countType = "Integer";
		String countName = "Int";
		if ("Long".equals(idType)) {
			countType = "Long";
			countName = "Long";
		}
		param.put("countType", countType);
		param.put("countName", countName);
		return param;
	}

	/**
	 * 生成类文件
	 * 
	 * @param projectName
	 * @param pageType
	 * @param webPath
	 * @param param
	 * @param type
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void generateClassFile(String projectName, PAGETYPE pageType, String webPath, Map<String, Object> param, CODETYPE type)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		String className = param.get("className").toString();
		String controllerContent = getController(param, type, pageType);
		String serviceContent = getService(param, type);
		String serviceImplContent = getServiceImpl(param, type);
		String daoContent = getDao(param, type);
		String daoImplContent = getDaoImpl(param, type);
		String daoDir = getDaolDir(projectName, webPath);
		String serviceDir = getServiceDir(projectName, webPath);
		String packagePath = getPackagePath();
		String controllerFile = webPath + srcPath + packagePath + "/controller/" + className + "Controller.java";
		String serviceFile = serviceDir + srcPath + packagePath + "/service/" + className + "Service.java";
		String serviceImplFile = serviceDir + srcPath + packagePath + "/service/impl/" + className + "ServiceImpl.java";
		String daoFile = daoDir + srcPath + packagePath + "/dao/" + className + "Dao.java";
		String daoImplFile = daoDir + srcPath + packagePath + "/dao/impl/" + className + "DaoImpl.java";
		FileUtils.writeStringToFile(new File(controllerFile), controllerContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + controllerFile);
		FileUtils.writeStringToFile(new File(serviceFile), serviceContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + serviceFile);
		FileUtils.writeStringToFile(new File(serviceImplFile), serviceImplContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + serviceImplFile);
		FileUtils.writeStringToFile(new File(daoFile), daoContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + daoFile);
		FileUtils.writeStringToFile(new File(daoImplFile), daoImplContent, ConstantData.DEFAULT_CHARSET);
		logger.info("生成" + daoImplFile);
	}

	private String getController(Map<String, Object> param, CODETYPE type, PAGETYPE pageType)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/page/" + pageType.getName() + "/controller/controller.ftl", param);
	}

	private String getService(Map<String, Object> param, CODETYPE type) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/service/service.ftl", param);
	}

	private String getServiceImpl(Map<String, Object> param, CODETYPE type) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/service/serviceImpl.ftl", param);
	}

	private String getDao(Map<String, Object> param, CODETYPE type) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/" + type.getFolderName() + "/dao.ftl", param);
	}

	private String getDaoImpl(Map<String, Object> param, CODETYPE type) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		return FreemarkerUtil.parseFile("codegenerate/" + type.getFolderName() + "/daoImpl4Mysql.ftl", param);
	}

	/**
	 * 创建好所有需要的路径
	 * 
	 * @param projectName
	 * @param webPath
	 * @param type
	 * @param pageType
	 * @param c
	 */
	private void initPath(String projectName, String webPath, CODETYPE type, PAGETYPE pageType, Class<?> c) {
		String daoDir = getDaolDir(projectName, webPath);
		String serviceDir = getServiceDir(projectName, webPath);
		String packagePath = getPackagePath();
		String daoImplPath = daoDir + srcPath + packagePath + "/dao/impl";
		String serviceImplPath = serviceDir + srcPath + packagePath + "/service/impl";
		String controllerPath = webPath + srcPath + packagePath + "/controller";
		new File(daoImplPath).mkdirs();
		new File(serviceImplPath).mkdirs();
		new File(controllerPath).mkdirs();
		if (pageType != PAGETYPE.NONE) {
			String jsDir = webPath + resourcesPath + "/static/js/" + StringUtil.getFirstLower(c.getSimpleName());
			String pageDir = webPath + resourcesPath + "/templates/" + StringUtil.getFirstLower(c.getSimpleName());
			new File(jsDir).mkdirs();
			new File(pageDir).mkdirs();
		}
	}

	private String getPackagePath() {
		return packageName.replaceAll("\\.", "/");
	}
	
	/**
	 * 生成代码
	 * 
	 * @param classes
	 *            要生成代码的Class对象数组
	 * @param codeType
	 *            代码类型
	 * @param pageType
	 *            页面类型
	 * @param projectName
	 *            项目名称
	 * @param webPath
	 *            web项目路径
	 * @throws TemplateException
	 * @throws IOException
	 * @throws ParseException
	 * @throws MalformedTemplateNameException
	 * @throws TemplateNotFoundException
	 */
	public void codeGenerate(Class<?>[] classes, CODETYPE codeType, PAGETYPE pageType, String projectName, String webPath)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		codeGenerate(classes, null, codeType, pageType, projectName, webPath);
	}

	/**
	 * 生成代码
	 * 
	 * @param classes
	 *            要生成代码的Class对象数组
	 * @param searchFormClasses
	 * 			     要生成代码的表单Class对象数组
	 * @param codeType
	 *            代码类型
	 * @param pageType
	 *            页面类型
	 * @param projectName
	 *            项目名称
	 * @param webPath
	 *            web项目路径
	 * @throws TemplateException
	 * @throws IOException
	 * @throws ParseException
	 * @throws MalformedTemplateNameException
	 * @throws TemplateNotFoundException
	 */
	public void codeGenerate(Class<?>[] classes, Class<?>[] searchFormClasses, CODETYPE codeType, PAGETYPE pageType, String projectName, String webPath)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		int counter = 0;
		for (Class<?> c : classes) {
			Class<?> searchFormClass = null;
			if(searchFormClasses!=null && counter < searchFormClasses.length) {
				searchFormClass = searchFormClasses[counter];
			}
			counter++;
			codeGenerate(c, searchFormClass, codeType, pageType, projectName, webPath);
		}
	}

	/**
	 * 生成代码
	 * 
	 * @param c
	 *            要生成代码的Class对象
	 * @param searchFormC
	 * 			    要生成代码的表单Class对象数组
	 * @param codeType
	 *            代码类型
	 * @param pageType
	 *            页面类型
	 * @param projectName
	 *            项目名称
	 * @param webPath
	 *            web项目路径
	 * @throws TemplateException
	 * @throws IOException
	 * @throws ParseException
	 * @throws MalformedTemplateNameException
	 * @throws TemplateNotFoundException
	 */
	private void codeGenerate(Class<?> c, Class<?> searchFormC, CODETYPE codeType, PAGETYPE pageType, String projectName, String webPath)
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		logger.info("生成代码" + c.getName() + "开始,代码类型为" + codeType.getFolderName() + ",页面类型为" + pageType.getName() + ",项目名称:" + projectName + ",web项目目录:" + webPath);
		initPath(projectName, webPath, codeType, pageType, c);
		Map<String, Object> param = buildParam(c, searchFormC, pageType);
		generateClassFile(projectName, pageType, webPath, param, codeType);
		if (pageType != PAGETYPE.NONE) {
			generatePageFile(webPath, param, pageType);
		}
		logger.info("生成代码" + c.getName() + "结束,代码类型为" + codeType.getFolderName() + ",页面类型为" + pageType.getName() + ",项目名称:" + projectName + ",web项目目录:" + webPath);
	}

	/**
	 * 获取dao项目的路径
	 * 
	 * @param projectName
	 * @param webPath
	 * @return
	 */
	private String getDaolDir(String projectName, String webPath) {
		return new File(webPath).getParentFile().getAbsolutePath() + "/" + projectName + daoProjectSuffix;
	}

	/**
	 * 获取service项目的路径
	 * 
	 * @param projectName
	 * @param webPath
	 * @return
	 */
	private String getServiceDir(String projectName, String webPath) {
		return new File(webPath).getParentFile().getAbsolutePath() + "/" + projectName + serviceProjectSuffix;
	}

}
