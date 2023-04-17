## hello
hello open api 服务治理

## 目标
简化api开发，规范api开发标准，统一api入口和参数。

目前使用protobuf作为序列化方案。使用jprotobuf作为序列化库。

支持本地和远程模式两种

本地模式：将api服务和apigateway融合，发布形式war包

远程模式：apigateway发布成web项目，apiservice启动服务，提供远程服务。底层使用netty实现。

## 使用方法

### 注册apigateway client

目前实现用zookeeper做服务注册中心

    <!--初始化apigw调用端 mode="local"为本地模式-->
    <hello:client name="kyls"/>
    <!--注册中心-->
    <hello:registry address="zookeeper://127.0.0.1:2181"/>
    
### 编写apiservice
apiservice支持两种编写方案

 1.继承指定抽象类或者接口，实现模板方法

 2.定义apiservice类，使用@ApiMethod暴露服务
 
 ApiService定义
 
    public interface ApiService<I, O> extends ApiServiceDefinition {
        O handler(I i, ApiExtend extend);
        O service(I i, ApiExtend extend);
    }
 
 例子1,继承AbstractApiService或者实现ApiService接口
         
    @Api
    public class Test extends AbstractApiService<User, User> {
    
        public User service(User user, ApiExtend extend) {
            return new User();
        }
    }

 例子2：定义服务类，使用@ApiMethod注解，所有需要发布成api服务都需要实现ApiServiceDefinition（空定义，只为了标注），目前所有的结构都是 out xxx(In in,ApiExtend extend)扫描服务时候会强制验证格式。
 
    @Api
    public class Test implements ApiServiceDefinition {
        @ApiMethod
        public User service(User user, ApiExtend extend) {
            return user;
        }
    
        @ApiMethod
        public User service1(User user, ApiExtend extend) {
            System.out.println("service1");
            return user;
        }
    
    }
    
 ### api服务配置
 使用apigateway server配置
 
     <!--初始化apigw服务端-->
    <hello:application name="kyls" port="8999" threads="4"/>
    <!--注册中心注册-->
    <hello:registry address="zookeeper://127.0.0.1:2181"/>
    <!--扫描所有符合注解要求的类，指定包，扫描@Api注解-->
    <hello:apiservices package="com.iflytek.hello.in.caixi.api.gateway.services"/>
    <!--插件体系，扫描插件，指定包，扫描@ApiUtils注解，有默认实现，如需要替换才自己实现-->
    <hello:utils package="com.iflytek.hello.in.caixi.api.gateway.services"/>
    
  ### 插件体系
  
 目前插件接口定义如下：
 
  url参数签名验证：ApiServiceSignCheck
  
  防重放：ApiServiceReplayCheck
  
  数据编码：ApiServiceCodec
  
  数据验证：ApiServiceValidate
  
  后续增加更多插件定义 