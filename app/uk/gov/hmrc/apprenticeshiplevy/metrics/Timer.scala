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

package uk.gov.hmrc.apprenticeshiplevy.metrics

import java.util.concurrent.TimeUnit
import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Success, Failure, Try}

trait Timer {
  metrics: GraphiteMetrics =>

  def timer[T](event: RequestEvent)(block: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val startTime = System.currentTimeMillis()
    block andThen {
      case Success(v) => {
        metrics.successfulRequest(event)
        metrics.processRequest(TimerEvent(event.name, System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS))
      }
      case Failure(t) => {
        metrics.failedRequest(event)
        metrics.processRequest(TimerEvent(event.name, System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS))
      }
    }
  }
}
