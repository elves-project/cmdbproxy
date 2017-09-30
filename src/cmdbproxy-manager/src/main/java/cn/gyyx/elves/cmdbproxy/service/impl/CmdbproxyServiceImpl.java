package cn.gyyx.elves.cmdbproxy.service.impl;

import cn.gyyx.elves.cmdbproxy.dao.CmdbproxyDao;
import cn.gyyx.elves.cmdbproxy.service.CmdbproxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : east.Fu
 * @Description :
 * @Date : Created in  2017/9/30 15:09
 */
@Service("elvesConsumerService")
public class CmdbproxyServiceImpl implements CmdbproxyService{

    @Autowired
    private CmdbproxyDao cmdbproxyDao;

    @Override
    public Map<String, Object> getAgentInfoUril() {
        Map<String,Object> data = new HashMap<String,Object>();

        data.put("url","");
        return data;
    }


}
