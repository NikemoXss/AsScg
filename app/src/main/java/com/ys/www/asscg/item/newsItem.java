package com.ys.www.asscg.item;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * 新闻子界面
 */
public class newsItem {
	private long id;
	private String name;
	private String value;
	private String time;

	public newsItem() {

	}

	public newsItem(JSONObject json) {
		try {
			if (json.has("id")) {
				setId(json.getLong("id"));
			}

			if (json.has("title")) {
				setName(json.getString("title"));
			}

			if (json.has("art_time")) {
				setTime(json.getString("art_time"));
			}

			if (json.has("art_content")) {
				setValue(json.getString("art_content"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
