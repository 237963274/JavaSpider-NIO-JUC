package com.kedian.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.nio
 * @Description:
 * @date 2019/7/23
 */
public class PathAndFiles {

    /*
    Paths 提供的 get() 方法用来获取 Path 对象：
        Path get(String first, String … more) : 用于将多个字符串串连成路径。
    Path 常用方法：
        boolean endsWith(String path) : 判断是否以 path 路径结束
        boolean startsWith(String path) : 判断是否以 path 路径开始
        boolean isAbsolute() : 判断是否是绝对路径
        Path getFileName() : 返回与调用 Path 对象关联的文件名
        Path getName(int idx) : 返回的指定索引位置 idx 的路径名称
        int getNameCount() : 返回Path 根目录后面元素的数量
        Path getParent() ：返回Path对象包含整个路径，不包含 Path 对象指定的文件路径
        Path getRoot() ：返回调用 Path 对象的根路径
        Path resolve(Path p) :将相对路径解析为绝对路径
        Path toAbsolutePath() : 作为绝对路径返回调用 Path 对象
        String toString() ： 返回调用 Path 对象的字符串表示形式
	 */
    @Test
    public void testPath() {
        Path path = Paths.get("e:/", "nio/hello.txt");
        System.out.println(path.endsWith("hello.txt"));
        System.out.println(path.startsWith("e:/"));
        System.out.println(path.isAbsolute());
        System.out.println(path.getFileName());
        System.out.println(path.getNameCount());
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println(path.getName(i));
        }
        System.out.println(path.getParent());
        System.out.println(path.getRoot());

        Path path2=path.resolve("e:/hello.txt");
        System.out.println(path2);

        Path path3=Paths.get("1.jpeg");
        Path path4 = path3.toAbsolutePath();
        System.out.println(path4);
    }

    /*
    Files常用方法：
        Path copy(Path src, Path dest, CopyOption … how) : 文件的复制
        Path createDirectory(Path path, FileAttribute<?> … attr) : 创建一个目录
        Path createFile(Path path, FileAttribute<?> … arr) : 创建一个文件
        void delete(Path path) : 删除一个文件
        Path move(Path src, Path dest, CopyOption…how) : 将 src 移动到 dest 位置
        long size(Path path) : 返回 path 指定文件的大小
	 */
    @Test
    public void testFiles() throws IOException {
        Path dir=Paths.get("e:/nio/");
        Files.createDirectory(dir);

        Path file=Paths.get("e:/nio/hello.txt");
        Files.createFile(file);

        Path path1=Paths.get("e:/nio/hello.txt");
        Path path2=Paths.get("e:/nio/hello2.txt");
        Files.copy(path1, path2,StandardCopyOption.COPY_ATTRIBUTES);
        System.out.println(Files.size(path2));

        Path path3=Paths.get("e:/nio/hello3.txt");
        Files.move(path2,path3 ,StandardCopyOption.ATOMIC_MOVE);
        Files.deleteIfExists(path1);
    }


    /*
        Files常用方法：用于判断
            boolean exists(Path path, LinkOption … opts) : 判断文件是否存在
            boolean isDirectory(Path path, LinkOption … opts) : 判断是否是目录
            boolean isExecutable(Path path) : 判断是否是可执行文件
            boolean isHidden(Path path) : 判断是否是隐藏文件
            boolean isReadable(Path path) : 判断文件是否可读
            boolean isWritable(Path path) : 判断文件是否可写
            boolean notExists(Path path, LinkOption … opts) : 判断文件是否不存在
            public static <A extends BasicFileAttributes> A readAttributes(Path path,Class<A> type,LinkOption... options) : 获取与 path 指定的文件相关联的属性。
     */
    @Test
    public void testFiles2() throws IOException {
        Path path=Paths.get("e:/nio/hello3.txt");
        System.out.println(Files.exists(path,LinkOption.NOFOLLOW_LINKS));
        BasicFileAttributes readAttributes = Files.readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        System.out.println(readAttributes.creationTime());
        System.out.println(readAttributes.lastModifiedTime());

        DosFileAttributeView fileAttributeView = Files.getFileAttributeView(path, DosFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        fileAttributeView.setHidden(true);
        fileAttributeView.setReadOnly(true);
    }

    /*
		Files常用方法：用于操作内容
			SeekableByteChannel newByteChannel(Path path, OpenOption…how) : 获取与指定文件的连接，how 指定打开方式。
			DirectoryStream newDirectoryStream(Path path) : 打开 path 指定的目录
			InputStream newInputStream(Path path, OpenOption…how):获取 InputStream 对象
			OutputStream newOutputStream(Path path, OpenOption…how) : 获取 OutputStream 对象
	 */
    @Test
    public void testFiles3() throws IOException {
        SeekableByteChannel byteChannel = Files.newByteChannel(Paths.get("1.JPG"), StandardOpenOption.READ);
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("e:/"));
        for (Path path : directoryStream) {
            System.out.println(path);
        }

        System.out.println("-------------------------------------");
        InputStream in = Files.newInputStream(Paths.get("e:/nio/hello3.txt"));
        byte[] buf=new byte[512];
        int len =0;
        while ((len=in.read(buf))>0){
            String str=new String(buf,0,buf.length);
            System.out.println(str);
        }
    }

    //自动资源管理：自动关闭实现 AutoCloseable 接口的资源
    @Test
    public void testAutoCloseable() {
        try {
            FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);

            ByteBuffer buf=ByteBuffer.allocate(1024);
            if (inChannel.read(buf)!=-1){
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
