package com.logx.utils;

public enum Unit {

	BIT("bit") {
		public long getBytes(int value) {
			return (long) (value / 8);
		}
	},
	BYTE("B") {
		public long getBytes(int value) {
			return value;
		}
	},
	KB("K") {
		public long getBytes(int value) {
			return value * 1024L;
		}
	},
	MB("M") {
		public long getBytes(int value) {
			return KB.getBytes(value) * 1024L;
		}
	},
	GB("G") {
		public long getBytes(int value) {
			return MB.getBytes(value) * 1024L;
		}
	},
	TB("T") {
		public long getBytes(int value) {
			return GB.getBytes(value) * 1024L;
		}
	};
	private String desc;

	/**
	 * 构造器
	 * 
	 * @param desc单位描述
	 */
	private Unit(String desc) {
		this.desc = desc;
	}

	/**
	 * 解析数据
	 */
	public static UnitPojo parser(String str) {
		try {
			str = str.trim();
			char[] data = str.toCharArray();
			StringBuilder numBuild = new StringBuilder(data.length);
			String unit = "";
			for (int i = 0; i < data.length; i++) {
				if (data[i] >= '0' && data[i] <= '9') {
					numBuild.append(data[i]);
					continue;
				} else {
					unit = str.substring(i);
					break;
				}
			}
			int num = Integer.parseInt(numBuild.toString());
			Unit unitType = parserUnit(unit);
			return new UnitPojo(num, unitType);
		} catch (Exception ex) {
		}
		throw new IllegalArgumentException("无效参数 `" + str + "`");
	}

	/**
	 * 验证是否匹配当前单位
	 **/
	public boolean matches(String unit) {
		if (this.desc.equalsIgnoreCase(unit)) {
			return true;
		}
		return false;
	}

	/**
	 * 解析单位
	 * 
	 * @param unit
	 * @return
	 */
	public static Unit parserUnit(String unit) {
		for (Unit u : Unit.values()) {
			if (u.matches(unit.trim())) {
				return u;
			}
		}
		if (unit.trim().length() == 0) {
			return Unit.BYTE;// 不设置单位，默认使用byte
		}
		throw new IllegalArgumentException("不支持的参数`" + unit + "`");
	}

	/**
	 * 根据单位value转换到byte
	 * 
	 * @param value
	 *            单位有Unit type确定
	 */
	public abstract long getBytes(int value);

	public static class UnitPojo {
		public UnitPojo(int num, Unit b) {
			this.value = num;
			this.unit = b;
		}

		public Unit unit;
		public int value;

		@Override
		public String toString() {
			return value + "->" + unit;
		}

		public long getBytes() {
			return unit.getBytes(value);
		}
	}
}
