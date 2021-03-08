package com.mango.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类，用于Web层文件的上传与下载等
 *
 */
@Slf4j
public class WebFileUtil {

    private static final char DOT = '.';

    private static final char FILE_SEPARATOR = '/';

    private WebFileUtil() {

    }

    /**
     * 根据文件全路径名获取文件名
     *
     * @param absoluteFileName 全路径名
     * @return 文件名
     */
    public static String getFileName(String absoluteFileName) {
        return absoluteFileName.substring(absoluteFileName.lastIndexOf(FILE_SEPARATOR) + 1);
    }

    /**
     * 得到文件名（不包含后缀）
     *
     * @param fileName 文件名
     * @return 文件名（不包含后缀）
     */
    public static String getFileNameNoSuffix(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            int dot = fileName.lastIndexOf(DOT);
            if (dot > -1) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    /**
     * 得到文件扩展名（不带点）
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtNoDot(String fileName) {
        return StringUtils.isBlank(fileName) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 得到文件扩展名（带点）
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtWithDot(String fileName) {
        return StringUtils.isBlank(fileName) ? "" : fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 判断客户端浏览器类型是否为IE
     *
     * @param request 请求对象
     * @return IE浏览器返回true，其他浏览器返回false
     */
    private static boolean isMsIe(HttpServletRequest request) {
        boolean isMsIe = false;
        String userAgent = request.getHeader("USER-AGENT");
        if (StringUtils.isNotBlank(userAgent)) {
            isMsIe = userAgent.contains("MSIE") || userAgent.contains("Trident");
        }
        return isMsIe;
    }

    /**
     * 解决IE、Chrome和Firfox下文件名乱码问题
     *
     * @param request  请求对象
     * @param fileName 客户端下载后的文件名
     * @return 编码处理后的文件名
     * @throws UnsupportedEncodingException 编码异常
     */
    private static String processFileName(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        // 获取浏览器类型
        boolean isIe = isMsIe(request);
        if (isIe) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return fileName;
    }

    /**
     * 文件下载
     *
     * @param request 请求对象
     * @param file    要下载的文件对象
     * @return 文件下载响应实例
     * @throws IOException IO异常
     */
    public static ResponseEntity<byte[]> downloadFile(HttpServletRequest request, File file) throws IOException {
        String fileName = processFileName(request, file.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param request 请求对象
     * @param bytes   字节数组
     * @param dlName  客户端下载后的文件名
     * @return 文件下载响应实例
     * @throws UnsupportedEncodingException 编码异常
     */
    public static ResponseEntity<byte[]> downloadFile(HttpServletRequest request, byte[] bytes, String dlName)
            throws UnsupportedEncodingException {
        String fileName = processFileName(request, dlName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param request 请求对象
     * @param file    要下载的文件对象
     * @param dlName  客户端下载后的文件名
     * @return 文件下载响应实例
     * @throws IOException IO异常
     */
    public static ResponseEntity<byte[]> downloadFile(HttpServletRequest request, File file, String dlName)
            throws IOException {
        String fileName = processFileName(request, dlName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 压缩文件
     *
     * @param bytesMaps 字节数据Map
     * @return 字节数据
     */
    public static byte[] zipBytes(Map<String, byte[]> bytesMaps) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipos = new ZipOutputStream(bos);
        for (Map.Entry<String, byte[]> e : bytesMaps.entrySet()) {
            ZipEntry entry = new ZipEntry(e.getKey());
            entry.setSize(e.getValue().length);
            zipos.putNextEntry(entry);
            zipos.write(e.getValue());
            zipos.closeEntry();
        }
        zipos.close();
        byte[] zipBytes = bos.toByteArray();
        bos.close();
        return zipBytes;
    }

    /**
     * 压缩文件
     *
     * @param fileMaps 文件对象Map
     * @param baseDir  文件目录
     * @param zipName  ZIP包文件名
     * @return File文件对象
     */
    public static File zipFile(Map<String, File> fileMaps, String baseDir, String zipName) {
        String directory = "";
        String suffix = "/";

        if (!baseDir.endsWith(suffix)) {
            directory = baseDir + suffix;
        }

        File dir = new File(directory);
        if (!dir.exists() && dir.mkdirs()) {
            log.warn("文件目录不存在");
        }
        File file = new File(directory + zipName);
        if (file.exists() && file.delete()) {
            log.warn("文件已存在");
        }

        byte[] buffer = new byte[1024];
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file))) {
            for (Map.Entry<String, File> e : fileMaps.entrySet()) {
                try (FileInputStream fis = new FileInputStream(e.getValue())) {
                    out.putNextEntry(new ZipEntry(e.getKey()));
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.closeEntry();
                } catch (Exception e1) {
                    log.error("读取文件异常", e1);
                }
            }
        } catch (Exception ex) {
            log.error("ZIP文件压缩异常", ex);
        }
        return file;
    }

}
