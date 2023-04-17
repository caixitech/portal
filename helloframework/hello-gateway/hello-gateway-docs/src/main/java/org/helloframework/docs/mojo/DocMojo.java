package org.helloframework.docs.mojo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.helloframework.docs.gen.ApiInfo;
import org.helloframework.docs.gen.Gen;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.lang3.time.DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT;

public class DocMojo {

    /**
     * output dir
     */
    private File outputDirectory;

    /**
     * skip hello doc generation method.
     */
    private boolean skipGeneration;

    /**
     * scan service define package.
     */
    private String[] scanPackage;

    /**
     * appname
     */
    private String appName;

    /**
     * copyright
     */
    private String copyright;

    private String docDirectory;

    private boolean generateJs;
    private Header[] headers;
    private String[] tips;
    private Resp[] resps;

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSkipGeneration(boolean skipGeneration) {
        this.skipGeneration = skipGeneration;
    }

    public void setScanPackage(String[] scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setDocDirectory(String docDirectory) {
        this.docDirectory = docDirectory;
    }

    public void setGenerateJs(boolean generateJs) {
        this.generateJs = generateJs;
    }

    public String[] getTips() {
        return tips;
    }

    public void setTips(String[] tips) {
        this.tips = tips;
    }

    public Resp[] getResps() {
        return resps;
    }

    public void setResps(Resp[] resps) {
        this.resps = resps;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public void execute() {
        try {
            if (skipGeneration) {
                System.out.println("Hello Doc generation is skipped");
                return;
            }
            Gen gen = new Gen();
            String destDirectory = outputDirectory.getPath() + "/api-doc";
            gen.setDocRoot(destDirectory);
            gen.scanPakcages(scanPackage);
            ApiInfo.Builder builder = new ApiInfo.Builder()
                    .name(appName)
                    .copyright(copyright)
                    .enName(copyright)
                    .buildTime(ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()));
            if (headers != null) {
                for (Header header : headers) {
                    builder.header(header.getName(), header.getType(), header.getDesc(), header.getRemark(), header.isRequired());
                }
            }
            if (resps != null) {
                for (Resp resp : resps) {
                    builder.response(resp.getName(), resp.getType(), resp.getDesc(), resp.getRemark(), resp.isRequired());
                }
            }
            if (tips != null) {
                for (String tip : tips) {
                    builder.tip(tip);
                }
            }
            ApiInfo apiInfo = builder.build();
            gen.initInfo(apiInfo);
            gen.handler();
            String staticDir = DocMojo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            CodeSource src = DocMojo.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                if (jar.getPath().endsWith("/")) {//目录方式
                    FileUtils.copyFileToDirectory(new File(staticDir + "/index.html"), new File(destDirectory));
                    FileUtils.copyDirectoryToDirectory(new File(staticDir + "/static"), new File(destDirectory));
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
//                            name = name.replaceFirst("static/", "");
                            FileUtils.forceMkdir(new File(destDirectory + File.separator + name));
                        } else {
                            InputStream stream = this.getClass().getResourceAsStream(File.separator + name);
                            byte[] data = IOUtils.toByteArray(stream);
//                            name = name.replaceFirst("static/", "");
                            FileUtils.writeByteArrayToFile(new File(destDirectory + File.separator + name), data);
                        }
                    }
                }
            } else {

            }
            //如果包含文档目录,也要复制到指定目录下
            if (!StringUtils.isEmpty(docDirectory)) {
                File fileDocDirectory = new File(docDirectory);
                if (!fileDocDirectory.exists()) {
                    File destDocDir = new File(destDirectory);
                    //如果目录不存在创建一个目录.
                    if (!destDocDir.exists()) {
                        FileUtils.forceMkdirParent(destDocDir);
                    }
                    if (fileDocDirectory.isDirectory()) {
                        for (File f : fileDocDirectory.listFiles()) {
                            if (f.isDirectory()) {
                                FileUtils.copyDirectoryToDirectory(f, destDocDir);
                            } else {
                                FileUtils.copyFileToDirectory(f, destDocDir);
                            }
                        }
                    } else {
                        FileUtils.copyFileToDirectory(fileDocDirectory, destDocDir);
                    }
                }
            }
//            if (generatePB) {
//                //生成proto文件
//                ProtoMain.generate(scanPackage, destDirectory);
//            }
            if (generateJs) {
                //生成interface-config.js文件
                InterfaceDataGen.generateInterface(scanPackage, destDirectory);
            }
        } catch (Throwable e) {
            throw new RuntimeException("生成接口文档失败", e);
        }
    }

    /**
     * 生成具有open api规范标准的文档
     *
     * @return
     */
//    private OpenApi buildOpenApi(ApiInfo apiInfo, Gen gen) {
//        gen.serviceMap.values().stream().map(s -> {
//            Path.PathBuilder builder = Path.PathBuilder.aPath();
//            Map<String, Media> reqContent = new HashMap<>();
//            Schema bodySchema = new Schema();
//            bodySchema.setType("object");
//            bodySchema.setRequired();
//            bodySchema.setProperties();
//            reqContent.put(MediaType.APPLICATION_JSON_VALUE, Media.MediaBuilder.aMedia().withSchema().build())
//            RequestBody requestBody = RequestBody.RequestBodyBuilder.aRequestBody()
//                    .withDescription(s.getDesc())
//                    .withContent()
//                    .build();
//            Operation operation = Operation.OperationBuilder.anOperation()
//                    .withRequestBody(requestBody)
//                    .withTags(Arrays.asList(s.getGroup()))
//                    .withOperationId(s.getServiceName() + "_" + s.getVersion()).build();
//            if (s.getMethod().contains("post")) {
//                builder.withPost()
//            }
//        })
//        OpenApi openApi = OpenApi.OpenApiBuilder.anOpenApi()
//                .withInfo(Info.InfoBuilder.anInfo().withTitle(apiInfo.getName()).withVersion("1.0").build())
//                .withOpenapi("3.0.3")
//                .build();
//        return openApi;
//    }
//
//    private Schema buildSchema(List<ServiceDataVo> serviceDataVos) {
//        Schema bodySchema = new Schema();
//        bodySchema.setType("object");
//        bodySchema.setRequired();
//        Map<String, Schema> subSchema = new HashMap<>();
//        for (ServiceDataVo vo : serviceDataVos) {
//            subSchema.put(vo.getName());
//        }
//        bodySchema.setProperties();
//    }
//
//    private Schema subSchema(ServiceDataVo serviceDataVo) {
//        String[] types = {"integer", "string", "object", "array", "number", "boolean"};
//        String type;
//        if (serviceDataVo.getRef().getType() instanceof Collection) {
//            type = types[3];
//        } else if (serviceDataVo.getRef().isAssignableFrom(Integer.class)) {
//            type = types[0];
//        } else if (serviceDataVo.getRef().isAssignableFrom(Number.class)) {
//            type = types[4];
//        } else if (serviceDataVo.getRef().isAssignableFrom(Boolean.class)) {
//            type = types[5];
//        } else if (serviceDataVo.getRef().isAssignableFrom(String.class)) {
//            type = types[1];
//        } else {
//            type = types[2];
//        }
//        Schema schema = Schema.SchemaBuilder.aSchema().withType(type).withDefaultVal(serviceDataVo.getDesc()).withDefaultVal(serviceDataVo.get)
//    }

}
