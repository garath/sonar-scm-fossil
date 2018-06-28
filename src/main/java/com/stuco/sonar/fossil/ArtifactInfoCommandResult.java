package com.stuco.sonar.fossil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArtifactInfoCommandResult {
	private String uuidHash;
	private Date uuidDate;
	private String parentHash;
	private Date parentDate;
	private String childHash;
	private Date childDate;
	private List<String> tags = new ArrayList<>();
	private String comment;
	private String user;

	public String getUuidHash() {
		return uuidHash;
	}

	public Date getUuidDate() {
		return uuidDate;
	}

	public String getParentHash() {
		return parentHash;
	}

	public Date getParentDate() {
		return parentDate;
	}

	public String getChildHash() {
		return childHash;
	}

	public Date getChildDate() {
		return childDate;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getComment() {
		return comment;
	}

	public String getUser() {
		return user;
	}

	void setUuidHash(String uuidHash) {
		this.uuidHash = uuidHash;
	}

	void setUuidDate(Date uuidDate) {
		this.uuidDate = uuidDate;
	}

	void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}

	void setParentDate(Date parentDate) {
		this.parentDate = parentDate;
	}

	void setChildHash(String childHash) {
		this.childHash = childHash;
	}

	void setChildDate(Date child) {
		this.childDate = child;
	}

	void setTags(List<String> tags) {
		this.tags = tags;
	}

	void setComment(String comment) {
		this.comment = comment;
	}

	void setUser(String user) {
		this.user = user;
	}

}
