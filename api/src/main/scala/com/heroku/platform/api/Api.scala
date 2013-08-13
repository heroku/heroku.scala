package com.heroku.platform.api

import concurrent.{ ExecutionContext, Future }
import scala.reflect.ClassTag

trait Api {

  implicit def executionContext: ExecutionContext

  def execute[T](request: Request[T], key: String)(implicit f: FromJson[T]): Future[Either[ErrorResponse, T]]

  def execute[I, O](request: RequestWithBody[I, O], key: String)(implicit to: ToJson[I], from: FromJson[O]): Future[Either[ErrorResponse, O]]

  def executeList[T](request: ListRequest[T], key: String)(implicit f: FromJson[List[T]]): Future[Either[ErrorResponse, PartialResponse[T]]]

  def executeListAll[T](request: ListRequest[T], key: String)(implicit f: FromJson[List[T]]): Future[Either[ErrorResponse, List[T]]] = listAll(request, key, List.empty)

  private def listAll[T](request: ListRequest[T], key: String, acc: List[T])(implicit f: FromJson[List[T]]): Future[Either[ErrorResponse, List[T]]] = {
    executeList(request, key).flatMap {
      case Left(e) => Future.successful(Left(e))
      case Right(p) if p.isComplete => Future.successful(Right(acc ++ p.list))
      case Right(p) => listAll(request.nextRequest(p.nextRange.get), key, acc ++ p.list)
    }
  }
}

trait ErrorResponseJson {
  implicit def errorResponseFromJson: FromJson[ErrorResponse]
}

trait ApiResponseJson extends HerokuAppResponseJson with AccountResponseJson with CollaboratorResponseJson
    with ConfigVarResponseJson with DomainResponseJson with DynoResponseJson with FormationResponseJson
    with KeyResponseJson with LogSessionResponseJson with RegionResponseJson with ReleaseResponseJson
    with OAuthResponseJson with AppTransferResponseJson with AddonResponseJson with ErrorResponseJson {
  implicit def userFromJson: FromJson[User]
}

trait ApiRequestJson extends AccountRequestJson with HerokuAppRequestJson with CollaboratorRequestJson with ConfigVarRequestJson
    with DomainRequestJson with DynoRequestJson with FormationRequestJson with KeyRequestJson with OAuthRequestJson with AppTransferRequestJson
    with AddonRequestJson {
  implicit def userBodyToJson: ToJson[UserBody]
}

case class User(id: String, email: String)

case class UserBody(id: Option[String] = None, email: Option[String] = None)

