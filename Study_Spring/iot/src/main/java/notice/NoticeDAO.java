package notice;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeDAO implements NoticeService{
	@Autowired @Qualifier("hanul") private SqlSession sql;
	@Override
	public void notice_insert(NoticeVO vo) {
		sql.insert("notice.mapper.insert" , vo);
		
	}

	@Override
	public void notice_reply_insert(NoticeVO vo) {
		sql.insert("notice.mapper.reply_insert" , vo);
		
	}

	@Override
	public List<NoticeVO> notice_list() {
		
		return sql.selectList("notice.mapper.list");
	}

	@Override
	public NoticeVO notice_view(int id) {
		// TODO Auto-generated method stub
		return sql.selectOne("notice.mapper.view" , id);
	}

	@Override
	public void notice_read(int id) {
		sql.update("notice.mapper.readcnt" , id);
		
	}

	@Override
	public void notice_update(NoticeVO vo) {
		sql.update("notice.mapper.update" , vo);
		
	}

	@Override
	public void notice_delete(int id) {
		sql.delete("notice.mapper.delete" , id);
		
	}

	@Override
	public NoticePage notice_list(NoticePage page) {
		//총 글 갯수를 가져와서 setTotalList
		page.setTotalList( sql.selectOne("notice.mapper.totalList" , page) );
		//총 글 내용을 가지고 올것
		page.setList( sql.selectList("notice.mapper.list" , page) );
		return page;
	}

}
