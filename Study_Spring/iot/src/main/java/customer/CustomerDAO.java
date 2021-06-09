package customer;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
//규칙2. DAO를 만들엇으면 무조건 @Repository 어노테이션으로 
//Spring이 DAO를 인식할수있게 만들어주면된다.
@Repository
public class CustomerDAO implements CustomerService{
	
	@Autowired @Qualifier("hanul") private SqlSession sql;
	
	@Override
	public void customer_insert(CustomerVO vo) {
		//sql.insert(mapper);
		// TODO Auto-generated method stub
		System.out.println(vo.getId());
		sql.insert("cu.mapper.insert" , vo);
		//jsp - > controller(mapping)
		//service(작업지시서) -> serviceimpl(service)"--" -> dao 
		
		//리턴을 꼭 주고싶다 int형으로 왜 리턴안하나..
		
	}

	@Override
	public List<CustomerVO> customer_list() {
		// TODO Auto-generated method stub
		return sql.selectList("cu.mapper.list");
	}

	@Override
	public CustomerVO customer_detail(String id) {
		// TODO Auto-generated method stub
		return sql.selectOne("cu.mapper.detail" , id);
	}

	@Override
	public void costomer_update(CustomerVO vo) {
		// TODO Auto-generated method stub
		sql.update("cu.mapper.update" , vo);
	}

	@Override
	public void customer_delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
