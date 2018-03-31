package com.pap.nuts.modules.model.beans;

import java.time.LocalDate;

import com.pap.nuts.modules.interfaces.AbstractBean;

public class FeedVersion extends AbstractBean {
	
	private long feedId;
	private String title;
	private LocalDate LatestVersion;
	
	
	public long getFeedId() {
		return feedId;
	}
	public void setFeedId(long feedId) {
		this.feedId = feedId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public LocalDate getLatestVersion() {
		return LatestVersion;
	}
	public void setLatestVersion(LocalDate latestVersion) {
		LatestVersion = latestVersion;
	}
	
	@Override
	public String toString() {
		return "FeedVersion [feedId=" + feedId + ", title=" + title + ", LatestVersion=" + LatestVersion + "]";
	}
	
}
