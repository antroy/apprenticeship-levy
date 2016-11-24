package uk.gov.hmrc.apprenticeshiplevy.endpoints

import org.scalatest._
import org.scalatest.Matchers._
import org.scalatest.concurrent.{Eventually, IntegrationPatience}

import scalaj.http.Http
import play.api.libs.json.Json

@DoNotDiscover
class RootSpec extends FunctionalSpec with Eventually with IntegrationPatience {
  describe("Root Endpoint") {
    contexts.foreach { case (pair) =>
      val context = pair._1
      val folder = s"$dir/${pair._2}"
      it (s"should when calling ${url}$context/ return links for each empref") {
        // set up
        val expected = fileToStr(s"${folder}/root.json")
        val expectedJson = Json.parse(expected)

        // test
        val result = eventually {
          Http(s"$url$context/")
            .headers(standardHeaders)
            .asString
        }

        // check
        result.code shouldBe 200
        result.contentType shouldBe Some("application/hal+json")
        Json.parse(result.body) shouldBe expectedJson
      }
    }
  }
}