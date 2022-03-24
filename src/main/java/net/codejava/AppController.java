package net.codejava;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

	
	
	@Autowired
	private UserRepository repos;
	
	@Autowired
	private ProductService service; 
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("admin")
	public String adminPage(Model model) {
		List<Product> listProducts = service.listAll();
		model.addAttribute("listProducts", listProducts);
		
		return "admin";
	}
	@RequestMapping("/")
	public String viewHomePage() {
		return "index";
	}
	
	
	@RequestMapping("/new")
	public String showNewProductPage(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		
		return "new_product";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);
		
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("edit_product");
		Product product = service.get(id);
		mav.addObject("product", product);
		
		return mav;
	}
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public ModelAndView method(String name) {
		if (name == "" || name == null)
		{
			String projectUrl = "https://api.whatsapp.com/send?phone=+905304711111";
			return new ModelAndView("redirect:" + projectUrl);
		}
		else {
			User user = userRepository.getUserByUsername(name);
			if(user == null) {
				return new ModelAndView("index");
			}
			else {
				String number=user.getMobile();
			String projectUrl = "https://api.whatsapp.com/send?phone=+9"+number;
		    return new ModelAndView("redirect:" + projectUrl);
			}
		}
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") int id) {
		service.delete(id);
		return "redirect:/";		
	}
	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	

	@RequestMapping("/videochat")
	public String startVideoChat() {
		
		return "videochat2";
		
	}
	
	@RequestMapping("/chatbox")
	public String startChat() {
		
		return "chatbox";
		
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		repos.save(user);
		return "register_success";
		
	}
}
