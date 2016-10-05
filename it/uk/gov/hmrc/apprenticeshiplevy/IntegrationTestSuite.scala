package uk.gov.hmrc.apprenticeshiplevy

import org.scalatest._
import uk.gov.hmrc.apprenticeshiplevy.util._
import uk.gov.hmrc.play.test.UnitSpec

class IntegrationTestsSuite extends Suites(new uk.gov.hmrc.apprenticeshiplevy.sandbox.Suite,
                                           new DocumentationControllerISpec,
                                           new ServiceLocatorRegistrationISpec)
  with BeforeAndAfterAllConfigMap with IntegrationTestConfig {

  override def beforeAll(cm: ConfigMap) {
    WiremockService.start()
    PlayService.start()
  }

  override def afterAll(cm: ConfigMap) {
    PlayService.stop()
    WiremockService.stop()
  }
}

class DESErrorConditionsIntegrationTestsSuite extends Suites(new PublicDocumentationControllerISpec)
  with BeforeAndAfterAllConfigMap with IntegrationTestConfig {
  val playService = new PlayService() {
    override def stubConfigPath = "./it/no-mappings"
    override def additionalConfiguration: Map[String, Any] = (super.additionalConfiguration - "microservice.private-mode") ++ Map(
      "microservice.private-mode" -> "false",
      "microservice.whitelisted-applications" -> "none")
  }

  override def beforeAll(cm: ConfigMap) {
    WiremockService.start()
    playService.start()
  }

  override def afterAll(cm: ConfigMap) {
    playService.stop()
    WiremockService.stop()
  }
}