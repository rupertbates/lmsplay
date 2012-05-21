import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.codehaus.jackson.map.ObjectMapper

object TestJson {
  def main(args: Array[String]) {
    deserialise()
  }
  def serialise() {
    var t = new TestClass("hello")
    //val module = new OptionModule with CaseClassModule
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    //om.registerModule(DefaultScalaModule)
    val s = mapper.writeValueAsString(t)
    println(s)
  }
  def deserialise() {
    val json = "{\"user\":{\"name\":\"gibbon\",\"address\":{\"number\":5,\"postcode\":\"n13lf\"}}}";
    val mapper = new ObjectMapper();
    val node = mapper.readTree(json);
    val test = node.findValue("name")
    println(test)

  }
}