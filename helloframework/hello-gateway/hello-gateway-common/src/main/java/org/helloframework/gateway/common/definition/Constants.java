package org.helloframework.gateway.common.definition;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.regex.Pattern;

/**
 * Constants
 *
 * @author lanjian
 */
public class Constants {

    public final static int GW_SEND_COUNT_DOWN_NUM = 1;

    public final static Integer CLIENT_READ_IDLE_TIME = 60;
    public final static Integer CLIENT_WRITE_IDLE_TIME = 40;
    public final static Integer CLIENT_ALL_IDLE_TIME = 20;

    public final static Integer SERVER_READ_IDLE_TIME = 60;
    public final static Integer SERVER_WRITE_IDLE_TIME = 60;
    public final static Integer SERVER_ALL_IDLE_TIME = 0;

    public final static Integer TOTAL_RETRY_CONNECT_COUNT = 3;

    public final static Integer RETRY_CONNECT_TIME = 5000;


    public static final String NAME = "hello";

    public static final String REMOTE_MODE = "remote";

    public static final String LOCAL_MODE = "local";

    public static final String MESH_MODE = "mesh";

    //    public static final String PB_PROTOCOL_PROPERTY = "pb";
    public static final String JSON_PROTOCOL_PROPERTY = "json";

    public static final String SERVICE_NAME_KEY = "service_name";

    public static final String SERVER_DEFAULT_CLASS = "org.helloframework.gateway.core.netty.server.NettyServer";

    public static final String CLIENT_DEFAULT_CLASS = "org.helloframework.gateway.core.netty.sender.NettySender";

    public static final String MESH_DEFAULT_CLASS = "org.helloframework.gateway.common.definition.MeshSender";

    public static final String CLIENT_DEFAULT_LOCAL_CLASS = "org.helloframework.gateway.core.netty.sender.LocalSender";

    public static final String CATEGORY_KEY = "category";

    public static final String PROVIDERS_CATEGORY = "providers";

    public static final String CONSUMERS_CATEGORY = "consumers";

    public static final String ROUTERS_CATEGORY = "routers";

    public static final String CONFIGURATORS_CATEGORY = "configurators";

    public static final String DEFAULT_CATEGORY = PROVIDERS_CATEGORY;


    public static final int DEFAULT_CPU = Runtime.getRuntime().availableProcessors();

    public static final int DEFAULT_CPU_THREADS = DEFAULT_CPU * 2;

    public static final int DEFAULT_WORK_CPU = 500;

    public static final String DYNAMIC_KEY = "dynamic";


    public static final int DEFAULT_REGISTRY_CONNECT_TIMEOUT = 5000;

    public static final int DEFAULT_SEND_CONNECT_TIMEOUT = 30 * 1000;

    public static final int DEFAULT_MAX_HANDLER = 100;

    public static final int DEFAULT_CONNECT_TIMEOUT = 2000;

    public static final int REPLY_TIMEOUT = 30 * 1000;

    public static final int FUSE_LIVE_TIMEOUT = 60 * 1000;

    public static final int FUSE_COUNT = 60;

    public static final String REPLY_CACHE_NAME = "REPLY_CACHE_KEY";

    public static final String FUSE_CACHE_NAME = "FUSE_CACHE_KEY";

    public static final String REMOVE_VALUE_PREFIX = "-";


    public static final String DEFAULT_KEY_PREFIX = "default.";

    public static final String DEFAULT_KEY = "default";


    public static final String BACKUP_KEY = "backup";


    public static final String ANYHOST_KEY = "anyhost";

    public static final String ANYHOST_VALUE = "0.0.0.0";

    public static final String LOCALHOST_KEY = "localhost";


    public static final String WEIGHT_KEY = "weight";

    public static final Integer WEIGHT = 100;


    public static final String TIMEOUT_KEY = "timeout";

    public static final String MAX_KEY = "max";

    public static final String CHECK_KEY = "check";

    public static final String GROUP_KEY = "group";

//    public static final String INTERFACE_KEY = "interface";

    public static final String VERSION_KEY = "version";

    public static final String TIME_VERSION_KEY = "time";

    public static final String METHODS_KEY = "methods";

    public static final String ANY_VALUE = "*";


    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");

    public final static String PATH_SEPARATOR = "/";

    public final static String METHODS_SEPARATOR = ",";

    public static final String REGISTRY_SEPARATOR = "|";

    public static final Pattern REGISTRY_SPLIT_PATTERN = Pattern
            .compile("\\s*[|;]+\\s*");

    public static final String EMPTY_PROTOCOL = "empty";

    /**
     * 注册中心是否同步存储文件，默认异步
     */
    public static final String REGISTRY_FILESAVE_SYNC_KEY = "save.file";

    /**
     * 注册中心失败事件重试事件
     */
    public static final String REGISTRY_RETRY_PERIOD_KEY = "retry.period";

    /**
     * 重试周期
     */
    public static final int DEFAULT_REGISTRY_RETRY_PERIOD = 5 * 1000;

    public static final int DEFAULT_TIME_OUT = 3 * 1000;

    public static final int DIE_FAIL_COUNT = 10;

    public static final String SESSION_TIMEOUT_KEY = "session";

    public static final int DEFAULT_SESSION_TIMEOUT = 60 * 1000;

    /**
     * 注册中心导出URL参数的KEY
     */
    public static final String EXPORT_KEY = "export";

    /**
     * 注册中心引用URL参数的KEY
     */
    public static final String REFER_KEY = "refer";

    public static final String X_Ca_Version = "X-Ca-Version";
    public static final String X_Ca_Stage = "X-Ca-Stage";
    public static final String X_Ca_Appid = "X-Ca-Appid";
    public static final String X_Ca_Timestamp = "X-Ca-Timestamp";
    public static final String X_Ca_Token = "X-Ca-Token";
    public static final String X_Ca_Nonce = "X-Ca-Nonce";
    public static final String X_Ca_Group = "X-Ca-Group";
    public static final String X_Ca_Api = "X-Ca-Api";
    public static final String X_Ca_Host = "X-Ca-Host";
    public static final String X_Ca_Mock = "X-Ca-Mock";
    public static final String X_Ca_Signature = "X-Ca-Signature";
    public static final String X_Ca_Signature_Headers = "X-Ca-Signature-Headers";
    public static final String X_Ca_Charset = "X-Ca-Charset";
    public static final String X_Ca_Request_Id = "X-Ca-Request-Id";
    public static final String X_Ca_Code = "X-Ca-Code";
    public static final String X_Ca_Message = "X-Ca-Message";

    public static final String REQUEST_DATA = "data";
    public static final int API_ERROR_CODE = 500;
    public static final int API_UNKNOW_ERROR_CODE = 543;
    public static final int API_NO_FOUND = 504;
    public static final byte[] ERROR_INFO = "exception".getBytes();
    public static final byte[] EXIT_INFO = "exit".getBytes();
    public static final byte[] REMOVE_INFO = "remove".getBytes();
    public static final byte[] PING_INFO = "ping".getBytes();
    public static final byte[] BACK_INFO = "back".getBytes();
    public static final String TMP_METHOD_SERVICE = "service";
    public static final FastDateFormat STANDARD_TIME = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    public static final int RETRY_CLIENT_CHECK = 30 * 1000;


    public static final String CLIENT_WORK_THREAD_NAME = "Hello-Client-Work";
    public static final String SERVER_WORK_THREAD_NAME = "Hello-Server-Work";

    public static final long REGISTER_TIME = 100;
    public static final Integer MAX = 10000;

    public static final String GRAPH_MODEL = "graph";
    //请求头里面标注模式
    public static final String X_Ca_Model = "X-Ca-Model";

}
