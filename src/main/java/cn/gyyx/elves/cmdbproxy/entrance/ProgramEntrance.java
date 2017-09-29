package cn.gyyx.elves.cmdbproxy.entrance;

import cn.gyyx.elves.util.ExceptionUtil;
import cn.gyyx.elves.util.SpringUtil;
import cn.gyyx.elves.util.mq.PropertyLoader;
import cn.gyyx.elves.util.zk.ZookeeperExcutor;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName: ProgramEntrance
 * @Description: elves-dashboard 程序启动入口
 * @author East.F
 * @date 2017年9月29日 15:58:45
 */
public class ProgramEntrance {
	
	private static final Logger LOG=Logger.getLogger(ProgramEntrance.class);

	/**
	 * 加载所有配置文件的路径
	 */
	private static void loadAllConfigFilePath(String configPath){
		SpringUtil.SPRING_CONFIG_PATH="file:"+configPath+File.separator+"conf"+File.separator+"spring.xml";
		SpringUtil.RABBITMQ_CONFIG_PATH="file:"+configPath+File.separator+"conf"+File.separator+"rabbitmq.xml";
		SpringUtil.PROPERTIES_CONFIG_PATH=configPath+File.separator+"conf"+File.separator+"conf.properties";
		SpringUtil.LOG4J_CONFIG_PATH=configPath+File.separator+"conf"+File.separator+"log4j.properties";
		LOG.info("load AllConfig FilePath success!");
	}
	
	/**
	 * 加载日志配置文件
	 */
	private static void loadLogConfig() throws Exception{
		InputStream in=new FileInputStream(SpringUtil.LOG4J_CONFIG_PATH);// 自定义配置
		PropertyConfigurator.configure(in);
		LOG.info("load LogConfig success!");
	}
	
	/**
	 * 加载Spring配置文件
	 */
	private static void loadApplicationXml() throws Exception{
		SpringUtil.app = new FileSystemXmlApplicationContext(SpringUtil.SPRING_CONFIG_PATH,SpringUtil.RABBITMQ_CONFIG_PATH);
		LOG.info("load Application Xml success!");
	}
	
	/**
	 * @Title: registerZooKeeper
	 * @Description: 注册zookeeper服务
	 * @throws Exception 设定文件
	 * @return void    返回类型
	 */
	private static void registerZooKeeper() throws Exception{
        if("true".equalsIgnoreCase(PropertyLoader.ZOOKEEPER_ENABLED)){
            LOG.info("regist zookeeper ...."+PropertyLoader.ZOOKEEPER_HOST);
            ZookeeperExcutor zke=new ZookeeperExcutor(PropertyLoader.ZOOKEEPER_HOST,
                    PropertyLoader.ZOOKEEPER_OUT_TIME, PropertyLoader.ZOOKEEPER_OUT_TIME);

            //创建模块根节点
            if(null==zke.getClient().checkExists().forPath(PropertyLoader.ZOOKEEPER_ROOT)){
                zke.getClient().create().creatingParentsIfNeeded().forPath(PropertyLoader.ZOOKEEPER_ROOT);
            }
            if(null==zke.getClient().checkExists().forPath(PropertyLoader.ZOOKEEPER_ROOT+"/scheduler")){
                zke.getClient().create().creatingParentsIfNeeded().forPath(PropertyLoader.ZOOKEEPER_ROOT+"/scheduler");
            }

            //创建节点
            String nodeName=zke.createNode(PropertyLoader.ZOOKEEPER_ROOT+"/scheduler/", "");
            if(null!=nodeName){
                //添加创建的节点监听，断线重连
                zke.addListener(PropertyLoader.ZOOKEEPER_ROOT+"/scheduler/", "");
            }
            LOG.info("registerZooKeeper success!");
        }
	}

	public static void main(String[] args) throws Exception {
		if(null!=args&&args.length>0){
			try {
				loadAllConfigFilePath(args[0]);

		    	loadLogConfig();

				loadApplicationXml();

				registerZooKeeper();
			} catch (Exception e) {
				LOG.error("start cmdbproxy error:"+ExceptionUtil.getStackTraceAsString(e));
				System.exit(1);
			}
    	}
	}
}