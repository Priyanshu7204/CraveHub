package com.example.demo.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entities.User;
import com.example.demo.dto.UserLogin;
import com.example.demo.services.EmailService;
import com.example.demo.services.OrderServices;
import com.example.demo.services.UserServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserServices userServices;
	@Autowired
	private EmailService emailService;
	@Autowired
	private OrderServices orderServices;

	// user registration and sending otp
	@PostMapping("/addingUser")
	public String addUser(@ModelAttribute("userRegistration") User user, HttpSession session) {

		// generating OTP from service layer
		String otp = this.userServices.generateOtp();
		user.setOtp(otp);
		user.setActive(false); // user will be active after otp verify

		this.userServices.addUser(user);

		try {
			emailService.sendOtpEmail(user.getUemail(), otp);
			System.out.println("OTP email sent successfully to: " + user.getUemail());
		} catch (Exception e) {
			System.out.println("Failed to send OTP email !!");
			e.printStackTrace();
		}

		session.setAttribute("verifyingEmail", user.getUemail());

		return "redirect:/verifyOtpPage";
	}

	// show otp page
	@GetMapping("/verifyOtpPage")
	public String renderOtpPage(HttpSession session) {
		if (session.getAttribute("verifyingEmail") == null) {
			return "redirect:/register";
		}
		return "verify_otp";
	}

	// check if user entered correct otp
	@PostMapping("/verifyOtpProcess")
	public String verifyOtpProcess(@RequestParam("otp") String otp, HttpSession session, Model model) {
		String email = (String) session.getAttribute("verifyingEmail");
		if (email == null) {
			return "redirect:/register";
		}

		User user = this.userServices.getUserByEmail(email);

		if (user != null && user.getOtp() != null && user.getOtp().equals(otp)) {
			user.setActive(true);
			user.setOtp(null); // remove otp after success
			this.userServices.addUser(user);

			session.removeAttribute("verifyingEmail");
			return "Register_Success";
		}

		model.addAttribute("error", "Invalid OTP! Please try again.");
		return "verify_otp";
	}

	// normal user login 
	@PostMapping("/userLogin")
	public String handleUserLogin(@ModelAttribute("userLogin") UserLogin login, Model model, HttpSession session) {

		if (login.getUserEmail() == null) {
			model.addAttribute("error2", "Form Data Missing!");
			return "Login";
		}

		boolean isValid = userServices.validateLoginCredentials(login.getUserEmail(), login.getUserPassword());

		if (isValid) {
			User user = userServices.getUserByEmail(login.getUserEmail());

			if (!user.isActive()) {
				model.addAttribute("error2", "Your account is blocked by Admin.");
				return "Login";
			}

			session.setAttribute("activeUser", user);

			// spring security authentication
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUemail(),
					null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
			SecurityContextHolder.getContext().setAuthentication(authToken);

			return "redirect:/product/back";
		}

		model.addAttribute("error2", "Invalid User Credentials");
		return "Login";
	}

	// handle google login and manage session
	@GetMapping("/product/back")
	public String syncUserSession(Model model, HttpSession session, @AuthenticationPrincipal OAuth2User oauthUser) {
		User user = (User) session.getAttribute("activeUser");

		if (oauthUser != null && user == null) {
			String email = oauthUser.getAttribute("email");
			user = userServices.getUserByEmail(email);

			if (user == null) {
				// new google user, need more details
				session.setAttribute("googleEmail", email);
				session.setAttribute("googleName", oauthUser.getAttribute("name"));
				return "redirect:/completeProfile";
			}
			if (!user.isActive()) {
				return "redirect:/login";
			}
			session.setAttribute("activeUser", user);
		}

		if (user != null) {
			model.addAttribute("orders", orderServices.getOrdersForUser(user));
			model.addAttribute("name", user.getUname());
			return "BuyProduct";
		}
		return "redirect:/login";
	}

	// show complete profile page for new google users
	@GetMapping("/completeProfile")
	public String renderProfileCompletion(HttpSession session, Model model) {
		if (session.getAttribute("googleEmail") == null) {
			return "redirect:/login";
		}
		model.addAttribute("email", session.getAttribute("googleEmail"));
		model.addAttribute("name", session.getAttribute("googleName"));
		return "CompleteProfile";
	}

	// save extra details of google user
	@PostMapping("/saveGoogleUser")
	public String finalizeGoogleRegistration(@RequestParam("unumber") Long phone, HttpSession session) {
		String email = (String) session.getAttribute("googleEmail");
		if (email != null) {
			User user = new User();
			user.setUemail(email);
			user.setUname((String) session.getAttribute("googleName"));
			user.setUpassword("OAUTH2_USER");
			user.setUnumber(phone);
			user.setActive(true);

			userServices.addUser(user);
			session.setAttribute("activeUser", user);
			session.removeAttribute("googleEmail");
		}
		return "redirect:/product/back";
	}
}