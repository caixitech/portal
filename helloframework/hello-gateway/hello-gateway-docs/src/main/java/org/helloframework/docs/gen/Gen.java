package org.helloframework.docs.gen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.helloframework.codec.json.JSON;
import org.helloframework.docs.annotation.*;
import org.helloframework.docs.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class Gen {
    public static final Logger log = LoggerFactory.getLogger(Gen.class);
    private static final SimpleDateFormat TIMELINE_SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final int NEW_TIMELINE_DAY = 7;
    private static final String DOC_ROOT = "./docs";

    // 待处理的类
    private Set<Class<?>> basicSet = new HashSet<Class<?>>();
    private Set<Class<?>> dtoSet = new HashSet<Class<?>>();
    private Set<Class<?>> serviceSet = new HashSet<Class<?>>();

    // 结果集
    public List<TimelineVo> timelineList = new ArrayList<TimelineVo>();
    public Map<String, MenuGroupVo> menuTempMap = new HashMap<String, MenuGroupVo>(); // Key: 分组名称
    public List<MenuGroupVo> menuList = new ArrayList<MenuGroupVo>();
    public Map<String, List<CodeVo>> basicMap = new HashMap<String, List<CodeVo>>(); // Key: js file
    public Map<String, DtoVo> dtoMap = new HashMap<String, DtoVo>(); // // Key: js file
    public Map<String, ServiceVo> serviceMap = new HashMap<String, ServiceVo>(); // // Key: js file

    //基本嘻嘻map
    private ApiInfo basicInfo = new ApiInfo();

    private String docRoot;

    public String getDocRoot() {
        return StringUtils.isNotBlank(docRoot) ? docRoot : DOC_ROOT;
    }

    public void setDocRoot(String docRoot) {
        this.docRoot = docRoot;
    }

    public void scanPakcages(String... packageName) {
        Assert.notEmpty(packageName, "package");
        try {
            // 扫描基础定义
            ScanWrapper scan = new ScanWrapper(packageName, DocApiGlobalCode.class);
            Set<Class<?>> set = scan.getClassSet();
            if (set != null) {
                basicSet.addAll(set);
            }
            // 扫描dto
            scan = new ScanWrapper(packageName, DocApiDTO.class);
            set = scan.getClassSet();
            if (set != null) {
                dtoSet.addAll(set);
            }
            // 描述service
            scan = new ScanWrapper(packageName, DocApiService.class);
            set = scan.getClassSet();
            if (set != null) {
                serviceSet.addAll(set);
            }
        } catch (Throwable e) {
            log.error("扫描包失败：" + e.getMessage(), e);
        }
    }


    public void initInfo(ApiInfo basicInfo) {
        this.basicInfo = basicInfo;
    }


    public boolean register(Class clazz, GenType type) {
        if (clazz == null)
            return false;

        switch (type) {
            case Basic:
                return basicSet.add(clazz);
            case Dto:
                return dtoSet.add(clazz);
            case Service:
                return serviceSet.add(clazz);
            default:
                return false;
        }
    }

    public void handler() throws GenException, IOException {
        log.info("输出目录：" + new File(getDocRoot()).getAbsolutePath());
        //检查输入的参数是否都有了
        check();
        // 分析
        resolve();
        // 排序
        sort();
        // 拷贝模板
        copyTemplate();
        // 写入
        write();
        log.info("文件生成成功！");
    }

    private void check() {
        Assert.notNull(basicInfo, "apiInfo");
        Assert.notNull(basicInfo.getEnName(), "apiInfo enName");
        Assert.notNull(docRoot, "docRot目录");
    }

    private void resolve() throws GenException {
        for (Class<?> clazz : basicSet) {
            // 分析时间轴。
            resolveTimeline(clazz);
            // 分析基础定义。
            resolveBasic(clazz);
        }
        for (Class<?> clazz : dtoSet) {
            // 分析时间轴。
            resolveTimeline(clazz);
            // 分析菜单。
            resolveMenu(clazz);
            // 分析数据结构。
            resolveDto(clazz);
        }
        for (Class<?> clazz : serviceSet) {
            // 分析时间轴。
            resolveTimeline(clazz);
            // 分析菜单。
            resolveMenu(clazz);
            // 分析接口服务。
            resolveService(clazz);
        }
    }

    private void sort() {
        // 菜单排序
        for (Map.Entry<String, MenuGroupVo> vo : menuTempMap.entrySet()) {
            Collections.sort(vo.getValue().getSubs(), new Comparator<MenuVo>() {
                public int compare(MenuVo o1, MenuVo o2) {
                    if (o1 == null && o2 == null)
                        return 0;
                    else if (o1 == null)
                        return 1;
                    else if (o2 == null)
                        return -1;

                    return ObjectUtils.compare(o1.getTitle(), o2.getTitle());
                }
            });
            // 复制到列表
            menuList.add(vo.getValue());
            // 列表排序
            Collections.sort(menuList, new Comparator<MenuGroupVo>() {
                public int compare(MenuGroupVo o1, MenuGroupVo o2) {
                    if (o1 == null && o2 == null)
                        return 0;
                    else if (o1 == null)
                        return 1;
                    else if (o2 == null)
                        return -1;

                    return ObjectUtils.compare(o1.getSort(), o2.getSort());
                }
            });
        }
    }

    private void copyTemplate() throws IOException {
//        FileUtils.deleteDirectory(new File(getDocRoot()));
        FileUtils.forceMkdir(new File(getDocRoot()));
//        FileUtils.copyDirectory(new File(URLDecoder.decode(Gen.class.getResource("/").getFile()) + basicInfo.getEnName()), new File(getDocRoot()));
        FileUtils.deleteDirectory(new File(getDocRoot() + "/static/data"));
        FileUtils.forceMkdir(new File(getDocRoot() + "/static/data"));
    }

    private void write() throws IOException {
        // 写菜单
        writeFile(getDocRoot() + "/static/data", "nav_menu.js", JSON.toJSONString(menuList));
        // 写时间轴
        writeFile(getDocRoot() + "/static/data", "timelines.js", JSON.toJSONString(timelineList));
        // 写基础定义
        for (Map.Entry<String, List<CodeVo>> vo : basicMap.entrySet()) {
            writeFile(getDocRoot() + "/static/data/basic_definition", vo.getKey() + ".js", JSON.toJSONString(vo.getValue()));
        }
        // 写数据结构
        for (Map.Entry<String, DtoVo> vo : dtoMap.entrySet()) {
            writeFile(getDocRoot() + "/static/data/dto", vo.getKey() + ".js", JSON.toJSONString(vo.getValue()));
        }
        // 写接口服务
        for (Map.Entry<String, ServiceVo> vo : serviceMap.entrySet()) {
            writeFile(getDocRoot() + "/static/data/service", vo.getKey() + ".js", JSON.toJSONString(vo.getValue()));
        }
        if (basicInfo != null) {
            writeFile(getDocRoot() + "/static/data", "basic.js", JSON.toJSONString(basicInfo));
            if (basicInfo.getHeader() != null && basicInfo.getHeader().size() > 0) {
                writeFile(getDocRoot() + "/static/data/basic_definition", "header.js", JSON.toJSONString(basicInfo.getHeader()));
            }
            if (basicInfo.getBasicResp() != null && basicInfo.getBasicResp().size() > 0) {
                writeFile(getDocRoot() + "/static/data/basic_definition", "resp.js", JSON.toJSONString(basicInfo.getBasicResp()));
            }
        }
    }

    private void writeFile(String path, String filename, String content) throws IOException {
        FileUtils.forceMkdir(new File(path));
        File file = new File(path + "/" + filename);
        file.delete();

        FileWriter writer = null;
        try {
            writer = new FileWriter(file, false);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            try {
                if (writer != null)
                    writer.close();
            } catch (Throwable ignored) {

            }
            throw e;
        }
    }

    private void resolveTimeline(Class<?> clazz) throws GenException {
        resolveTimeline(clazz, timelineList);
    }

    private void resolveTimeline(Class<?> clazz, List<TimelineVo> list) throws GenException {
        try {
            if (!clazz.isAnnotationPresent(DocApiTimeline.class))
                return;

            DocApiTimeline ann = clazz.getAnnotation(DocApiTimeline.class);
            if (ann == null || ann.value() == null || ann.value().length == 0)
                return;

            for (DocTimeline timeline : ann.value()) {
                TimelineVo vo = new TimelineVo();
                vo.setTime(timeline.time());
                vo.setContent(timeline.content());
                vo.setUrl(resolveUrl(clazz));
                list.add(vo);
            }
            // 时间轴排序
            Collections.sort(list, new Comparator<TimelineVo>() {
                public int compare(TimelineVo o1, TimelineVo o2) {
                    if (o1 == null && o2 == null)
                        return 0;
                    else if (o1 == null)
                        return 1;
                    else if (o2 == null)
                        return -1;

                    return ObjectUtils.compare(o2.getTime(), o1.getTime());
                }
            });
        } catch (Throwable e) {
            throw new GenException("分析时间轴异常，", clazz, e);
        }
    }

    private void resolveMenu(Class<?> clazz) throws GenException {
        try {
            if (clazz.isAnnotationPresent(DocApiDTO.class)) {
                DocApiDTO ann = clazz.getAnnotation(DocApiDTO.class);
                String groupName = "数据结构";
                MenuGroupVo groupVo = menuTempMap.get(groupName);
                if (groupVo == null) {
                    groupVo = new MenuGroupVo();
                    groupVo.setSort(0);
                    groupVo.setTitle(groupName);
                    groupVo.setSubs(new ArrayList<MenuVo>());
                    menuTempMap.put(groupName, groupVo);
                }
                MenuVo vo = new MenuVo();
                vo.setTitle(ann.enName());
                vo.setName(ann.cnName());
                vo.setUrl(resolveUrl(clazz));
                vo.setGroup(ann.group());
                vo.setIsnew(resolveIsnew(clazz));
                groupVo.getSubs().add(vo);
            }
            if (clazz.isAnnotationPresent(DocApiService.class)) {
                DocApiService ann = clazz.getAnnotation(DocApiService.class);
                String groupName = "接口服务";
                MenuGroupVo groupVo = menuTempMap.get(groupName);
                if (groupVo == null) {
                    groupVo = new MenuGroupVo();
                    groupVo.setSort(1);
                    groupVo.setTitle(groupName);
                    groupVo.setSubs(new ArrayList<MenuVo>());
                    menuTempMap.put(groupName, groupVo);
                }
                MenuVo vo = new MenuVo();
                if (StringUtils.isBlank(ann.serviceName())) {
                    vo.setTitle(clazz.getName());
                } else {
                    vo.setTitle(ann.serviceName());
                }
                vo.setName(ann.cnName());
                vo.setUrl(resolveUrl(clazz));
                vo.setIsnew(resolveIsnew(clazz));
                vo.setGroup(ann.group());
                vo.setFinish(ann.finish());
                groupVo.getSubs().add(vo);
            }
        } catch (Throwable e) {
            throw new GenException("分析菜单异常，", clazz, e);
        }
    }

    private void resolveBasic(Class<?> clazz) throws GenException {
        try {
            if (!clazz.isAnnotationPresent(DocApiGlobalCode.class))
                return;

            DocApiGlobalCode ann = clazz.getAnnotation(DocApiGlobalCode.class);
            List<CodeVo> voList = basicMap.computeIfAbsent("code", s -> new ArrayList<>());

            Field[] fields = clazz.getDeclaredFields();
            if (fields == null || fields.length == 0)
                return;

            for (Field field : fields) {
                CodeVo vo = new CodeVo();
                Object obj = field.get(field.getName());
                String code = field.getType().getMethod("get" + StringUtils.capitalize(ann.codeKey())).invoke(obj).toString();
                vo.setCode(code);
                String msg = field.getType().getMethod("get" + StringUtils.capitalize(ann.msgKey())).invoke(obj).toString();
                vo.setDes(msg);
                voList.add(vo);
            }
        } catch (Throwable e) {
            throw new GenException("分析基础定义异常，", clazz, e);
        }
    }

    private void resolveDto(Class<?> clazz) throws GenException {
        try {
            if (!clazz.isAnnotationPresent(DocApiDTO.class))
                return;

            DocApiDTO ann = clazz.getAnnotation(DocApiDTO.class);
            DtoVo vo = new DtoVo();
            vo.setCnName(ann.cnName());
            vo.setEnName(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
            vo.setDesc(ann.desc());
            vo.setDoc(ann.doc());

            // 字段
            final List<DtoDataVo> dataList = new ArrayList<DtoDataVo>();
            vo.setData(dataList);
            ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    if (!field.isAnnotationPresent(DocApiFiled.class))
                        return;
                    DocApiFiled def = field.getAnnotation(DocApiFiled.class);
                    if (def == null) {
                        return;
                    }
                    DtoDataVo dataVo = new DtoDataVo();
                    if (StringUtils.isNotBlank(def.param())) {
                        dataVo.setName(def.param());
                    } else {
                        dataVo.setName(field.getName());
                    }
                    if (StringUtils.isNotBlank(def.type())) {
                        dataVo.setType(def.type());
                    } else {
                        dataVo.setType(field.getType().getSimpleName());
                    }
                    dataVo.setDesc(def.desc());
                    dataVo.setRemark(def.remark());
                    dataVo.setRequired(String.valueOf(def.required()));
                    dataVo.setLink(def.link());
                    dataList.add(dataVo);
                }
            });
            // 时间轴
            List<TimelineVo> timelines = new ArrayList<TimelineVo>();
            resolveTimeline(clazz, timelines);
            vo.setTimelines(timelines);
            dtoMap.put(StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName(), vo);
        } catch (Throwable e) {
            throw new GenException("分析数据结构异常，", clazz, e);
        }
    }

    private void resolveService(Class<?> clazz) throws GenException {
        try {
            if (!clazz.isAnnotationPresent(DocApiService.class))
                return;

            DocApiService ann = clazz.getAnnotation(DocApiService.class);
            ServiceVo vo = new ServiceVo();
            vo.setCnName(ann.cnName());
            if (StringUtils.isBlank(ann.serviceName())) {
                vo.setServiceName(clazz.getName());
            } else {
                vo.setServiceName(ann.serviceName());

            }
            vo.setVersion(ann.version());
            vo.setMethod(StringUtils.join(ann.methods()));
            vo.setDesc(ann.desc());
            vo.setGroup(ann.group());
            vo.setDoc(ann.doc());
            vo.setFinish(ann.finish());
            //接口计数.
            basicInfo.setIncCount(basicInfo.getIncCount() + 1);
            if (ann.finish() == 100) {
                basicInfo.setFinishCount(basicInfo.getFinishCount() + 1);
            }
            // 入参
            final List<ServiceDataVo> requestList = new ArrayList<ServiceDataVo>();
            vo.setRequests(requestList);
            if (clazz.isAnnotationPresent(DocApiIn.class)) {
                DocApiIn def = clazz.getAnnotation(DocApiIn.class);
                for (DocApiFiled in : def.value()) {
                    ServiceDataVo dataVo = new ServiceDataVo();
                    dataVo.setRequired(in.remark());
                    dataVo.setName(in.param());
                    dataVo.setType(in.type());
                    dataVo.setDesc(in.desc());
                    dataVo.setRemark(in.remark());
                    dataVo.setLink(in.link());
                    requestList.add(dataVo);
                }
            }
            //如果没有使用APiIn注解使用了DocApiInDTO的注解
            if (!clazz.isAnnotationPresent(DocApiIn.class) && clazz.isAnnotationPresent(DocApiInDTO.class)) {
                DocApiInDTO def = clazz.getAnnotation(DocApiInDTO.class);
                ReflectionUtils.doWithFields(def.clazz(), new ReflectionUtils.FieldCallback() {
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        ServiceDataVo dataVo = getDataVoByDtoField(field, def.clazz());
                        if (dataVo == null) {
                            return;
                        }
                        requestList.add(dataVo);
                    }
                });
            }

            // 出参
            final List<ServiceDataVo> responseList = new ArrayList<ServiceDataVo>();
            vo.setResponses(responseList);
            if (clazz.isAnnotationPresent(DocApiOut.class)) {
                DocApiOut def = clazz.getAnnotation(DocApiOut.class);
                for (DocApiFiled out : def.value()) {
                    ServiceDataVo dataVo = new ServiceDataVo();
                    dataVo.setRequired(String.valueOf(out.required()));
                    dataVo.setName(out.param());
                    dataVo.setType(out.type());
                    dataVo.setDesc(out.desc());
                    dataVo.setRemark(out.remark());
                    dataVo.setLink(out.link());
                    responseList.add(dataVo);
                }
            }
            //如果没有使用DocApiOut注解,使用DocApiOutDTO注解
            if (!clazz.isAnnotationPresent(DocApiOut.class) && clazz.isAnnotationPresent(DocApiOutDTO.class)) {
                DocApiOutDTO def = clazz.getAnnotation(DocApiOutDTO.class);
                ReflectionUtils.doWithFields(def.clazz(), new ReflectionUtils.FieldCallback() {
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        ServiceDataVo dataVo = getDataVoByDtoField(field, def.clazz());
                        if (dataVo == null) {
                            return;
                        }
                        responseList.add(dataVo);
                    }
                });
            }
            //返回码
            if (clazz.isAnnotationPresent(DocApiCodes.class)) {
                List<CodeVo> codeVos = new ArrayList<>();
                DocApiCodes def = clazz.getAnnotation(DocApiCodes.class);
                for (DocApiCode code : def.value()) {
                    CodeVo codeVo = new CodeVo();
                    codeVo.setCode(code.code());
                    codeVo.setDes(code.msg());
                    codeVos.add(codeVo);
                }
                vo.setApiCodes(codeVos);
            }
            // 时间轴
            List<TimelineVo> timelines = new ArrayList<TimelineVo>();
            resolveTimeline(clazz, timelines);
            vo.setTimelines(timelines);
            serviceMap.put(vo.getServiceName() + "_" + vo.getVersion(), vo);
        } catch (Throwable e) {
            throw new GenException("分析接口服务异常，", clazz, e);
        }
    }

    /**
     * 功过字段注解生成serviceDataVo实体
     *
     * @param f
     * @return
     */
    private ServiceDataVo getDataVoByDtoField(Field f, Class dtoClass) {
        DocApiFiled basicField = f.getAnnotation(DocApiFiled.class);
        if (basicField == null) {
            return null;
        }
        ServiceDataVo dataVo = new ServiceDataVo();
        dataVo.setRequired(String.valueOf(basicField.required()));
        if (StringUtils.isBlank(basicField.param())) {
            dataVo.setName(f.getName());
        } else {
            dataVo.setName(basicField.param());
        }
        ResolvableType type = ResolvableType.forField(f);
        dataVo.setRef(type);
        if (StringUtils.isBlank(basicField.type())) {
            if (Collection.class.isAssignableFrom(f.getType()) && dtoClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] dtoParams = ((ParameterizedType) dtoClass.getGenericSuperclass()).getActualTypeArguments();
                if (dtoParams != null && dtoParams.length > 0) {
                    String[] typeArray = dtoParams[0].getTypeName().split("\\.");
                    String finalType = typeArray[typeArray.length - 1];
                    dataVo.setType(finalType + "[]");
                }
            } else {
                dataVo.setType(f.getType().getSimpleName());
            }
        } else {
            dataVo.setType(basicField.type());
        }
        if (StringUtils.isBlank(basicField.link())) {
            if (Collection.class.isAssignableFrom(f.getType()) && dtoClass.getGenericSuperclass() instanceof ParameterizedType) {
                Type[] dtoParams = ((ParameterizedType) dtoClass.getGenericSuperclass()).getActualTypeArguments();
                if (dtoParams != null && dtoParams.length > 0) {
                    String[] typeArray = dtoParams[0].getTypeName().split("\\.");
                    String finalType = typeArray[typeArray.length - 1];
                    dataVo.setLink(finalType);
                }
            }
        } else {
            dataVo.setLink(basicField.link());
        }
        dataVo.setDesc(basicField.desc());
        dataVo.setRemark(basicField.remark());
        return dataVo;
    }

    private String resolveUrl(Class<?> clazz) {
        if (clazz.isAnnotationPresent(DocApiGlobalCode.class)) {
            return "/basic-definition";
        } else if (clazz.isAnnotationPresent(DocApiDTO.class)) {
            DocApiDTO ann = clazz.getAnnotation(DocApiDTO.class);
            return "/dto/" + (StringUtils.isNotBlank(ann.enName()) ? ann.enName() : clazz.getSimpleName());
        } else if (clazz.isAnnotationPresent(DocApiService.class)) {
            DocApiService ann = clazz.getAnnotation(DocApiService.class);
            return "/service/" + (StringUtils.isBlank(ann.serviceName()) ? clazz.getName() : ann.serviceName()) + "_" + ann.version();
        } else {
            return "";
        }
    }

    private String resolveIsnew(Class<?> clazz) throws GenException {
        try {
            if (!clazz.isAnnotationPresent(DocApiTimeline.class))
                return "";

            DocApiTimeline ann = clazz.getAnnotation(DocApiTimeline.class);
            if (ann == null || ann.value() == null || ann.value().length == 0)
                return "";

            for (DocTimeline timeline : ann.value()) {
                Date date = TIMELINE_SDF.parse(timeline.time());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, NEW_TIMELINE_DAY);

                Date now = new Date();
                if (cal.getTime().getTime() >= now.getTime()) {
                    return "1";
                }
            }

            return "";
        } catch (Throwable e) {
            throw new GenException("分析时间轴是否更新时异常，", clazz, e);
        }
    }

    private boolean checkLength(Integer... list) {
        if (list == null || list.length == 0) return true;

        boolean eq = true;
        Integer cur = list[0];
        for (int i = 1; i < list.length; i++) {
            eq = (list[i].equals(cur));
            if (!eq) break;
            cur = list[i];
        }
        return eq;
    }

}
