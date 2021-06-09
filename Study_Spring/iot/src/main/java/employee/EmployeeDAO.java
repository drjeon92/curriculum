package employee;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDAO implements EmployeeService{
	@Autowired @Qualifier("hr") private SqlSession sql;
	
	@Override
	public List<EmployeeVO> employee_list() {
		
		return sql.selectList("hr.mapper.list");
	}

	@Override
	public List<EmployeeVO> employee_list(String depts) {
		// TODO Auto-generated method stub
		return sql.selectList("hr.mapper.depts",depts);
	}

	@Override
	public EmployeeVO employee_detail(int id) {
		// TODO Auto-generated method stub
		return sql.selectOne("hr.mapper.detail" , id);
	}

	@Override
	public List<DepartmentVO> employee_department() {
		// TODO Auto-generated method stub
		return sql.selectList("hr.mapper.department");
	}

}
