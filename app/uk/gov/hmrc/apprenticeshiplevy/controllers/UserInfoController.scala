/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.apprenticeshiplevy.controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import uk.gov.hmrc.apprenticeshiplevy.config.MicroserviceAuthConnector
import uk.gov.hmrc.play.frontend.auth.connectors.domain.EpayeAccount
import uk.gov.hmrc.play.microservice.controller.BaseController

trait UserInfoController extends BaseController {

  def authConnector = MicroserviceAuthConnector

  implicit val epayeAccountWrites = Json.writes[EpayeAccount]

  def userAccounts = Action.async {
    implicit request => authConnector.currentAuthority.map {
      case Some(authority) => Ok(Json.toJson(authority.accounts.epaye))
      case None => NotFound
    }
  }
}

object UserInfoController extends UserInfoController