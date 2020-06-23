package org.jeecg.modules.mortgagerpc.mongo.serviece.imp;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.jeecg.modules.mortgagerpc.mongo.serviece.MongoServerce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service("mongoServerce")
public class MongoServerImp implements MongoServerce {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFSBucket gridFSBucket;


    @Override
    public String  save(File file) {
        try{
            InputStream inputStream = new FileInputStream(file);
            //存储文件并起名称
            ObjectId objectId = gridFsTemplate.store(inputStream, file.getName());
            inputStream.close();
            return  objectId.toString();
        }catch (Exception e) {

        }
       return  null;
    }
    @Override
    public String  save(MultipartFile file) throws IOException {
        //存储文件并起名称
        ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename());
        return  objectId.toString();
    }

    @Override
    public void delete(String mongoid) {
        try{
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(mongoid)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public  byte[]  getFileByteByID(String id){
        try{
            GridFSFile  gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
            if(gridfsfile != null){
                GridFSDownloadStream in = gridFSBucket.openDownloadStream(gridfsfile.getObjectId());
                GridFsResource resource = new GridFsResource(gridfsfile,in);
                InputStream inputStream = resource.getInputStream();
                return getBytes(inputStream);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }
    @Override
    public File getFileByID(String id){
        FileOutputStream out=null;
        File newFile=null;
        try{
           // Query s=Query.query(Criteria.where("_id").is(id));
            GridFSFile  gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
            if(gridfsfile != null){
                GridFSDownloadStream in = gridFSBucket.openDownloadStream(gridfsfile.getObjectId());
                GridFsResource resource = new GridFsResource(gridfsfile,in);
                InputStream inputStream = resource.getInputStream();
                byte[] f = getBytes(inputStream);
                newFile=new File("D:\\TEMPfILE\\"+gridfsfile.getFilename());
                out = new FileOutputStream(newFile);
                out.write(f);
                return newFile ;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(newFile!=null&&newFile.exists()){
                newFile.delete();
            }
            if(out!=null){
                try {
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return  null;
    }
    private byte[] getBytes(InputStream inputStream) throws  Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024*10];
        int  i = 0;
        while (-1!=(i=inputStream.read(b))){
            bos.write(b,0,i);
        }
        return bos.toByteArray();
    }
}
