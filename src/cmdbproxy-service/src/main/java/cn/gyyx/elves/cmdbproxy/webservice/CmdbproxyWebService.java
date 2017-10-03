package cn.gyyx.elves.cmdbproxy.webservice;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : east.Fu
 * @Description : http接口服务
 * @Date : Created in  2017/9/29 16:50
 */
@Controller
@RequestMapping("/")
public class CmdbproxyWebService {

    private static final Logger LOG = Logger.getLogger(CmdbproxyWebService.class);

    @RequestMapping("/{module}/{version}/{group}/{method}")
    public void api(@PathVariable("module") String module, @PathVariable("version") String version ,
                    @PathVariable("group") String group, @PathVariable("method") String method, HttpServletRequest request, Model model) {

        LOG.info("client 请求接口：" + request.getRequestURL());

        model.addAttribute("result", "");
    }
}
