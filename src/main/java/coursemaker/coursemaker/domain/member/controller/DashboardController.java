package coursemaker.coursemaker.domain.member.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	@GetMapping(value="/")
	public String dashboard() {
		return "/dashboard";
	}

	@GetMapping(value="/user")
	public String user() {
		return "/user";
	}

	@GetMapping(value="/traveler")
	public String manager() {
		return "/traveler";
	}

	@GetMapping(value="/admin")
	public String admin() {
		return "/admin";
	}

	@GetMapping(value="/api")
	public String restDashboard() {
		return "rest/dashboard";
	}
}