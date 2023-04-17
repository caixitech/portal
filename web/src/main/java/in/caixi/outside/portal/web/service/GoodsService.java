package in.caixi.outside.portal.web.service;

import in.caixi.outside.portal.domain.Goods;
import org.helloframework.mongodb.Mongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsService {
    @Resource
    private MongoTemplate mongoTemplate;

    public List<Goods> list() {
        return Mongo.build(mongoTemplate).all(Goods.class);
    }
}
