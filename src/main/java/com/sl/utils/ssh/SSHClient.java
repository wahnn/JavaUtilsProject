package com.sl.utils.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPInputStream;
import ch.ethz.ssh2.SCPOutputStream;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Description(ssh 操作工具类  参考网上资料)
 * author: Gao Xueyong
 * Create at: 2019/3/27 17:34
 */
public class SSHClient {

    private static SSHClient instance;

    private String ip;

    private int port;

    private String name;

    private String password;

    /**
     * 私有化默认构造函数
     * 实例化对象只能通过getInstance
     */
    private SSHClient() {

    }

    /**
     * 私有化有参构造函数
     *
     * @param ip       服务器ip
     * @param port     服务器端口 22
     * @param name     登录名
     * @param password 登录密码
     */
    private SSHClient(String ip, int port, String name, String password) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.password = password;
    }

    /**
     * download
     *
     * @param remoteFile            服务器上的文件名
     * @param remoteTargetDirectory 服务器上文件的所在路径
     * @param newPath               下载文件的路径
     *                              return  {“success”:true or false,"msg":"xxxx"}
     */
    public Map<String, Object> downloadFile(String remoteFile, String remoteTargetDirectory, String newPath) {
        Connection connection = new Connection(ip, port);
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("msg", "下载文件失败！");
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(name, password);
            if (!isAuthenticated) {
                System.out.println("连接建立失败");
                result.put("msg", "下载文件失败！原因：连接建立失败");
                return result;
            }
            SCPClient scpClient = connection.createSCPClient();
            SCPInputStream sis = scpClient.get(remoteTargetDirectory + File.separator + remoteFile);
            File f = new File(newPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            File newFile = new File(newPath + remoteFile);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] b = new byte[4096];
            int i;
            while ((i = sis.read(b)) != -1) {
                fos.write(b, 0, i);
            }
            fos.flush();
            fos.close();
            sis.close();
            connection.close();
//                System.out.println("download ok");
            result.put("success", true);
            result.put("msg", "下载文件成功!");
//            } else {
//                System.out.println("连接建立失败");
//            }
        } catch (IOException e) {
//            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "下载文件失败！原因：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取服务器上相应文件的流
     *
     * @param remoteFile            文件名
     * @param remoteTargetDirectory 文件路径
     * @return
     * @throws IOException
     */
    public SCPInputStream getStream(String remoteFile, String remoteTargetDirectory) throws IOException {
        Connection connection = new Connection(ip, port);
        connection.connect();
        boolean isAuthenticated = connection.authenticateWithPassword(name, password);
        if (!isAuthenticated) {
            System.out.println("连接建立失败");
            return null;
        }
        SCPClient scpClient = connection.createSCPClient();
        return scpClient.get(remoteTargetDirectory + File.separator + remoteFile);
    }

    /**
     * 上传文件到服务器
     *
     * @param f                     文件对象
     * @param length                文件大小
     * @param remoteTargetDirectory 上传路径
     * @param mode                  默认为null
     *                              return  {“success”:true or false,"msg":"xxxx"}
     */
    public Map<String, Object> uploadFile(File f, long length, String remoteTargetDirectory, String mode) {
        Connection connection = new Connection(ip, port);
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("msg", "上传文件失败！");
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(name, password);
            if (!isAuthenticated) {
                System.out.println("连接建立失败");
                result.put("msg", "上传文件失败！原因：连接建立失败");
                return result;
            }
            SCPClient scpClient = new SCPClient(connection);
            SCPOutputStream os = scpClient.put(f.getName(), length, remoteTargetDirectory, mode);
            byte[] b = new byte[4096];
            FileInputStream fis = new FileInputStream(f);
            int i;
            while ((i = fis.read(b)) != -1) {
                os.write(b, 0, i);
            }
            os.flush();
            fis.close();
            os.close();
            connection.close();
            result.put("success", true);
            result.put("msg", "上传文件成功!");
//            System.out.println("upload ok");
        } catch (IOException e) {
//            e.printStackTrace();
            result.put("success", false);
            result.put("msg", "上传文件失败！原因：" + e.getMessage());
        }
        return result;
    }


    /**
     * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
     *
     * @param command 命令  不涉及二次密码验证！！！
     * @return {“success”:true or false,"msg":"xxxx"}
     * 执行成功只是执行命令成功，不代表命令要执行的结果成功！！！
     */
    public Map<String, Object> exec(String command) {
        Map<String, Object> retResult = new HashMap<>();
        retResult.put("success", false);
        retResult.put("msg", "执行命令失败");
        StringBuffer result = new StringBuffer();
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(name, ip, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            int exitStatus = openChannel.getExitStatus();
            System.out.println(exitStatus);
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                // result+= new String(buf.getBytes("gbk"),"UTF-8")+" <br>\r\n";
//                result += new String(buf.getBytes("gbk"), "UTF-8") + "\r\n";
                result.append(buf + "\n");
            }
            retResult.put("success", true);
            retResult.put("result",result.toString());
            retResult.put("msg", "执行命令成功");
        } catch (JSchException | IOException e) {
//            result += e.getMessage();
            result.append(e.getMessage());
            result.append("\r\n" + e);
            retResult.put("success", false);
            retResult.put("msg", "执行命令失败：原因：" + e);
//            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return retResult;
    }


    /**
     * 单例模式
     * 懒汉式
     * 线程安全
     *
     * @return
     */
    public static SSHClient getInstance() {
        if (null == instance) {
            synchronized (SSHClient.class) {
                if (null == instance) {
                    instance = new SSHClient();
                }
            }
        }
        return instance;
    }

    public static SSHClient getInstance(String ip, int port, String name, String password) {
        if (null == instance) {
            synchronized (SSHClient.class) {
                if (null == instance) {
                    instance = new SSHClient(ip, port, name, password);
                }
            }
        }
        return instance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
