package DTO;

import java.sql.Time;
import java.util.Date;

public class FileDownDTO {
	
	private int seq;
	private String fileNames;
	private String downLoadUser;
	private Date downLoadDate;
	private Time downLoadTime;
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getFileNames() {
		return fileNames;
	}
	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}
	public String getDownLoadUser() {
		return downLoadUser;
	}
	public void setDownLoadUser(String downLoadUser) {
		this.downLoadUser = downLoadUser;
	}
	public Date getDownLoadDate() {
		return downLoadDate;
	}
	public void setDownLoadDate(Date downLoadDate) {
		this.downLoadDate = downLoadDate;
	}
	public Time getDownLoadTime() {
		return downLoadTime;
	}
	public void setDownLoadTime(Time downLoadTime) {
		this.downLoadTime = downLoadTime;
	}

}
