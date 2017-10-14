package com.choice.wechat.persistence.reply;

import java.util.List;

import com.choice.wechat.domain.reply.KeyWord;

public interface KeyWordMapper {
	public List<KeyWord> findMsgFormKeyWord(String appid,String keyWord,String msgType);
}
