package com.qsyout.core.base.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.qsyout.core.base.FileService;

public class LocaleFileService implements FileService {

	private String basePath;

	@Override
	public File makeFile(String path, String fileName) {
		return new File(notExistFolder(path), fileName);
	}

	@Override
	public File saveFile(String path, String fileName, MultipartFile file) throws IllegalStateException, IOException {
		File dest = makeFile(path, fileName);
		file.transferTo(dest);
		return dest;
	}
	
	@Override
	public File getFile(String filePath) throws IOException {
		File file = new File(excutePath(filePath));
		
		if(!file.exists()){
			throw new IOException("指定文件不存在!");
		}
		
		return file;
	}

	@Override
	public File getFile(String path, String fileName) throws IOException {
		return getFile(path + "/" + fileName);
	}

	@Override
	public boolean delFile(String path, String fileName) {
		try {
			return getFile(path, fileName).delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private File notExistFolder(String path){
		File parent = new File(excutePath(path));

		if (!parent.exists()) {
			parent.mkdirs();
		}
		
		return parent;
	}
	
	public String excutePath(String path) {
		return this.basePath + "/" + path;
	}

	public LocaleFileService(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public File saveFile(String path, String fileName, InputStream is) throws IOException {
		File dest = makeFile(path, fileName);
		OutputStream os = new FileOutputStream(dest);
		
		byte[] b = new byte[1024];
		
		int count = 0;
        while((count=is.read(b))!=-1){
            os.write(b,0,count);
        }
        
        os.flush();
        os.close();
        is.close();
        
		return dest;
	}

}
