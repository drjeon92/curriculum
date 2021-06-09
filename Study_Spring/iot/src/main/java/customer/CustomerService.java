package customer;

import java.util.List;

public interface CustomerService {
	//DTO <-> VO 
	//DataTransferObject Value Object
	
	//Vo를 파라메터로 넘길 예정
	void customer_insert(CustomerVO vo);
	
	//ArrayList , List 이용해서 DTO를 전부 담을 예정.
	List<CustomerVO> customer_list();
	
	//고객 상세 정보 number oracle String 바꿀예정
	CustomerVO customer_detail(String id);
	
	//DTO를 이용해서 수정할 예정
	void costomer_update(CustomerVO vo);
	
	
	//삭제를 하는데 id값을 이용하여 삭제 할 예정 2021-05-11 
	//2021-05-12 : String 확정했다. 
	void customer_delete(String id);
}
