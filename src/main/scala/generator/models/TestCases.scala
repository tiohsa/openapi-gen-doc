package generator.models

case class TestCases(
    getMethod: Option[TestCase],
    postMethod: Option[TestCase],
    putMethod: Option[TestCase],
    deleteMethod: Option[TestCase]
) {
  override def toString: String = {
    List(
      this.getMethod,
      this.postMethod,
      this.putMethod,
      this.deleteMethod
    ).filter(_.isDefined).map(_.get.toString).mkString("\n")
  }
}
