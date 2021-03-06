/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.viperfish.chatapplication.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.glassfish.grizzly.websockets.WebSocket;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * a request sent from the client to the server. The client request would be in
 * the JSON format on arrival to the server. The response would then be passed
 * through filters and be processed by a {@link RequestHandler}. This class is
 * not designed for thread safety.
 * 
 * @author sdai
 */
public class LSRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4397309950514696170L;
	/**
	 * Login request
	 */
	public static final long LS_LOGIN = 1;
	/**
	 * Chat message request
	 */
	public static final long LS_MESSAGE = 2;
	/**
	 * Get associates request
	 */
	public static final long LS_ASSOCIATE_LOOKUP = 3;
	/**
	 * Add associate request
	 */
	public static final long LS_ADD_ASSOCIATE = 4;
	/**
	 * Look up user request
	 */
	public static final long LS_LOOKUP_USER = 5;
	/**
	 * Delete associate request
	 */
	public static final long LS_DELETE_ASSOCIATE = 6;
	/**
	 * Get public key request
	 */
	public static final long LS_LOOKUP_KEY = 7;

	/**
	 * Retrieve unsent message request
	 */
	public static final long LS_RETRIEVE_UNSENT = 8;

	private String source;
	private Map<String, String> attributes;
	private Date timeStamp;
	private Long type;
	private String data;
	private transient WebSocket socket;

	public LSRequest(String from, Map<String, String> attributes, Date timeStamp, Long type, String data,
			WebSocket sock) {
		this.source = from;
		this.attributes = attributes;
		this.timeStamp = timeStamp;
		this.type = type;
		this.data = data;
		this.socket = sock;
	}

	public LSRequest() {
		source = "anonymous";
		attributes = new HashMap<>();
		timeStamp = new Date();
		type = 0L;
		data = "";
		socket = null;
	}

	public void setSource(String from) {
		this.source = from;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setSocket(WebSocket socket) {
		this.socket = socket;
	}

	public String getSource() {
		return this.source;
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

	public Date getTimeStamp() {
		return this.timeStamp;
	}

	public String getData() {
		return this.data;
	}

	@JsonIgnore
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.data.getBytes(StandardCharsets.UTF_8));
	}

	public Long getType() {
		return type;
	}

	@JsonIgnore
	public Long getContentLenth() {
		return (long) data.getBytes(StandardCharsets.UTF_8).length;
	}

	@JsonIgnore
	public Set<String> getAttributeNames() {
		Set<String> result = new HashSet<>();
		attributes.entrySet().stream().forEach((e) -> {
			result.add(e.getKey());
		});
		return result;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	@JsonIgnore
	public WebSocket getSocket() {
		return this.socket;
	}

	@JsonIgnore
	public LSSession getSession() {
		return DefaultLSSession.getSession(source);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.source);
		hash = 97 * hash + Objects.hashCode(this.attributes);
		hash = 97 * hash + Objects.hashCode(this.timeStamp);
		hash = 97 * hash + Objects.hashCode(this.type);
		hash = 97 * hash + Objects.hashCode(this.data);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LSRequest other = (LSRequest) obj;
		if (!Objects.equals(this.source, other.source)) {
			return false;
		}
		if (!Objects.equals(this.data, other.data)) {
			return false;
		}
		if (!Objects.equals(this.attributes, other.attributes)) {
			return false;
		}
		if (!Objects.equals(this.timeStamp, other.timeStamp)) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		return true;
	}

}
