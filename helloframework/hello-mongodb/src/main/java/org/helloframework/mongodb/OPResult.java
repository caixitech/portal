package org.helloframework.mongodb;

import com.mongodb.client.result.UpdateResult;

public class OPResult {
    private MongoOpType mongoOpType;
    private UpdateResult updateResult;

    public OPResult(MongoOpType mongoOpType, UpdateResult updateResult) {
        this.mongoOpType = mongoOpType;
        this.updateResult = updateResult;
    }

    public MongoOpType getMongoOpType() {
        return mongoOpType;
    }

    public void setMongoOpType(MongoOpType mongoOpType) {
        this.mongoOpType = mongoOpType;
    }

    public UpdateResult getUpdateResult() {
        return updateResult;
    }

    public void setUpdateResult(UpdateResult updateResult) {
        this.updateResult = updateResult;
    }
}
