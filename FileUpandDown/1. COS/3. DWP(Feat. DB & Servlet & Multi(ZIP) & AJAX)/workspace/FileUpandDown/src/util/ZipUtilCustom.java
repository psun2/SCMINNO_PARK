package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

public class ZipUtilCustom {
	
	private String root;
	private String uploadRoot;
	
	private String downloadRoot;
	private String downloadPath;
	private String downloadZipPath;
	
	private final static String zipFileName = "download.zip";
	
	private FileOutputStream fileOutputStream = null;
	private ZipOutputStream zipOutputStream = null;
	
	private String[] fileNames;
	
	public ZipUtilCustom(HttpServletRequest request) {
		try {
			this.root = request.getServletContext().getRealPath("/");
			this.uploadRoot = this.root + "upload";
			this.downloadRoot = this.root + "download";
			this.downloadPath = this.downloadRoot + File.separator + System.currentTimeMillis();
			
			File downloadCur = new File(this.downloadPath);
			if(!downloadCur.exists()) downloadCur.createNewFile();
			
			this.downloadZipPath = this.downloadPath + File.separator + this.zipFileName;
			File downloadZip = new File(downloadZipPath);
			if(!downloadZip.exists()) downloadZip.createNewFile();
			
			this.fileNames = new String[0];
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end ZipUtilCustom()
	
	public void compare(File fileOrDirectory) {
		try {
			searchDirectory(fileOrDirectory);
			
			close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end compare()
	
	private void searchDirectory(File directory) {
		if(directory.exists()) {
			if(directory.isDirectory()) {
				File[] files = directory.listFiles();
				for(File file : files) {
					searchDirectory(file);
				}
			} else if(directory.isFile()) {
				compressZip(directory);
			}
		}
	} // end searchDirectory()
	
	private void compressZip(File file) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			
			// 파일명 중복 검사
			String fileName = file.getName();
			fileName = overlap(fileName);
			
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = fileInputStream.read(buffer, 0, buffer.length)) >= 0) {
				zipOutputStream.write(buffer, 0, read);
			}
			zipOutputStream.flush();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(fileInputStream != null)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	} // end compressZip()
	
	private String overlap(String fileName) {
		try {
			pushArray(fileName);
			
			boolean overlap = false;
			int index = -1;
			String overlapFileName = "";
			
			for(int i = 0; i < this.fileNames.length; i++) {
				for(int j = 0; j < this.fileNames.length; j++) {
					if(this.fileNames[i].equals(this.fileNames[j])) {
						overlap = true;
						index = i;
						overlapFileName = this.fileNames[i];
						break;
					}
				}
			}
			
			if(overlap) {
				fileName = overlapFileName + "_" + System.currentTimeMillis();
				this.fileNames[index] = fileName;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return fileName;
	} // end overlap()
	
	private void pushArray(String fileName) {
		String[] temp = new String[this.fileNames.length + 1];
		
		for(int i = 0; i < this.fileNames.length; i++) {
			temp[i] = this.fileNames[i];
		}
		temp[temp.length - 1] = fileName;
		this.fileNames = temp;
	} // end pushArray()
	
	private void close() {
		try {
			if(zipOutputStream != null) {
				zipOutputStream.flush();
				zipOutputStream.close();
			}
			if(fileOutputStream != null) fileOutputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end close()

	public void downloadTempDelete() {
		try {
			String targetName = "";
			String targetPath = this.downloadZipPath;
			while(true) {
				File file = new File(targetPath);
				if(file.exists()) file.delete();
				
				targetPath = targetPath.substring(0, targetPath.lastIndexOf("/"));
				targetName = targetPath.substring(targetPath.lastIndexOf("/") + 1);
				if(targetName.equals("download")) break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // end downloadTempDelete()
}
