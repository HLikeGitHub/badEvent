package com.yjy.web.mms.services.aers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yjy.web.mms.model.dao.aersdao.AersFileListdao;
import com.yjy.web.mms.model.dao.notedao.AttachmentDao;
import com.yjy.web.mms.model.entity.aers.AersFileList;
import com.yjy.web.mms.model.entity.user.User;

@Service
public class FileUploadManagerServices {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AersFileListdao fldao;
	@Autowired
	private AttachmentDao AttDao;
	@Autowired
	private com.yjy.web.mms.model.dao.notedao.AttachService AttachService;
	
	
	@Value("${file.root.path}")
	private String rootPath;
	
//	/**
//	 * 根据父	ID 查询 显示的 路径
//	 * @param parentId
//	 * @return
//	 */
//	public List<AersFilePath> findpathByparent(Long parentId){
//		return fpdao.findByParentIdAndPathIstrash(parentId, 0L);
//	}
//	
//	/**
//	 * 根据路径查询 文件集合
//	 * @param AersFilePath
//	 * @return
//	 */
//	public List<AersFileList> findfileBypath(AersFilePath filePath){
//		return fldao.findByFpathAndFileIstrash(filePath, 0L);
//	}
//	
//	/**
//	 * 根据文件路径 递归查询父级目录 
//	 * @param filePath
//	 * @param parentpaths
//	 */
//	public void findAllParent(AersFilePath filePath,List<AersFilePath> parentpaths){
//		if (filePath.getParentId() != 1L) {
//			AersFilePath filepath1 = fpdao.findById(filePath.getParentId()).orElse(null);
//			parentpaths.add(filepath1);
//			findAllParent(filepath1,parentpaths);
//		}
//	}
	
	/**
	 * 保存文件以及附件
	 * @param file
	 * @param user
	 * @param nowpath
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public Object savefile(MultipartFile file,User user ) throws IllegalStateException, IOException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(this.rootPath,simpleDateFormat.format(new Date()));
		
		File savepath = new File(root,user.getUserName());
		//System.out.println(savePath.getPath());
		
		if (!savepath.exists()) {
			savepath.mkdirs();
		}
		
		String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
		log.info("shuffix:{}",shuffix);
		String newFileName = UUID.randomUUID().toString().toLowerCase()+"."+shuffix;
		File targetFile = new File(savepath,newFileName);
		file.transferTo(targetFile);
		AersFileList filelist = new AersFileList();
		String filename = file.getOriginalFilename();
		filename = onlyname(filename,shuffix,1,true);
		filelist.setFileName(filename);
		filelist.setURL(targetFile.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
		filelist.setFileShuffix(shuffix);
		filelist.setSize(file.getSize());
		filelist.setUploadTime(new Date());
		filelist.setFileType(file.getContentType());
		filelist.setUser(user);
		fldao.save(filelist);
		return filelist;
	}
	
//	//修改附件
//	public Integer updateatt(MultipartFile file,User user,AersFilePath nowpath,long attid) throws IllegalStateException, IOException{
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
//		File root = new File(this.rootPath,simpleDateFormat.format(new Date()));
//		
//		File savepath = new File(root,user.getUserName());
//		//System.out.println(savePath.getPath());
//		
//		if (!savepath.exists()) {
//			savepath.mkdirs();
//		}
//		if(!file.isEmpty()){
//		String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
//		log.info("shuffix:{}",shuffix);
//		String newFileName = UUID.randomUUID().toString().toLowerCase()+"."+shuffix;
//		File targetFile = new File(savepath,newFileName);
//		file.transferTo(targetFile);
//		
//		
//		return AttachService.updateatt(file.getOriginalFilename(),
//				targetFile.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""), shuffix, file.getSize(),
//			    file.getContentType(), new Date(), attid);
//		}
//		return 0;
//	}
//	
//	@Transactional
//	public void doshare(List<Long> fileids){
//		for (Long fileid : fileids) {
//			
//			AersFileList filelist = fldao.findById(fileid).orElse(null);
//			
//			filelist.setFileIsshare(1L);
//			fldao.save(filelist);
//		}
//		
//	}
//	
//	
//	/**
//	 * 根据文件id 批量 删除文件  同时删除 数据库以及本地文件
//	 * @param fileids
//	 */
//	@Transactional
//	public void deleteFile(List<Long> fileids){
//		for (Long fileid : fileids) {
//			AersFileList filelist = fldao.findById(fileid).orElse(null);
//			
//			File file = new File(this.rootPath,filelist.getFilePath());
//			//System.out.println(fileid+":"+file.exists());
//			if(file.exists()&&file.isFile()){
//				System.out.println("现在删除"+filelist.getFileName()+"数据库存档>>>>>>>>>");
//				fldao.deleteById(fileid);
//				System.out.println("现在删除"+filelist.getFileName()+"本地文件    >>>>>>>>>");
//				file.delete();
//			}
//		}
//	}
//	
//	/**
//	 * 根据文件夹id 批量删除 文件夹    并删除此路径下的所有文件以及文件夹
//	 * @param pathids
//	 */
//	@Transactional
//	public void deletePath(List<Long> pathids){
//		for (Long pathid : pathids) {
//			AersFilePath filepath = fpdao.findById(pathid).orElse(null);
////			System.out.println("第一个文件夹："+filepath);
//			
//			//首先删除此文件夹下的文件
//			List<AersFileList> files = fldao.findByFpath(filepath);
//			if(!files.isEmpty()){
////				System.out.println("找到第一个文件夹下的文件不为空！~~~");
////				System.out.println(files);
//				List<Long> fileids= new ArrayList<>();
//				for (AersFileList filelist : files) {
//					fileids.add(filelist.getFileId());
//				}
//				deleteFile(fileids);
//			}
//			
//			//然后删除此文件夹下的文件夹
//			List<AersFilePath> filepaths = fpdao.findByParentId(pathid);
//			if(!filepaths.isEmpty()){
//				List<Long> pathids2 = new ArrayList<>();
//				for (AersFilePath filePath : filepaths) {
//					pathids2.add(filePath.getId());
//				}
//				deletePath(pathids2);
//			}
//			
//			fpdao.delete(filepath);
//		}
//	}
//	
//	/**
//	 * 根据文件id 将文件放入回收站
//	 * @param fileids
//	 */
//	
//	@Transactional
//	public void trashfile(List<Long> fileids,Long setistrashhowmany,Long userid){
//		for (Long fileid : fileids) {
//			AersFileList fileList = fldao.findById(fileid).orElse(null);
//			fileList.setFileIstrash(setistrashhowmany);
//			if(userid != null){
//				fileList.setFpath(null);
//			}
//			
//			fldao.save(fileList);
//		}
//		
//	}
//	
////	public void trashPath(List<Long> pathids){
////		for (Long pathid : pathids) {
////			FilePath filepath = fpdao.findOne(pathid);
////			
////			filepath.setPathIstrash(1L);
////			
////			fpdao.save(filepath);
////		}
////		
////	}
//	
//	/**
//	 * 根据文件夹id 批量放入回收战
//	 * @param pathids
//	 */
//	public void trashpath(List<Long> pathids,Long setistrashhaomany,boolean isfirst){
//		for (Long pathid : pathids) {
//			AersFilePath filepath = fpdao.findById(pathid).orElse(null);
//			//System.out.println("第一个文件夹："+filepath);
//			
//			//首先将此文件夹下的文件放入回收战
//			List<AersFileList> files = fldao.findByFpath(filepath);
//			if(!files.isEmpty()){
//			//	System.out.println("找到第一个文件夹下的文件不为空！~~~");
//				//System.out.println(files);
//				List<Long> fileids= new ArrayList<>();
//				for (AersFileList filelist : files) {
//					fileids.add(filelist.getFileId());
//				}
//				trashfile(fileids,2L,null);
//			}
////			System.out.println("此文件夹内的文件修改成功");
//			//然后将此文件夹下的文件夹放入回收战
//			List<AersFilePath> filepaths = fpdao.findByParentId(pathid);
//			if(!filepaths.isEmpty()){
////				System.out.println("此文件夹下还有文件夹");
//				List<Long> pathids2 = new ArrayList<>();
//				for (AersFilePath filePath : filepaths) {
//					pathids2.add(filePath.getId());
//				}
////				System.out.println("pathids2"+pathids2);
////				System.out.println("接下来尽心递归调用");
//				trashpath(pathids2,2L,false);
//			}
////			System.out.println("此文件下下再无文件夹");
//			if (isfirst) {
//				filepath.setParentId(0L);
//			}
//			filepath.setPathIstrash(setistrashhaomany);
//			fpdao.save(filepath);
//		}
//	}
//	
//	/**
//	 * 文件还原
//	 * @param checkfileids
//	 */
//	@Transactional
//	public void filereturnback(List<Long> checkfileids,Long userid) {
//		AersFilePath fpath = fpdao.findByParentIdAndPathUserId(1L, userid);
//		for (Long checkfileid : checkfileids) {
//			AersFileList fileList = fldao.findById(checkfileid).orElse(null);
//			
//			if (userid != null) {
//				String name = onlyname(fileList.getFileName(), fpath, fileList.getFileShuffix(), 1, true);
//				fileList.setFpath(fpath);
//				fileList.setFileName(name);
//			}
//			fileList.setFileIstrash(0L);
//			fldao.save(fileList);
//		}
//		
//	}
//	
//	/**
//	 * 文件夹还原
//	 * @param pathids
//	 * @param userid
//	 */
//	public void pathreturnback(List<Long> pathids,Long userid){
//		for (Long pathid : pathids) {
//			AersFilePath filepath = fpdao.findById(pathid).orElse(null);
//			System.out.println("第一个文件夹："+filepath);
//			
//			//首先将此文件夹下的文件还原
//			List<AersFileList> files = fldao.findByFpath(filepath);
//			if(!files.isEmpty()){
//				System.out.println("找到第一个文件夹下的文件不为空！~~~");
//				System.out.println(files);
//				List<Long> fileids= new ArrayList<>();
//				for (AersFileList filelist : files) {
//					fileids.add(filelist.getFileId());
//				}
//				filereturnback(fileids,null);
//			}
//			System.out.println("此文件夹内的文件还原成功");
//			System.out.println("然后将此文件夹下的文件夹还原");
//			//然后将此文件夹下的文件夹还原
//			List<AersFilePath> filepaths = fpdao.findByParentId(pathid);
//			if(!filepaths.isEmpty()){
//				System.out.println("此文件夹下还有文件夹");
//				List<Long> pathids2 = new ArrayList<>();
//				for (AersFilePath filePath : filepaths) {
//					pathids2.add(filePath.getId());
//				}
//				System.out.println("pathids2"+pathids2);
//				System.out.println("接下来尽心递归调用");
//				pathreturnback(pathids2,null);
//			}
//			System.out.println("此文件夹下再无文件夹");
//			if(userid!=null){
//				System.out.println("userid不为空表示是第一次进入的文件夹 现在还原");
//				AersFilePath fpath = fpdao.findByParentIdAndPathUserId(1L, userid);
//				String name = onlyname(filepath.getPathName(), fpath, null, 1, false);
//				filepath.setPathName(name);
//				filepath.setParentId(fpath.getId());
//			}
//			System.out.println("还原成功");
//			
//			filepath.setPathIstrash(0L);
//			fpdao.save(filepath);
//		}
//	}
//	
//	
//	/**
//	 * 复制和移动
//	 * @param mcfileids
//	 * @param fromwhere  1为移动  2 为复制
//	 */
//	@Transactional
//	public void moveAndcopy(List<Long> mcfileids,List<Long> mcpathids,Long topathid,boolean fromwhere,Long userid){
//		AersFilePath topath = fpdao.findById(topathid).orElse(null);
//		if(fromwhere){
//			System.out.println("这里是移动！！~~");
//			if(!mcfileids.isEmpty()){
//				System.out.println("fileid is not null");
//				for (Long mcfileid : mcfileids) {
//					AersFileList filelist = fldao.findById(mcfileid).orElse(null);
//					String filename = onlyname(filelist.getFileName(),topath,filelist.getFileShuffix(),1,true);
//					filelist.setFpath(topath);
//					filelist.setFileName(filename);
//					fldao.save(filelist);
//				}
//			}
//			if(!mcpathids.isEmpty()){
//				System.out.println("pathid is not null");
//				for (Long mcpathid : mcpathids) {
//					AersFilePath filepath = fpdao.findById(mcpathid).orElse(null);
//					String name = onlyname(filepath.getPathName(), topath, null, 1, false);
//					filepath.setParentId(topathid);
//					filepath.setPathName(name);
//					fpdao.save(filepath);
//				}
//			}
//		}else{
//			System.out.println("这里是复制！！~~");
//			if(!mcfileids.isEmpty()){
//				System.out.println("fileid is not null");
//				for (Long mcfileid : mcfileids) {
//					AersFileList filelist = fldao.findById(mcfileid).orElse(null);
//					copyfile(filelist,topath,true);
//				}
//			}
//			if(!mcpathids.isEmpty()){
//				System.out.println("pathid is not null");
//				for (Long mcpathid : mcpathids) {
//					copypath(mcpathid, topathid, true, userid);
//				}
//			}
//		}
//	}
//	
//	public void copypath(Long mcpathid,Long topathid,boolean isfirst,Long userid){
//		AersFilePath filepath = fpdao.findById(mcpathid).orElse(null);
//
//		//第一个文件夹的复制
//		AersFilePath copypath = new AersFilePath();
//		copypath.setParentId(topathid);
//		String copypathname = filepath.getPathName();
//		if(isfirst){
//			copypathname = "拷贝 "+filepath.getPathName().replace("拷贝 ", "");
//		}
//		copypath.setPathName(copypathname);
//		copypath.setPathUserId(userid);
//		copypath = fpdao.save(copypath);
//		
//		//这一个文件夹下的文件的复制
//		List<AersFileList> filelists = fldao.findByFpathAndFileIstrash(filepath, 0L);
//		for (AersFileList fileList : filelists) {
//			copyfile(fileList,copypath,false);
//		}
//		
//		List<AersFilePath> filepathsons = fpdao.findByParentIdAndPathIstrash(filepath.getId(), 0L);
//		
//		if(!filepathsons.isEmpty()){
//			for (AersFilePath filepathson : filepathsons) {
//				copypath(filepathson.getId(),copypath.getId(),false,userid);
//			}
//		}
//		
//	}
//	
//	/**
//	 * 文件复制
//	 * @param filelist
//	 */
//	public void copyfile(AersFileList filelist,AersFilePath topath,boolean isfilein){
//		File s = getFile(filelist.getFilePath());
//		User user = filelist.getUser();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
//		File root = new File(this.rootPath,simpleDateFormat.format(new Date()));
//		File savepath = new File(root,user.getUserName());
//		
//		if (!savepath.exists()) {
//			savepath.mkdirs();
//		}
//		
//		String shuffix = filelist.getFileShuffix();
//		log.info("shuffix:{}",shuffix);
//		String newFileName = UUID.randomUUID().toString().toLowerCase()+"."+shuffix;
//		File t = new File(savepath,newFileName);
//		
//		copyfileio(s,t);
//		
//		AersFileList filelist1 = new AersFileList();
//		String filename="";
//		if(isfilein){
//			filename = "拷贝 "+filelist.getFileName().replace("拷贝 ", "");
//		}else{
//			filename = filelist.getFileName();
//		}
//		filename = onlyname(filename,topath,shuffix,1,true);
//		filelist1.setFileName(filename);
//		filelist1.setFilePath(t.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
//		filelist1.setFileShuffix(shuffix);
//		filelist1.setSize(filelist.getSize());
//		filelist1.setUploadTime(new Date());
//		filelist1.setFpath(topath);
//		filelist1.setContentType(filelist.getContentType());
//		filelist1.setUser(user);
//		fldao.save(filelist1);
//		
//	}
//	/**
//	 * 本地文件复制
//	 * @param s
//	 * @param t
//	 */
//	public void copyfileio(File s,File t){
//		InputStream fis = null;
//		OutputStream fos = null;
//		
//		try {
//			fis = new BufferedInputStream(new FileInputStream(s));
//			fos = new BufferedOutputStream(new FileOutputStream(t));
//			byte[] buf = new byte[2048];
//			int i ;
//			while((i = fis.read(buf)) != -1){
//				fos.write(buf, 0, i);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				fis.close();
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	/**
//	 * 移动复制文件树 点击加载
//	 * @param mctoid
//	 * @param mcpathids
//	 * @return
//	 */
//	public List<AersFilePath> mcpathload(Long mctoid,List<Long> mcpathids){
//		List<AersFilePath> showsonpath = new ArrayList<>();
//		List<AersFilePath> sonpaths = fpdao.findByParentIdAndPathIstrash(mctoid, 0L);
//		
//		for (AersFilePath sonpath : sonpaths) {
//			boolean nosame = true;
//			for (Long mcpathid : mcpathids) {
//				if(sonpath.getId().equals(mcpathid)){
//					nosame = false;
//					break;
//				}
//			}
//			if(nosame){
//				showsonpath.add(sonpath);
//			}
//		}
//		return showsonpath;
//	}
//	
//	/**
//	 * 重命名业务方法
//	 * @param name
//	 * @param renamefp
//	 * @param nowpathid
//	 * @param isfile
//	 */
//	public void rename(String name,Long renamefp,Long nowpathid,boolean isfile){
//		if(isfile){
//			//文件名修改
//			AersFileList fl = fldao.findById(renamefp).orElse(null);
//			String newname = onlyname(name, fl.getFpath(), fl.getFileShuffix(), 1, isfile);
//			fl.setFileName(newname);
//			fldao.save(fl);
//		}else{
//			//文件夹名修改
//			AersFilePath fp = fpdao.findById(renamefp).orElse(null);
//			AersFilePath filepath = fpdao.findById(nowpathid).orElse(null);
//			String newname = onlyname(name, filepath, null, 1, false);
//			fp.setPathName(newname);
//			fpdao.save(fp);
//		}
//		
//	}
//	
//	
//	
	/**
	 * 文件以及路径得同名处理
	 * @param name
	 * @param filepath
	 * @param shuffix
	 * @param num
	 * @return
	 */
	public String onlyname(String name,String shuffix,int num,boolean isfile){
		Object f = null;
		f = fldao.findByFileName(name);

		if(f != null){
			int num2 = num -1;
			if(shuffix == null){
				name = name.replace("("+num2+")", "")+"("+num+")";
			}else{
				name = name.replace("."+shuffix,"").replace("("+num2+")", "")+"("+num+")"+"."+shuffix;
			}
			num += 1;
			return onlyname(name,shuffix,num,isfile);
		}
		return name;
	}
	/**
	 * 得到文件
	 * @param filepath
	 * @return
	 */
	public File getFile(String filepath){
		return new File(this.rootPath,filepath);
	}
//	
//	/**
//	 * 找到父级目录 并 拼接成路径
//	 * @param nowpath
//	 * @param parentpaths
//	 * @return
//	 */
//	public String savepath(AersFilePath nowpath,List<AersFilePath> parentpaths){
//		findAllParent(nowpath,parentpaths);
//		Collections.reverse(parentpaths);
//		String savepath = "";
//		for (AersFilePath filePath : parentpaths) {
//			savepath += filePath.getPathName()+"/";
//		}
//		savepath = savepath.substring(0, savepath.length()-1);
//		return savepath;
//	}
//	
//	/**
//	 * 复制和移动  根据上级目录id 查询 所有子目录
//	 * @param parentid
//	 * @return
//	 */
////	public List<FilePath> moveandcopy(Long parentid){
////		List<FilePath> filePaths= fpdao.findByParentId(parentid);
////		return filePaths;
////	}
////	
//	
//	/**
//	 * 获取附件
//	 * 
//	 * @param att
//	 * @return
//	 */
//	public File get(Attachment att) {
//		return new File(this.rootPath+att.getAttachmentPath());
//	}
//	
//	public Attachment get(String filePath) {
//		return AttDao.findByAttachmentPath(filePath);
//	}

	
}
