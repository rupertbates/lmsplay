package helpers

import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.ObjectMapper
import com.fasterxml.jackson.module.scala

import org.apache.http.{HttpEntity, HttpResponse}
import java.io.InputStream
import models.Match
import org.codehaus.jackson.`type`.TypeReference
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import java.util.{ArrayList, UUID}
import org.apache.http.client.HttpClient
import play.api.db
import scala.{DefaultScalaModule, CaseClassModule, OptionModule}

object CouchHelper {
  def getGames () :  List[Match] = {
    val httpClient = new DefaultHttpClient()
    
    val httpGet = new HttpGet("http://localhost:5984/lms/_design/allMatches/_view/allMatches")

    val response = httpClient.execute(httpGet)

    if (response.getStatusLine.getStatusCode != 200)
      return null

    val entity: HttpEntity = response.getEntity
    val entityStream = entity.getContent

    val om = new ObjectMapper();
    //val module = new OptionModule with CaseClassModule
    om.registerModule(DefaultScalaModule)
    //node = om.readTree(entityStream)

    val data = om.readValue(entityStream, new TypeReference[ArrayList[Match]]() {})
    httpClient.getConnectionManager().shutdown();
    return data
  }
//  def getSomething(){
//    HttpClient httpClient = new StdHttpClient.Builder()
//      .url("http://localhost:5984")
//      .build();
//
//    CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
//    CouchDbConnector db = new StdCouchDbConnector("mydatabase", dbInstance);
//
//    db.createDatabaseIfNotExists();
//
//    Sofa sofa = db.get(Sofa.class, "ektorp");
//    sofa.setColor("blue");
//    db.update(sofa);
//  }

}
