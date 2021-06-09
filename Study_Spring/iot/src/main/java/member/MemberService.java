package member;

import java.util.HashMap;

public interface MemberService {
	boolean member_join(MemberVO vo); //회원가입 시 회원 정보 저장
	MemberVO member_select(String id); // 로그인 된 회원이 회원정보 조회
	MemberVO member_login(HashMap<String, Object> map); //회원 로그인 처리
	boolean member_update(MemberVO vo); //회원 정보 수정
	boolean member_delete(String id); //회원 탈퇴
	boolean member_id_check(String id); // 회원 가입시 있는 아이디 인지 체크
	
	
	//추후 추가 예정 --
	boolean member_social_id(MemberVO vo); //소셜로그인시(네이버,카카오) 회원의 존재여부
	boolean member_social_insert(MemberVO vo); //소셜로그인시 회원 신규저장
	boolean member_social_update(MemberVO vo);//소셜로그인시 회원 정보 변경
}
