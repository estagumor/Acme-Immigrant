package controllers.officer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ApplicationService;
import services.OfficerService;
import services.QuestionService;
import controllers.AbstractController;
import domain.Question;

@Controller
@RequestMapping("/question/officer")
public class QuestionOfficerController extends AbstractController {

	// Services
	@Autowired
	QuestionService questionService;

	@Autowired
	OfficerService officerService;

	@Autowired
	ApplicationService applicationService;

	// Constructors (Debugueo)
	public QuestionOfficerController() {
		super();
	}

	// Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int questionId) {
		ModelAndView result;
		final Question a;

		a = this.questionService.findOne(questionId);
		result = new ModelAndView("question/display");
		result.addObject("question", a);

		return result;
	}

	// // Create
	// @RequestMapping(value = "/create", method = RequestMethod.GET)
	// public ModelAndView create(@RequestParam final int applicationId) {
	// ModelAndView result;
	// final Question q;
	//
	// q = this.questionService.create();
	// if (q.getApplication().getOfficer() != this.officerService
	// .getActorByUA(LoginService.getPrincipal())) {
	// result = new ModelAndView("application/display");
	// result.addObject("applicationId", applicationId);
	// result.addObject("message", "question.commit.error");
	// }
	// result = this.createEditModelAndView(q);
	//
	// return result;
	// }

	// Create V2
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@RequestParam final int applicationId,
			@RequestParam final String statement) {
		ModelAndView result;
		final Question q;

		if (statement == null || statement == "") {
			result = new ModelAndView(
					"redirect:/application/officer/display.do?applicationId="
							+ applicationId);
			result.addObject("errorMessage", "question.create.error");
			return result;
		}

		q = this.questionService.create(applicationId, statement);
		if (q.getApplication().getOfficer() != this.officerService
				.getActorByUA(LoginService.getPrincipal())) {
			result = new ModelAndView("application/display");
			result.addObject("applicationId", applicationId);
			result.addObject("message", "question.commit.error");
			return result;
		} else
			try {
				q.setStatement(statement);
				this.questionService.save(q);
				result = new ModelAndView(
						"redirect:/application/officer/display.do?applicationId="
								+ applicationId);
			} catch (final Throwable ops) {
				result = new ModelAndView("application/display");
				result.addObject("applicationId", applicationId);
				result.addObject("message", "question.commit.error");
			}

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int questionId) {

		ModelAndView result;
		Question q;

		q = this.questionService.findOne(questionId);
		if (q.getApplication().getOfficer() != this.officerService
				.getActorByUA(LoginService.getPrincipal())) {
			result = this.display(questionId);
			result.addObject("message", "question.commit.error");
		}
		result = this.createEditModelAndView(q);

		return result;
	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Question question,
			final BindingResult br) {

		ModelAndView result;
		if (br.hasErrors())
			result = this.createEditModelAndView(question);
		else
			try {
				this.questionService.save(question);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable ops) {
				result = this.createEditModelAndView(question,
						"question.commit.error");
			}

		return result;

	}

	// Ancillary Methods
	protected ModelAndView createEditModelAndView(final Question q) {

		final ModelAndView result;

		result = this.createEditModelAndView(q, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Question q,
			final String messageCode) {

		ModelAndView result;

		result = new ModelAndView("question/edit");
		result.addObject("question", q);
		result.addObject("formURI", "officer/question/edit.do");
		result.addObject("messageCode", messageCode);

		return result;
	}

}