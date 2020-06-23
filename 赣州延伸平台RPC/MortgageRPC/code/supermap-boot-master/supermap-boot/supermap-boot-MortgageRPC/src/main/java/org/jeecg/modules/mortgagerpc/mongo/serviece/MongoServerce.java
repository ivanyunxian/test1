package org.jeecg.modules.mortgagerpc.mongo.serviece;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public  interface MongoServerce {

    public  String  save(File file);
    public byte[]  getFileByteByID(String id);
    public File  getFileByID(String id);
    public String  save(MultipartFile file) throws IOException;
    public void delete(String mongoid);
}
