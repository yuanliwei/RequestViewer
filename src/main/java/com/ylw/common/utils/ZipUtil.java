package com.ylw.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.util.IOUtils;
import com.ylw.common.utils.digest.Hex;

public class ZipUtil {

	private static Log log = LogFactory.getLog(ZipUtil.class);

	/**
	 * zip压缩文件
	 * 
	 * @param dir
	 * @param zippath
	 */
	public static void zip(String dir, String zippath) {
		try {
			FileUtil.delete(zippath);
			File zipFile = new File(zippath);
			Files.createDirectories(Paths.get(zipFile.getParent()));
			OutputStream out;
			out = new BufferedOutputStream(new FileOutputStream(new File(zippath)));
			ZipArchiveOutputStream ozip = (ZipArchiveOutputStream) new ArchiveStreamFactory("utf-8")
					.createArchiveOutputStream("zip", out);
			Path path = Paths.get(dir);
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Path fileName = file.subpath(path.getNameCount(), file.getNameCount());
					ArchiveEntry archiveEntry = new ZipArchiveEntry(fileName.toString());
					InputStream input = new FileInputStream(file.toFile());
					ozip.putArchiveEntry(archiveEntry);
					org.apache.commons.compress.utils.IOUtils.copy(input, ozip, 2048);
					input.close();
					ozip.closeArchiveEntry();
					return FileVisitResult.CONTINUE;
				}
			});
			ozip.close();
		} catch (IOException | ArchiveException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *            zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *            解压后的文件存放路径, 如"D:/test/" ()
	 */
	public static void unzip(String zipFilePath, String saveFileDir) {
		try {
			// Path path = Paths.get(saveFileDir);
			File dir = new File(saveFileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(zipFilePath);
			if (file.exists()) {
				InputStream is = null;
				ZipArchiveInputStream zais = null;
				try {
					is = new FileInputStream(file);
					zais = new ZipArchiveInputStream(is);
					ArchiveEntry archiveEntry = null;
					while ((archiveEntry = zais.getNextEntry()) != null) {
						// 获取文件名
						String entryFileName = archiveEntry.getName();
						// 构造解压出来的文件存放路径
						String entryFilePath = saveFileDir + entryFileName;
						OutputStream os = null;
						try {
							// 把解压出来的文件写到指定路径
							File entryFile = new File(entryFilePath);
							if (entryFileName.endsWith("/")) {
								entryFile.mkdirs();
							} else {
								os = new BufferedOutputStream(new FileOutputStream(entryFile));
								byte[] buffer = new byte[1024];
								int len = -1;
								while ((len = zais.read(buffer)) != -1) {
									os.write(buffer, 0, len);
								}
							}
						} catch (IOException e) {
							throw new IOException(e);
						} finally {
							if (os != null) {
								os.flush();
								os.close();
							}
						}

					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					IOUtils.close(zais);
					IOUtils.close(is);
				}
			} 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String longStringData = "longTestStringData";
		String out = gzip(longStringData);
		System.out.println("data:" + longStringData);
		System.out.println("out1:" + out);
		System.out.println("out2:" + new String(gunzip(out.getBytes())));
		System.out.println("data length:" + longStringData.length());
		System.out.println(" out length:" + out.length());
	}

	/**
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(gzip);
		}
		return Hex.bytesToHexString(out.toByteArray());
	}

	/**
	 * <p>
	 * Description:使用gzip进行解压缩
	 * </p>
	 *
	 * @param compressed
	 * @return
	 * @throws Exception
	 */
	public static byte[] gunzip(byte[] compressed) throws Exception {
		if (compressed == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] decompressed = null;
		try {
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toByteArray();
		} catch (IOException e) {
			log.error(e);
		} finally {
			IOUtils.close(ginzip);
			IOUtils.close(in);
			IOUtils.close(out);
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 *
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = Hex.bytesToHexString(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			IOUtils.close(zout);
			IOUtils.close(out);
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 *
	 * @param compressedStr
	 *            压缩后的文本
	 * @return 解压后的字符串
	 * @throws Exception
	 */
	public static String unzip(String compressedStr) throws Exception {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = Hex.hexStringToBytes(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			IOUtils.close(zin);
			IOUtils.close(in);
			IOUtils.close(out);
		}
		return decompressed;
	}

}