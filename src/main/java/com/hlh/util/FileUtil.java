package com.hlh.util;

/**
 * 文件处理
 */

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @version V1.0
 * @ClassName FileUtil
 * @Description
 * @Author zhangyue
 * @Date 2018/1/9 20:58
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * byte[] 转InputStream
     */
    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    /**
     * InputStream 转 byte[]
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * byte[] 转 InputStreamReader
     */
    public static final InputStreamReader byte2Reader(byte[] buf) {
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(buf));
        return isr;
    }

    /**
     * 删除文件
     *
     * @param pathname 文件名（包括路径）
     */
    public static void deleteFile(String pathname) {

        File file = new File(pathname);
        if (file.isFile() && file.exists()) {
            file.delete();
        } else {
            logger.error("File[" + pathname + "] not exists!");
        }
    }

    /**
     * 迭代删除文件夹
     *
     * @param dirPath 文件夹路径
     */
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        deleteDir(file);
    }

    /**
     * 迭代删除文件夹
     *
     * @param file 文件句柄
     */
    public static void deleteDir(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    /**
     * 删除文件树
     *
     * @param dirpath 文件夹路径
     */
    public static void deleteFileTree(String dirpath) throws IOException {

        File dir = new File(dirpath);
        FileUtils.deleteDirectory(dir);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /**
     * 获取文件分隔符
     *
     * @return
     */
    public static String getFileSeparator() {
        return File.separator;
    }

    /**
     * 获取相对路径
     *
     * @param params 按参数先后位置得到相对路径
     * @return
     */
    public static String getRelativePath(String... params) {

        if (null != params) {
            String path = "";
            for (String str : params) {
                path = path + getFileSeparator() + str;
            }

            return path;
        }

        return null;
    }

    /**
     * 把一个字符串写到指定文件中
     *
     * @param str      要写入文件中的字符串内容
     * @param path     文件夹路径
     * @param fileName 文件名称
     */
    public static void writeStringToFile(String str, String path, String fileName) throws IOException {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(path + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, true);
        fw.write(str);
        fw.flush();
        fw.close();
    }

    /**
     * 在某个文件中追加内容
     *
     * @param fileName
     * @param content
     */
    public static void appendStringToFile(String fileName, String content) {
        try {
            //判断文件是否存在
            File file = new File(fileName);
            isFileExists(file);
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write((content + "\r\n").getBytes());
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 判断文件是否存在,如果不存在则创建
    public static void isFileExists(File file) {
        if (file.exists()) {
        } else {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 判断文件夹是否存在，如果不存在则创建
    public static void isDirExists(File file) {
        if (file.exists()) {
            if (!file.isDirectory()) {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 根据文件路径获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileHeader(File filePath) {
        FileInputStream is = null;
        String value = "";
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[20];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头十六进制信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        System.out.println("HexString: " + builder.toString());
        return builder.toString();
    }


    /**
     * 复制文件
     *
     * @param source 源文件
     * @param dest   新文件
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
