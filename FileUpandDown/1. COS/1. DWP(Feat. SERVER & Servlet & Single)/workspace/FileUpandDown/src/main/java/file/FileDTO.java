package file;

public class FileDTO {

	private String fileName;
	private String fileRealName;
	private String extention;
	private long fileSize;
	private int downCount;
	
	public FileDTO() {
		super();
	}

	public FileDTO(String fileName, String fileRealName, String extention, long fileSize, int downCount) {
		super();
		this.fileName = fileName;
		this.fileRealName = fileRealName;
		this.extention = extention;
		this.fileSize = fileSize;
		this.downCount = downCount;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileRealName() {
		return fileRealName;
	}

	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getDownCount() {
		return downCount;
	}

	public void setDownCount(int downCount) {
		this.downCount = downCount;
	}
		
	
}
