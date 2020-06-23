package org.jeecg.modules.mortgagerpc.mongo.util;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

@Configuration
public class GridFSBucket {
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private GridFSBucket gridFSBucket;


    @Bean
    public com.mongodb.client.gridfs.GridFSBucket getGridFSBuckets() {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db);
    }
}
