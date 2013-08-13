package com.heroku.platform.api.client.spray

import com.heroku.platform.api._



abstract class AccountSpec(aj: ApiRequestJson with ApiResponseJson) extends ApiSpec(aj){

  val implicits: AccountRequestJson with AccountResponseJson = aj

  import implicits._

  "Api for Account" must {
    "operate on the Account" in {
      val acct = info(Account.Info())
      val tracking = acct.allow_tracking
      val updated = update(Account.Update(allow_tracking = Some(!tracking)))
      updated.allow_tracking must not be (tracking)
    }
  }

}

class SprayAccountSpec extends AccountSpec(SprayJsonBoilerplate) with SprayApiSpec

class PlayAccountSpec extends AccountSpec(PlayJsonBoilerplate) with SprayApiSpec

