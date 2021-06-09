package notice;

import java.util.List;

import org.springframework.stereotype.Component;

import common.PageVO;

//Page처리하는  어떤 기능을 처리하기 위한 class
@Component
public class NoticePage extends PageVO{
	private List<NoticeVO> list;

	public List<NoticeVO> getList() {
		return list;
	}

	public void setList(List<NoticeVO> list) {
		this.list = list;
	}
	
	
}
