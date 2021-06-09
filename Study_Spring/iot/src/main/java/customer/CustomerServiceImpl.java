package customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//개발자가 PM이 만들어준 Service.class 받음

//어노테이션 규칙 Service를 implements 받은 (개발자가사용하는class)
//에서 무조건 @Service 어노테이션을 줘야된다.
@Service
public class CustomerServiceImpl  implements CustomerService{
	@Autowired private CustomerDAO dao;
	@Override
	public void customer_insert(CustomerVO vo) {
		// TODO Auto-generated method stub
		dao.customer_insert(vo);
	}

	@Override
	public List<CustomerVO> customer_list() {
		// TODO Auto-generated method stub
		return dao.customer_list();
	}

	@Override
	public CustomerVO customer_detail(String id) {
		// TODO Auto-generated method stub
		return dao.customer_detail(id);
	}

	@Override
	public void costomer_update(CustomerVO vo) {
		// TODO Auto-generated method stub
		 dao.costomer_update(vo);
		
	}

	@Override
	public void customer_delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
