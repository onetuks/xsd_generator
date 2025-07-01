public class XsdGeneratorTest {



  @Test

  public void generateTest() {

    // given

    XsdGenerator xsdGenerator = new XsdGenerator();

    XTree xTree = TestFixture.createXTree();

    String expected = TestFixture.createXsdString();



    // when

    String result = xsdGenerator.generate(xTree);



    // then

    Assert.assertEquals(expected, result);

  }

}