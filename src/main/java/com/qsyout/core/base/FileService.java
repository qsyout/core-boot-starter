package com.qsyout.core.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	File makeFile(String path,String fileName);
	
	File saveFile(String path,String fileName,MultipartFile file) throws IllegalStateException, IOException;
	
	File saveFile(String path,String fileName,InputStream is) throws IOException;
	
	File getFile(String path,String fileName) throws IOException;
	
	File getFile(String filePath) throws IOException;
	
	boolean delFile(String path,String fileName);
}
