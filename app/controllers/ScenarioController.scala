package controllers

import javax.inject.Inject
import play.api.mvc._

class ScenarioController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

	def getHospLst() = Action {
		Ok(views.html.index())
	}

	def getBudgetInfo() = Action {
		Ok(views.html.index())
	}

	def getHumansInfo() = Action {
		Ok(views.html.index())
	}

	def getHospDetail() = Action {
		Ok(views.html.index())
	}

}