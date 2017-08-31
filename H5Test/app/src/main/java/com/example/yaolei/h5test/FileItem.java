package com.example.yaolei.h5test;

import java.io.File;

public class FileItem {

	private File file;
	private String fileName;
	public FileItem() {
	}
	public FileItem(File file) {
		this.file = file;
	}
	public FileItem(File file, String fileName) {
		setFile(file);
		setFileName(fileName);
	}
	public void setFile(File file) {
		this.file = file;
	}
	public File getFile() {
		return file;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
}
