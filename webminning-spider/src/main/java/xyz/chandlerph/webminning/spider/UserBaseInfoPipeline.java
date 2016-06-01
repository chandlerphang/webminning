
package xyz.chandlerph.webminning.spider;

import org.apache.ibatis.session.SqlSession;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import xyz.chandlerph.webminning.spider.domain.UserBaseInfo;
import xyz.chandlerph.webminning.spider.mappers.UserBaseInfoMapper;

public class UserBaseInfoPipeline implements PageModelPipeline<UserBaseInfo> {

	@Override
	public void process(UserBaseInfo t, Task task) {
		SqlSession sqlsession = MyBatisSqlSessionFactory.openSession();

		try {
			UserBaseInfoMapper mapper = sqlsession.getMapper(UserBaseInfoMapper.class);
			mapper.insertSelective(t);
			sqlsession.commit();
		}
		finally {
			sqlsession.close();
		}

	}

}
