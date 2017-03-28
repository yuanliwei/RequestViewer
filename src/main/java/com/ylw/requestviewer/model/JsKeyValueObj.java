package com.ylw.requestviewer.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "js_key_value_obj")
public class JsKeyValueObj {
	@DatabaseField(id = true, unique = true, columnName = "key")
	private String key;
	@DatabaseField(columnName = "value")
	private String value;
	@DatabaseField(columnName = "time")
	private long time;

	public JsKeyValueObj() {
	}

	public JsKeyValueObj(String key2, String value2) {
		this.key = key2;
		this.value = value2;
		this.time = System.currentTimeMillis();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return this.time;
	}
}