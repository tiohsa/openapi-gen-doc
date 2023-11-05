package generator.models

case class TestCases(
    getMethod: List[TestCase],
    postMethod: List[TestCase],
    putMethod: List[TestCase],
    deleteMethod: List[TestCase]
)
