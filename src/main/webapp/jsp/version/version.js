var service = {
	serviceId : function(value, row, index) {
		try {
			return row.service.serviceId
		} catch (exception) {
		}
	},
	serviceName : function(value, row, index) {
		try {
			return row.service.serviceName
		} catch (exception) {
		}
	}
};
var version = {
	code : function(value, row, index) {
		try {
			return row.version.code
		} catch (exception) {
		}
	},
	type : function(value, row, index) {
		try {
			if (value == "0")
				return "否";
			if (value == "1")
				return "是";
		} catch (exception) {
		}
	},
	versionDesc : function(value, row, index) {
		try {
			return row.version.versionDesc
		} catch (exception) {
		}
	},
	optType0 : function(value, row, index) {
		try {
			if (value == "0")
				return "新增";
			if (value == "1")
				return "修改";
			if (value == "2")
				return "删除";
			if (value == "3")
				return "发布";
		} catch (exception) {
		}
	},
	optType : function(value, row, index) {
		try {
			if (row.version.optType == "0")
				return "新增";
			if (row.version.optType == "1")
				return "修改";
			if (row.version.optType == "2")
				return "删除";
			if (value == "3")
				return "发布";
		} catch (exception) {
		}
	},
	optUser : function(value, row, index) {
		try {
			return row.version.optUser
		} catch (exception) {
		}
	},
	optDate : function(value, row, index) {
		try {
			return row.version.optDate
		} catch (exception) {
		}
	}
}
var versionHis = {
	autoId : function(value, row, index) {
		try {
			return row.versionHis.autoId
		} catch (exception) {
		}
	},
	code : function(value, row, index) {
		try {
			return row.versionHis.code
		} catch (exception) {
		}
	},
	type : function(value, row, index) {
		try {
			if (value == "0")
				return "否";
			if (value == "1")
				return "是";
		} catch (exception) {
		}
	},
	versionDesc : function(value, row, index) {
		try {
			return row.versionHis.versionDesc
		} catch (exception) {
		}
	},
	optType0 : function(value, row, index) {
		try {
			if (value == "0")
				return "新增";
			if (value == "1")
				return "修改";
			if (value == "2")
				return "删除";
		} catch (exception) {
		}
	},
	optType : function(value, row, index) {
		try {
			if (row.versionHis.optType == "0")
				return "新增";
			if (row.versionHis.optType == "1")
				return "修改";
			if (row.versionHis.optType == "2")
				return "删除";
		} catch (exception) {
		}
	},
	optUser : function(value, row, index) {
		try {
			return row.versionHis.optUser
		} catch (exception) {
		}
	},
	optDate : function(value, row, index) {
		try {
			return row.versionHis.optDate
		} catch (exception) {
		}
	}
}
var invoke = {
	interfaceName : function(value, row, index) {
		try {
			return row.inter.interfaceName;
		} catch (exception) {
		}
	},
	systemChineseName : function(value, row, index) {
		try {
			return row.system.systemChineseName;
		} catch (exception) {
		}
	},
	isStandardText : function(value, row, index) {
		if ("0" == value) {
			return "是";
		} else {
			return "否";
		}
	},
	typeText : function(value, row, index) {
		if ("1" == value) {
			return "消费者";
		}
		if ("0" == value) {
			return "提供者";
		}
	}

}