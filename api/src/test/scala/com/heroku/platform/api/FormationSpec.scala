package com.heroku.platform.api

abstract class FormationSpec(aj: ApiRequestJson with ApiResponseJson) extends ApiSpec(aj) {

  val implicits: FormationRequestJson with FormationResponseJson = aj

  import implicits._

  "Api for Formations" must {
    "operate on Formations" in {
      val app = getApp
      val formations = listAll(Formation.List(app.name))
      //TODO better tests, need to create releases from a test slug
      formations.size must equal(0)
    }
  }

}

