package ${packageName}.dao.impl;

import ${packageName}.dao.I${pojoName}Dao;
import ${packageName}.pojo.${pojoName};
import ${packageName}.mapper.${pojoName}Mapper;
import org.helloframework.mybatis.mysql.AbstractMySqlDao;
import org.springframework.stereotype.Repository;

/**
* Created by codegen
*/
@Repository
public class ${pojoName}DaoImpl extends AbstractMySqlDao<${pojoName}> implements I${pojoName}Dao {
    @Override
    protected Class mapperClass() {
        return ${pojoName}Mapper.class;
    }
}