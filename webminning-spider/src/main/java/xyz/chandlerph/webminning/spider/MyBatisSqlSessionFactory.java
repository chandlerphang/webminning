package xyz.chandlerph.webminning.spider;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import xyz.chandlerph.webminning.spider.mappers.CookiesMapper;
import xyz.chandlerph.webminning.spider.mappers.UserBaseInfoMapper;
import xyz.chandlerph.webminning.spider.mappers.UserDetailInfoMapper;

public class MyBatisSqlSessionFactory {

	private static class SqlSessionFactoryHolder {  
        private static final SqlSessionFactory INSTANCE;
        
        static {
        	InputStream inputStream;
			try {
				inputStream = Resources.getResourceAsStream("mybatis-config.xml");
				INSTANCE = new SqlSessionFactoryBuilder().build(inputStream);
				
				MapperHelper mapperHelper = new MapperHelper();
				mapperHelper.registerMapper(UserBaseInfoMapper.class);
				mapperHelper.registerMapper(UserDetailInfoMapper.class);
				mapperHelper.registerMapper(CookiesMapper.class);
				
				mapperHelper.processConfiguration(INSTANCE.getConfiguration());
				
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
        } 
    } 
	
	public static SqlSessionFactory getSqlSessionFactory() {
		return SqlSessionFactoryHolder.INSTANCE;
	}

	public static SqlSession openSession() {
		return getSqlSessionFactory().openSession();
	}
}
