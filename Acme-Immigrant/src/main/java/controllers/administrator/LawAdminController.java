package controllers.administrator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LawService;
import controllers.AbstractController;
import domain.Law;

@Controller
@RequestMapping("/law/admin")
public class LawAdminController extends AbstractController {

	// Services
	@Autowired
	LawService lawService;

	// Constructors (Debugueo)
	public LawAdminController() {
		super();
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int lawId) {
		ModelAndView result;
		final Law a;

		a = this.lawService.findOne(lawId);
		result = new ModelAndView("law/display");
		result.addObject("law", a);

		return result;
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Law v;

		v = this.lawService.create();

		result = this.createEditModelAndView(v);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int lawId) {

		ModelAndView result;
		Law a;

		a = this.lawService.findOne(lawId);
		result = this.createEditModelAndView(a);

		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Law law, final BindingResult br) {

		ModelAndView result;
		if (br.hasErrors())
			result = this.createEditModelAndView(law);
		else
			try {
				this.lawService.save(law);
				result = this.list();
			} catch (final Throwable ops) {
				result = this.createEditModelAndView(law, "law.commit.error");
			}

		return result;

	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		List<Law> laws = new ArrayList<>();

		laws = (List<Law>) this.lawService.findAll();
		result = new ModelAndView("law/list");
		result.addObject("requestUri", "law/admin/list.do");
		result.addObject("laws", laws);
		return result;
	}

	// Ancillary Methods
	protected ModelAndView createEditModelAndView(final Law law) {

		final ModelAndView result;

		result = this.createEditModelAndView(law, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Law law,
			final String messageCode) {

		ModelAndView result;

		result = new ModelAndView("law/edit");
		result.addObject("law", law);
		result.addObject("formURI", "law/admin/edit.do");
		result.addObject("messageCode", messageCode);

		return result;
	}

}