package com.simba.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 全国名址数据库工具<br>
 * 
 * 使用这个工具需要依赖四个固定名称的文件：province.xml, city.xml, county.xml, town.xml
 * 
 * @author caozj
 */
public class AddressDatabase {

	private AddressDatabase() {
		init();
	}

	private static final class AddressDatabaseHolder {
		private static final AddressDatabase instance = new AddressDatabase();
	}

	public static AddressDatabase getInstance() {
		return AddressDatabaseHolder.instance;
	}

	private void init() {
		// 初始化XML解析器
		try {
			initAddress();
			provinceUnmarshaller = JAXBContext.newInstance(ProvinceRecords.class).createUnmarshaller();
			cityUnmarshaller = JAXBContext.newInstance(CityRecords.class).createUnmarshaller();
			countyUnmarshaller = JAXBContext.newInstance(CountyRecords.class).createUnmarshaller();
			townUnmarshaller = JAXBContext.newInstance(TownRecords.class).createUnmarshaller();
			// 加载数据文件
			loadData(false);
		} catch (Exception e) {
			throw new RuntimeException("加载地址XML失败", e);
		}
	}

	private void initAddress() {
		provinceFile = "/address/province.xml";
		cityFile = "/address/city.xml";
		countryFile = "/address/county.xml";
		townFile = "/address/town.xml";
	}

	private String provinceFile;
	private String cityFile;
	private String countryFile;
	private String townFile;

	// XML解析器
	private Unmarshaller provinceUnmarshaller;
	private Unmarshaller cityUnmarshaller;
	private Unmarshaller countyUnmarshaller;
	private Unmarshaller townUnmarshaller;

	/** 省份集合，已形成结构的地址库集合 */
	private List<Province> provinces = new ArrayList<Province>();

	/** 省份集合（无城市信息） */
	private List<String> provincesNoCitys = new ArrayList<String>();

	/** 原始省份数据 */
	private ProvinceRecords provinceRecords = null;

	/** 原始城市数据 */
	private CityRecords cityRecords = null;

	/** 原始区县数据 */
	private CountyRecords countyRecords = null;

	/** 原始乡镇数据 */
	private TownRecords townRecords = null;

	/**
	 * 初始化地址库
	 * 
	 * @param loadTown
	 *            是否加载乡镇数据（会额外使用大约50M的内存空间），不加载时只能使用省份、城市、县区三级数据
	 * @throws JAXBException
	 */
	public void loadData(boolean loadTown) throws JAXBException {
		// XML to Object
		loadDataFromXml(loadTown);
		checkDataValid(loadTown);
		// 准备省份
		dealProvince();
		// 城市归类
		DealCity();
		// 县区归类
		dealCountry();
		// 乡镇归类
		dealTown();
	}

	private void dealProvince() {
		provinces.addAll(provinceRecords.r);
		for (Province province : provinces) {
			provincesNoCitys.add(province.name);
		}
	}

	private void dealTown() {
		if (townRecords == null) {
			return;
		}
		for (Town town : townRecords.r) {
			for (County county : countyRecords.r) {
				if (!county.code.equals(town.pcode)) {
					continue;
				}
				if (county.towns == null) {
					county.towns = new ArrayList<Town>();
				}
				town.county = county;
				county.towns.add(town);
			}
		}
	}

	private void dealCountry() {
		for (County county : countyRecords.r) {
			for (City city : cityRecords.r) {
				if (!city.code.equals(county.pcode)) {
					continue;
				}
				if (city.countys == null) {
					city.countys = new ArrayList<County>();
				}
				county.city = city;
				city.countys.add(county);
			}
		}
	}

	private void DealCity() {
		for (City city : cityRecords.r) {
			for (Province province : provinces) {
				if (!province.code.equals(city.pcode)) {
					continue;
				}
				if (province.citys == null) {
					province.citys = new ArrayList<City>();
				}
				city.province = province;
				province.citys.add(city);
			}
		}
	}

	private void loadDataFromXml(boolean loadTown) throws JAXBException {
		provinceRecords = (ProvinceRecords) provinceUnmarshaller
				.unmarshal(this.getClass().getResourceAsStream(provinceFile));
		cityRecords = (CityRecords) cityUnmarshaller.unmarshal(this.getClass().getResourceAsStream(cityFile));
		countyRecords = (CountyRecords) countyUnmarshaller.unmarshal(this.getClass().getResourceAsStream(countryFile));
		if (loadTown) {
			townRecords = (TownRecords) townUnmarshaller.unmarshal(this.getClass().getResourceAsStream(townFile));
		}
	}

	private void checkDataValid(boolean loadTown) {
		// 验证
		if (provinceRecords == null) {
			throw new RuntimeException("找不到文件：province.xml");
		}
		if (cityRecords == null) {
			throw new RuntimeException("找不到文件：city.xml");
		}
		if (countyRecords == null) {
			throw new RuntimeException("找不到文件：county.xml");
		}
		if (loadTown && townRecords == null) {
			throw new RuntimeException("找不到文件：town.xml");
		}
	}

	/**
	 * 根据邮政编码模糊搜索地址集合
	 * 
	 * @param zipcode
	 *            参数规则<br>
	 *            3 个数字：搜索城市列表<br>
	 *            4-5个数字：搜索区县列表<br>
	 *            6 个数字：搜索乡镇列表<br>
	 * @return
	 */
	public List<Address> searchAddressByZipcode(String zipcode) {
		List<Address> list = new ArrayList<Address>();
		if (zipcode != null && zipcode.trim().length() >= 3) {
			if (Pattern.matches("\\d{3}", zipcode)) {
				for (City city : cityRecords.r) {
					if (city.code.startsWith(zipcode)) {
						Address addr = new Address();
						addr.privince = city.province;
						addr.city = city;
						addr.code = city.code;
						addr.name = addr.privince.name + " " + addr.city.name;
						list.add(addr);
					}
				}
			} else if (Pattern.matches("\\d{4,5}", zipcode)) {
				for (County county : countyRecords.r) {
					if (county.code.startsWith(zipcode)) {
						Address addr = new Address();
						addr.privince = county.city.province;
						addr.city = county.city;
						addr.county = county;
						addr.code = county.code;
						addr.name = addr.privince.name + " " + addr.city.name + " " + addr.county.name;
						list.add(addr);
					}
				}
			} else if (Pattern.matches("\\d{6}", zipcode) && townRecords != null) {
				for (Town town : townRecords.r) {
					if (town.code.startsWith(zipcode)) {
						Address addr = new Address();
						addr.privince = town.county.city.province;
						addr.city = town.county.city;
						addr.county = town.county;
						addr.town = town;
						addr.code = town.code;
						addr.name = addr.privince.name + " " + addr.city.name + " " + addr.county.name + " "
								+ addr.town.name;
						list.add(addr);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 搜索省份列表
	 * 
	 * @return 返回的集合只有省份名称
	 */
	public List<String> searchProvinces() {
		return provincesNoCitys;
	}

	/**
	 * 搜索城市列表
	 * 
	 * @param provinceName
	 *            省份名称
	 * @return 返回的集合只有城市名称
	 */
	public List<String> searchCitys(String provinceName) {
		List<String> list = new ArrayList<String>();
		for (Province province : provinces) {
			if (province.name.equals(provinceName)) {
				for (City city : province.citys) {
					list.add(city.name);
				}
				return list;
			}
		}
		return list;
	}

	/**
	 * 搜索县区列表
	 * 
	 * @param provinceName
	 *            省份名称
	 * @param cityName
	 *            城市名称
	 * @return 返回的集合包含邮政编码，但是没有乡镇集合
	 */
	public List<County> searchCountys(String provinceName, String cityName) {
		List<County> list = new ArrayList<County>();
		for (Province province : provinces) {
			if (!province.name.equals(provinceName) || province.citys == null) {
				continue;
			}
			for (City city : province.citys) {
				if (!city.name.equals(cityName) || city.countys == null) {
					continue;
				}
				for (County county : city.countys) {
					County county2 = new County();
					county2.code = county.code;
					county2.name = county.name;
					list.add(county2);
				}
				return list;
			}
		}
		return list;
	}

	/**
	 * 搜索乡镇列表
	 * 
	 * @param provinceName
	 *            省份名称
	 * @param cityName
	 *            城市名称
	 * @param countyName
	 *            县区名称
	 * @return 返回的集合附带邮政编码
	 */
	public List<Town> searchTowns(String provinceName, String cityName, String countyName) {
		List<Town> list = new ArrayList<Town>();
		for (Province province : provinces) {
			if (!province.name.equals(provinceName) || province.citys == null) {
				continue;
			}
			for (City city : province.citys) {
				if (!city.name.equals(cityName) || city.countys == null) {
					continue;
				}
				for (County county : city.countys) {
					if (county.name.equals(countyName)) {
						if (county.towns == null) {
							continue;
						}
						for (Town town : county.towns) {
							Town town2 = new Town();
							town2.code = town.code;
							town2.name = town.name;
							list.add(town2);
						}
						return list;
					}
				}
			}
		}
		return list;
	}

	/**
	 * 查询邮政编码
	 * 
	 * @param provinceName
	 *            省份名称
	 * @param cityName
	 *            城市名称
	 * @param countyName
	 *            【可选】县区名称，模糊搜索
	 * @param townName
	 *            【可选】乡镇名称，模糊搜索
	 * @return 返回第一个邮政编码，优先顺序：精确匹配到的城市、精确匹配到的区县、模糊匹配到的区县、模糊匹配到的乡镇、模糊匹配到的区县。找不到时返回NULL
	 *         。
	 */
	public String searchZipcode(String provinceName, String cityName, String countyName, String townName) {
		if (provinceName == null || provinceName.length() == 0) {
			return null;
		}
		for (Province province : provinces) {
			if (!province.name.equals(provinceName) || province.citys == null) {
				continue;
			}
			if (cityName == null || cityName.length() == 0) {
				return null;
			}
			for (City city : province.citys) {
				if (!city.name.equals(cityName)) {
					continue;
				}
				if ((countyName == null || countyName.length() == 0)
						&& (countyName == null || countyName.length() == 0)) {
					return city.code + "00"; // 精确匹配到城市
				}
				if (city.countys == null) {
					continue;
				}
				if (countyName == null || countyName.length() == 0) {
					return null;
				}
				for (County county : city.countys) {
					if (county.name.equals(countyName)) {
						if (townName == null || townName.length() == 0) {
							return county.code; // 精确匹配到区县
						}
						if (county.towns == null) {
							continue;
						}
						for (Town town : county.towns) {
							if (town.name.indexOf(townName) != -1) {
								return town.code; // 模糊匹配到的乡镇
							}
						}
					}
					if (county.name.indexOf(countyName) != -1) {
						return county.code; // 模糊匹配到的区县
					}
				}
			}
		}
		return null;
	}

	// ///////////////////// XML 结构 //////////////////////////////////////

	@XmlRootElement(name = "provinces")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class ProvinceRecords {

		List<Province> r;

		@Override
		public String toString() {
			return "ProvinceRecords [r=" + r + "]";
		}

	}

	@XmlRootElement(name = "citys")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CityRecords {

		List<City> r;

		@Override
		public String toString() {
			return "CityRecords [r=" + r + "]";
		}

	}

	@XmlRootElement(name = "countys")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CountyRecords {

		List<County> r;

		@Override
		public String toString() {
			return "CountyRecords [r=" + r + "]";
		}

	}

	@XmlRootElement(name = "towns")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TownRecords {

		List<Town> r;

		@Override
		public String toString() {
			return "TownRecords [r=" + r + "]";
		}

	}

	/**
	 * 统一的xml记录格式
	 */
	public static class Record implements Serializable {

		private static final long serialVersionUID = 1L;

		/** 表示父编号，可表示为省份编号、城市邮编、县区邮编 */
		@XmlAttribute(name = "p")
		public String pcode;

		/** 表示省份、城市的编号，或者县区、乡镇的邮政编码 */
		@XmlAttribute(name = "c")
		public String code;

		/** 省份、城市、县区或乡镇的名称，或者单个地址的完整名称 */
		@XmlAttribute(name = "n")
		public String name;

		@Override
		public String toString() {
			return "Record [pcode=" + pcode + ", code=" + code + ", name=" + name + "]";
		}

	}

	/**
	 * 省份
	 */
	public static class Province extends Record {

		private static final long serialVersionUID = 1L;

		/** 城市集合 */
		public List<City> citys;

		@Override
		public String toString() {
			return "Province [pcode=" + pcode + ", code=" + code + ", name=" + name + ", citys=" + citys + "]";
		}

	}

	/**
	 * 城市
	 */
	public static class City extends Record {

		private static final long serialVersionUID = 1L;

		/** 所属省份 */
		private Province province;

		/** 县区集合 */
		public List<County> countys;

	}

	/** 县区 */
	public static class County extends Record {

		private static final long serialVersionUID = 1L;

		/** 所属城市 */
		private City city;

		/** 乡镇集合 */
		public List<Town> towns;

		@Override
		public String toString() {
			return "County [pcode=" + pcode + ", code=" + code + ", name=" + name + ", towns=" + towns + "]";
		}

	}

	/**
	 * 乡镇
	 */
	public static class Town extends Record {

		private static final long serialVersionUID = 1L;

		/** 所属乡镇 */
		private County county;

		/** 返回经过分割后的乡镇集合 */
		public String[] names() {
			if (name == null) {
				return new String[0];
			}
			return name.split("\\|\\|\\|");
		}

	}

	/**
	 * 单个地址，包含省市地区地址邮编详细信息
	 */
	public static class Address extends Record {

		private static final long serialVersionUID = 1L;

		public Province privince;

		public City city;

		public County county;

		public Town town;

	}

}
