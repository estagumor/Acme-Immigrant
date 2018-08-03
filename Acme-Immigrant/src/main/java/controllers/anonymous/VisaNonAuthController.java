package controllers.anonymous;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.VisaService;
import controllers.AbstractController;
import domain.Visa;
import forms.SearchVisa;

@Controller
@RequestMapping("/visa")
public class VisaNonAuthController extends AbstractController {

	// Services
	@Autowired
	VisaService visaService;

	// Constructors (Debugueo)
	public VisaNonAuthController() {
		super();
	}

	// Search
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(final SearchVisa searchVisa) {
		ModelAndView result;
		List<Visa> visas = new ArrayList<>();

		if (searchVisa.getKeyword() != null)
			visas = this.visaService.searchVisaByKeyword(searchVisa
					.getKeyword());
		else
			visas = this.visaService.getAvailableVisas();
		result = new ModelAndView("visa/list");
		result.addObject("requestUri", "visa/search.do");
		result.addObject("visas", visas);
		return result;
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int visaId) {
		ModelAndView result;
		Visa visa;

		visa = this.visaService.findOne(visaId);
		result = new ModelAndView("visa/display");
		result.addObject("visa", visa);
		return result;
	}

}