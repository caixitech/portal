package org.helloframework.docs.mojo;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.helloframework.docs.annotation.DocApiDTO;
import org.helloframework.docs.annotation.DocApiInDTO;
import org.helloframework.docs.annotation.DocApiOutDTO;
import org.helloframework.docs.annotation.DocApiService;
import org.helloframework.docs.gen.ScanWrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OpenApiGen {
    private static final ModelResolver modelResolver;

    static {
        modelResolver = new ModelResolver();
    }

    private final TreeSet<Class<?>> componentSchemas = new TreeSet<>((o1, o2) -> ObjectUtils.compare(o1.getName(), o2.getName()));
    private final TreeSet<Class<?>> services = new TreeSet<>((o1, o2) -> ObjectUtils.compare(o1.getName(), o2.getName()));

    public Schema<?> schema(Class<?> clazz) {
        return modelResolver.schema(clazz);
    }

    /**
     * 生产openApi标准的json文件
     */
    public String toJson(OpenAPI openAPI) {
        return Json.pretty(openAPI);
    }

    public void genFiles(OpenAPI openAPI, String destDirectory) throws IOException {
        String staticDir = OpenApiGen.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        CodeSource src = OpenApiGen.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            if (jar.getPath().endsWith("/")) {//目录方式
                FileUtils.copyDirectoryToDirectory(new File(staticDir + "/swagger-ui/"), new File(destDirectory));
            } else {//jar包生成方式
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith("com") || name.startsWith("META-INF")) {
                        continue;
                    }
                    if (e.isDirectory()) {
                        FileUtils.forceMkdir(new File(destDirectory + File.separator + name));
                    } else {
                        InputStream stream = this.getClass().getResourceAsStream(File.separator + name);
                        byte[] data = IOUtils.toByteArray(stream);
                        FileUtils.writeByteArrayToFile(new File(destDirectory + File.separator + name), data);
                    }
                }
            }
        }
        FileUtils.writeStringToFile(new File(destDirectory + "/swagger-ui/swagger.json"), toJson(openAPI), StandardCharsets.UTF_8);
    }

    public OpenAPI build(Info info, String... packageName) {
        scan(packageName);
        //1. paths
        OpenAPI openAPI = new OpenAPI();
        openAPI.setComponents(components());
        openAPI.setPaths(paths());
        openAPI.tags(tags());
        openAPI.info(info);
        return openAPI;
    }

    private List<Tag> tags() {
        List<Tag> tags = new LinkedList<>();
        Set<String> groups = new HashSet<>();
        for (Class<?> service : services) {
            DocApiService docApiService = service.getAnnotation(DocApiService.class);
            groups.add(docApiService.group());
        }
        for (String group : groups) {
            tags.add(new Tag().name(group));
        }
        return tags;
    }

    private Components components() {
        Components components = new Components();
        for (Class<?> componentSchema : componentSchemas) {
            Schema<?> schema = schema(componentSchema);
            components.addSchemas(schema.getName(), schema);
        }
        return components;
    }

    private Paths paths() {
        Paths paths = new Paths();
        for (Class<?> service : services) {
            String url = "/gateway/";
            DocApiService docApiService = service.getAnnotation(DocApiService.class);
            DocApiInDTO inDTO = service.getAnnotation(DocApiInDTO.class);
            DocApiOutDTO outDTO = service.getAnnotation(DocApiOutDTO.class);
            if (StringUtils.isNotBlank(docApiService.serviceName())) {
                url += docApiService.serviceName();
            } else {
                url += service.getName();
            }
            PathItem pathItem = new PathItem();
            Operation postOperation = new Operation();
            postOperation.setSummary(docApiService.cnName());
            postOperation.setDescription(docApiService.desc());
            postOperation.addTagsItem(docApiService.group());
            postOperation.parameters(defaultParameters());
            RequestBody requestBody = new RequestBody();
            Content content = new Content();
            MediaType mediaType = new MediaType();
            mediaType.setSchema(schema(inDTO.clazz()));
            content.addMediaType("application/json", mediaType);
            requestBody.content(content).required(true);
            postOperation.setRequestBody(requestBody);
            ApiResponses apiResponses = new ApiResponses();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setDescription(outDTO.desc());
            apiResponse.setContent(new Content().addMediaType("application/json", new MediaType().schema(schema(outDTO.clazz()))));
            apiResponses.addApiResponse("200", apiResponse);
            postOperation.setResponses(apiResponses);
            pathItem.post(postOperation);
            paths.addPathItem(url, pathItem);
        }
        return paths;
    }

    public List<Parameter> defaultParameters() {
        List<Parameter> list = new LinkedList<>();
        list.add(parameterOf("X-Ca-Version", "版本号", "1.0"));
        list.add(parameterOf("X-Ca-Nonce", "每次请求的uuid", UUID.randomUUID().toString()));
        list.add(parameterOf("X-Ca-Signature", "签名", "example"));
        list.add(parameterOf("X-Ca-Signature-Headers", "签名", "example"));
        list.add(parameterOf("Content-Type", "请求类型", "application/json"));
        return list;
    }

    private Parameter parameterOf(String name, String description, String example) {
        Parameter p = new Parameter();
        p.setIn("header");
        p.setDescription(description);
        p.setName(name);
        p.setRequired(true);
        p.setSchema(new StringSchema());
        p.setExample(example);
        return p;
    }

    @SuppressWarnings("unchecked")
    private void scan(String... packageName) {
        ScanWrapper scan = new ScanWrapper(packageName, DocApiDTO.class);
        try {
            Set<Class<?>> apiDtoSet = scan.getClassSet();
            componentSchemas.addAll(apiDtoSet);
            scan = new ScanWrapper(packageName, DocApiService.class);
            Set<Class<?>> serviceSet = scan.getClassSet();
            services.addAll(serviceSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
