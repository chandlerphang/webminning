package xyz.chandlerph.webminning.spider.service;

import org.apache.ibatis.session.SqlSession;

import xyz.chandlerph.webminning.spider.MyBatisSqlSessionFactory;
import xyz.chandlerph.webminning.spider.domain.UserBaseInfo;
import xyz.chandlerph.webminning.spider.mappers.UserBaseInfoMapper;

public class UserBaseInfoServiceImpl implements UserBaseInfoService {

	@Override
	public int getBaseUsersAccount(){
		SqlSession sqlsession = MyBatisSqlSessionFactory.openSession();
		UserBaseInfoMapper mapper = sqlsession.getMapper(UserBaseInfoMapper.class);
		
		UserBaseInfo record = new UserBaseInfo();
		record.setId(11l);
		return mapper.selectCount(record);
	}
	
}
