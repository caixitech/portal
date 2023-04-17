package in.caixi.outside.portal.web.controller;

import in.caixi.outside.portal.web.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class IndexController {
    @Resource
    private GoodsService goodsService;

    @RequestMapping({"/index", ""})
    public String index(ModelMap modelMap) {
        modelMap.put("goods", goodsService.list());
        return "index";
    }

}
