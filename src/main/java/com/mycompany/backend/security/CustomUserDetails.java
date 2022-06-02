package com.mycompany.backend.security;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
  //인증 정보로 추가로 저장하고 싶은 내용 정의
	private String mname;
	private String memail;
	
	public CustomUserDetails(
			String mid, 
			String mpassword, 
			boolean menabled, 
			List<GrantedAuthority> mauthorities,
			String mname,
			String memail) { //이 정보들은 userdetail에 생성자로 넘겨줌 
		super(mid, mpassword, menabled, true, true, true, mauthorities);
		this.mname = mname; //이 2개는 필드에 선언 되있기때문 
		this.memail = memail;
	}
	
	public String getMname() {
		return mname;
	}

	public String getMemail() {
		return memail;
	}
}

