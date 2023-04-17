package org.helloframework.mybatis.oracle;//package org.helloframework.mybatis.oracle;
//
//import org.apache.ibatis.annotations.DeleteProvider;
//import org.apache.ibatis.annotations.InsertProvider;
//import org.apache.ibatis.annotations.SelectProvider;
//import org.apache.ibatis.annotations.UpdateProvider;
//import org.helloframework.mybatis.query.builder.DBBuilder;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * Author lanjian
// * Email  lanjian
// */
//public interface OracleMapper<T> {
//
//    @SelectProvider(type = OracleSqlGen.class, method = "one")
//    T one(DBBuilder dbBuilder);
//
//    @SelectProvider(type = OracleSqlGen.class, method = "list")
//    List<T> list(DBBuilder dbBuilder);
//
//    @SelectProvider(type = OracleSqlGen.class, method = "count")
//    int count(DBBuilder dbBuilder);
//
//    @SelectProvider(type = OracleSqlGen.class, method = "sum")
//    BigDecimal sum(DBBuilder dbBuilder);
//
//    @UpdateProvider(type = OracleSqlGen.class, method = "update")
//    int update(DBBuilder dbBuilder);
//
//    @InsertProvider(type = OracleSqlGen.class, method = "insert")
//    int insert(DBBuilder dbBuilder);
//
//    @DeleteProvider(type = OracleSqlGen.class, method = "delete")
//    int delete(DBBuilder dbBuilder);
//
//}
