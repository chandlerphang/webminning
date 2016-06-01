package xyz.chandlerph.webminning.spider.mappers;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import xyz.chandlerph.webminning.spider.domain.UserDetailInfo;

/**
 * 
 * @author Chandler Phang
 */
public interface UserDetailInfoMapper extends Mapper<UserDetailInfo> {

	List<UserDetailInfo> getBusinessStatic(Integer selectLimitAmount);

	List<UserDetailInfo> getEmploymentStatic(Integer selectLimitAmount);

	List<UserDetailInfo> getEducationStatic(Integer selectLimitAmount);
	
}
