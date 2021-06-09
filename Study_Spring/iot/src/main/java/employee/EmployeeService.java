package employee;

 

import java.util.List;

 

public interface EmployeeService {

	List<EmployeeVO> employee_list(); //사원목록조회
	List<EmployeeVO> employee_list(String depts); //사원목록조회(부서목록을선택한경우)	
	EmployeeVO employee_detail(int id);//사원상세조회
	List<DepartmentVO> employee_department(); //부서목록조회

}