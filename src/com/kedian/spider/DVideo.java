package com.kedian.spider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wzh
 * @description
 * @create 2019-06-27 20:51
 */
public class DVideo {

    public static void main(String[] args) {
        for (int i =0; i < 1000; i++) {
            String url="https://";
                    //
            String desc="";
            if (i<10){
                 url=url+"00"+i+".ts";
                 desc="C:\\Users\\Administrator\\Desktop\\video\\00000"+i+".ts";
            }else if (i>=10&&i<100){
                url=url+"0"+i+".ts";
                desc="C:\\Users\\Administrator\\Desktop\\video\\0000"+i+".ts";

            }else if (i>=100&&i<999){
                url=url+""+i+".ts";
                desc="C:\\Users\\Administrator\\Desktop\\video\\000"+i+".ts";
            }else {
                url=url+""+i+".ts";
                desc="C:\\Users\\Administrator\\Desktop\\video\\00"+i+".ts";
            }
            getPageContent(url,desc);
        }
    }

    public static void getPageContent(String url,String desc)  {
        InputStream is=null;
        InputStreamReader isr =null;
        FileOutputStream os=null;
        BufferedReader br=null;
        BufferedOutputStream buf=null;
        try {
            // 建立连接
            URL u = new URL(url);
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            HttpURLConnection httpUrlConn = (HttpURLConnection) u.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 获取输入流
            is = httpUrlConn.getInputStream();
            // 将字节输入流转换为字符输入流
            isr = new InputStreamReader(is, "utf-8");
            os = new FileOutputStream(new File(desc));
            buf=new BufferedOutputStream(os);
            // 为字符输入流添加缓冲
            br = new BufferedReader(isr);
            // 读取返回结果
            byte[] b = new byte[2048];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                buf.write(b, 0, len);
            }
            buf.flush();//刷新缓冲区，即把内容写入
            // 释放资源
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (buf!=null){
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("完成："+desc);
    }

}
