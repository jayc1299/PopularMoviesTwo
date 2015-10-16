package com.android.test.popularmoviestwo.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailers {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("iso_639_1")
	@Expose
	private String iso6391;
	@SerializedName("key")
	@Expose
	private String key;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("site")
	@Expose
	private String site;
	@SerializedName("size")
	@Expose
	private Integer size;
	@SerializedName("type")
	@Expose
	private String type;

	/**
	 *
	 * @return
	 * The id
	 */
	public String getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 * The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public Trailers withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 *
	 * @return
	 * The iso6391
	 */
	public String getIso6391() {
		return iso6391;
	}

	/**
	 *
	 * @param iso6391
	 * The iso_639_1
	 */
	public void setIso6391(String iso6391) {
		this.iso6391 = iso6391;
	}

	public Trailers withIso6391(String iso6391) {
		this.iso6391 = iso6391;
		return this;
	}

	/**
	 *
	 * @return
	 * The key
	 */
	public String getKey() {
		return key;
	}

	/**
	 *
	 * @param key
	 * The key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public Trailers withKey(String key) {
		this.key = key;
		return this;
	}

	/**
	 *
	 * @return
	 * The name
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Trailers withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 *
	 * @return
	 * The site
	 */
	public String getSite() {
		return site;
	}

	/**
	 *
	 * @param site
	 * The site
	 */
	public void setSite(String site) {
		this.site = site;
	}

	public Trailers withSite(String site) {
		this.site = site;
		return this;
	}

	/**
	 *
	 * @return
	 * The size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 *
	 * @param size
	 * The size
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	public Trailers withSize(Integer size) {
		this.size = size;
		return this;
	}

	/**
	 *
	 * @return
	 * The type
	 */
	public String getType() {
		return type;
	}

	/**
	 *
	 * @param type
	 * The type
	 */
	public void setType(String type) {
		this.type = type;
	}

	public Trailers withType(String type) {
		this.type = type;
		return this;
	}
}