package ${packageName}.mapper;

import org.apache.ibatis.annotations.Mapper;
import ${packageName}.pojo.${pojoName};
import org.helloframework.mybatis.mysql.MysqlMapper;

/**
* Created by codegen
*/
@Mapper
public interface ${pojoName}Mapper extends MysqlMapper<${pojoName}> {

}