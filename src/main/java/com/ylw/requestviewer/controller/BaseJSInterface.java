package com.ylw.requestviewer.controller;

import com.ylw.common.utils.ormliteutils.OrmLiteUtils;
import com.ylw.requestviewer.model.JsKeyValueObj;

public class BaseJSInterface {
	public void saveData(String key, String value) {
		OrmLiteUtils.saveOrUpdate(new JsKeyValueObj(key, value));
	}

	public String loadData(String key) {
		JsKeyValueObj obj = OrmLiteUtils.queryForSameId(new JsKeyValueObj(key, ""));
		if (obj==null) {
			return null;
		}
		return obj.getValue();
	}
}
