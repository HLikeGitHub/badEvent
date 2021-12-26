package com.yjy.web.comm.file.fastdfs;

import com.yjy.web.comm.utils.TextUtil;
import com.yjy.web.comm.utils.properties.AppPropUtil;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @info FastDFSClient
 * <p>文件上传后会获取远程文件的组和文件名字，这个可以用于查找，下载和删除该文件</p>
 * @author rwx
 * @mail aba121mail@qq.com
 */
public class FastDFSClient {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private static class FastDFSClientHolder{
        private static FastDFSClient instance = new FastDFSClient();
    }

    public static FastDFSClient getInstance(){
        return FastDFSClientHolder.instance;
    }

    private FastDFSClient(){
        initConf();
    }

    private void initConf(){
        try {
            Properties props = new Properties();
            if(AppPropUtil.getInstance().isDev()){
                //开发环境配置
                props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, "60");
                props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, "60");
                props.put(ClientGlobal.PROP_KEY_CHARSET, "UTF-8");
                props.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, "false");
                props.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, "FastDFS0123456789");
                props.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, "80");
                props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "192.168.20.158:22122");//host1,host2
            }else {
                //生产环境配置
                props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, "90");
                props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, "90");
                props.put(ClientGlobal.PROP_KEY_CHARSET, "UTF-8");
                props.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, "false");
                props.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, "FastDFS0123456789");
                props.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, "80");
                props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, "192.168.20.158:22122");//host1,host2
            }
            ClientGlobal.initByProperties(props);
        } catch (Exception e) {
            logger.error("FastDFSClient Init error, e = ", e.getClass());
            e.printStackTrace();
        }
    }

    private TrackerServer getTrackerServer() throws Exception {
        return  new TrackerClient().getConnection();
    }

    public String getTrackerUrl() throws Exception {
        int iPort = ClientGlobal.getG_tracker_http_port();
        String port = iPort == 80 ? "" : (":" + iPort);
        return "http://" + getTrackerServer().getInetSocketAddress().getHostString() + port + "/";
    }

    private StorageClient getStorageClient() throws Exception {
        StorageClient storageClient = new StorageClient(getTrackerServer(), null);
        return  storageClient;
    }

    public StorageServer[] getStoreStorages(String groupName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    public ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    public String[] upload(FastDFSFile file) {
        logger.info("FastDFSClient--->>>upload, file=" + file.getName() +
                ", length=" + TextUtil.formatFileSize(file.getContent() == null ? 0 : file.getContent().length));

        NameValuePair[] metas = new NameValuePair[1];
        metas[0] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        StorageClient storageClient=null;
        String[] results = null;
        try {
            storageClient = getStorageClient();
            results = storageClient.upload_file(file.getContent(), file.getExt(), metas);
        } catch (Exception e) {
            logger.error("FastDFSClient--->>>upload error, file=" + file.getName()+", e="+e.getClass());
            e.printStackTrace();
        }
        if(results == null){
            logger.error("FastDFSClient--->>>upload error, code=" + storageClient == null ?  "null" : storageClient.getErrorCode() + "");
        }else{
            String groupName = results[0];
            String remoteFileName = results[1];
            logger.info("FastDFSClient--->>>upload success, groupName=" + groupName + ", remoteFileName=" + remoteFileName);
        }
        logger.info("FastDFSClient--->>>upload finish, time=" + (System.currentTimeMillis() - startTime) + " ms");
        return results;
    }

    /**
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile multipartFile){
        if(multipartFile == null){
            logger.error("FastDFSClient--->>>upload error, file is null");
            return "";
        }
//        Path path = Paths.get("D://"+file.getOriginalFilename());
//        Files.write(path, file.getBytes());

        String fileName = multipartFile.getOriginalFilename();
        String postfix = "";
        if(fileName.contains(".")){
            postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        InputStream inputStream = null;
        byte[] bytes = null;
        try{
            inputStream = multipartFile.getInputStream();
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
        }catch (Exception e){
            logger.error("FastDFSClient--->>>upload error, e="+e.getClass());
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }

        String[] results = upload(new FastDFSFile(fileName, bytes, postfix));
        if (results == null) {
            return "";
        }
        String trackerUrl = "";
        try{
            trackerUrl = getTrackerUrl();
        }catch (Exception e){
            logger.info("FastDFSClient--->>>upload error, e="+e.getClass());
            e.printStackTrace();
        }
        String path = trackerUrl + results[0] + "/" + results[1];
        logger.info("FastDFSClient--->>>upload success, file="+fileName+", url="+path);
        return path;
    }

    public FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public InputStream downFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (Exception e) {
            logger.error("Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    public void deleteFile(String groupName, String remoteFileName) throws Exception {
        StorageClient storageClient = getStorageClient();
        int i = storageClient.delete_file(groupName, remoteFileName);
        logger.info("delete file successfully!!!" + i);
    }
}