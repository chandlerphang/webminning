package xyz.chandlerph.webminning.spider.mappers;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import xyz.chandlerph.webminning.spider.domain.UserBaseInfo;

/**
 * 
 * @author Chandler Phang
 */
public interface UserBaseInfoMapper extends Mapper<UserBaseInfo> {

	List<UserBaseInfo> getLocationStatic(Integer selectLimitAmount);

	int getAmountByReocordItem(UserBaseInfo record);

}
