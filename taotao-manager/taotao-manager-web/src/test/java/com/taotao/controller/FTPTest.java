package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.junit.Test;

import com.taotao.common.utils.FtpUtil;

public class FTPTest {
	@Test
	public void testFtpClient() throws Exception {
		// create Ftpclient object
		FTPClient ftpClient = new FTPClient();
		// connect ftp
		ftpClient.connect("10.0.60.169", 21);
		// login and password
		ftpClient.login("ftpuser", "ftpuser");
		// upload docs
		// configure upload path
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		// image is binary, so we need this!!!
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		// read a local file
		FileInputStream inputStream = new FileInputStream(
				new File("C:\\Users\\bingyang.wei\\Desktop\\Singing_Rabbit.jpg"));
		ftpClient.storeFile("hello1.jpg", inputStream);
		// close connection
		ftpClient.logout();
	}

	@Test
	public void testFtpUtil() throws Exception {
		FileInputStream inputStream = new FileInputStream(
				new File("C:\\Users\\bingyang.wei\\Desktop\\Singing_Rabbit.jpg"));
		FtpUtil.uploadFile("10.0.60.169", 21, "ftpuser", "ftpuser", "/home/ftpuser/www/images", "/2018/02/01",
				"bing.jpg", inputStream);

	}
}
