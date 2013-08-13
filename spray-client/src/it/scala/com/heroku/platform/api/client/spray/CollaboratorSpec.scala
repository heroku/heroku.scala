package com.heroku.platform.api.client.spray


import com.heroku.platform.api._


abstract class CollaboratorSpec(aj: ApiRequestJson with ApiResponseJson) extends SprayApiSpec(aj) {

  val implicits:CollaboratorRequestJson with CollaboratorResponseJson = aj

  import implicits._

  "Api for Collaborator" must {
    "operate on Collaborators" in {
      val app = getApp
      val collabAdd = create(Collaborator.Create(app.id, testCollaborator))
      val collabList = listPage(Collaborator.List(app.id))
      collabList.isComplete must be(true)
      collabList.list.isEmpty must be(false)
      collabList.list.contains(collabAdd) must be(true)
      val collabInfo = info(Collaborator.Info(app.id, collabAdd.id))
      collabInfo must equal(collabAdd)
      delete(Collaborator.Delete(app.id, collabAdd.id))
    }
  }

}


class SprayCollaboratorSpec extends CollaboratorSpec(SprayJsonBoilerplate)


class PlayCollaboratorSpec extends CollaboratorSpec(PlayJsonBoilerplate)

