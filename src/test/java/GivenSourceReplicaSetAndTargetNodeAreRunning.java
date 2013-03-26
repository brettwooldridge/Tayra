import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map.Entry;

import com.ee.tayra.Environment;
import com.ee.tayra.fixtures.AssertMongoFixture;
import com.ee.tayra.fixtures.MongoSourceAndTargetConnector;
import com.ee.tayra.fixtures.RunnerFixture;
import com.ee.tayra.NamedParameters;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import fit.Fixture;
import fit.Parse;
import fit.exception.FitFailureException;
import fit.exception.MissingCellsFailureException;
import fitlibrary.DoFixture;

public class GivenSourceReplicaSetAndTargetNodeAreRunning extends DoFixture {


  private static final int SLEEP_TIME = 800;
  private MongoSourceAndTargetConnector connector;
  private NamedParameters parameters;

  public GivenSourceReplicaSetAndTargetNodeAreRunning()
      throws UnknownHostException {
    parameters = Environment.settings();
    parameters.add("{file}", "test.out");
    connector = new MongoSourceAndTargetConnector(parameters);
  }



  public final Fixture openTerminal() {
    return new RunnerFixture(parameters);
  }

  public final void sleep(final int duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public final Fixture runMongoCommandOn(final String nodeName)
      throws UnknownHostException {
    Node node = Node.valueOf(nodeName.toUpperCase(), connector);
    return node.getMongoFixture();
  }

  public final Fixture ensuringTargetIsConsistentWithSource() {
    sleep(SLEEP_TIME);
    return new AssertMongoFixture(connector);
  }

  public final boolean openOplogForNodeAndTravelDocumentsBackInTime(
      final String nodeName, final int howMany) {
    Node node = Node.valueOf(nodeName.toUpperCase(), connector);
    Mongo mongo = node.getMongo();
    DBCursor cursor = null;
    try {
      DBCollection collection = mongo.getDB("local").getCollection(
          "oplog.rs");
      cursor = collection.find().skip((int) collection.count() - howMany);
      parameters.add("{Until}", JSON.serialize(cursor.next().get("ts"))
              .replaceAll("[\" ]", ""));
    } catch (MongoException problem) {
      throw new FitFailureException(problem.getMessage());
    } finally {
      System.out.println("closing cursor");
      cursor.close();
    }
    return true;
  }

  public final Fixture ensureSourceAndTargetHas() {
    return ensuringTargetIsConsistentWithSource();
  }

  public final void withConfiguration(final Parse cells) throws IOException {
    Parse args = cells.more;
    if (args == null) {
      throw new MissingCellsFailureException(cells.text()
          + " requires an argument");
    }
    String cmdString = args.text();
    cmdString = injectValuesIn(cmdString, parameters);
    args.addToBody("<hr/>" + label("Substituted Values Output") + "<hr/>");
    args.addToBody("<pre/>" + cmdString + "</pre>");
  }

  private String injectValuesIn(final String cmdString,
      final NamedParameters namedParams) {
    String result = new String(cmdString);
    for (Entry<String, String> nameValue : namedParams.entrySet()) {
      String key = nameValue.getKey();
      String value = nameValue.getValue();
      result = result.replace(key, value);
    }
    return result;
  }

  @Override
  protected final void tearDown() throws Exception {
    connector.close();
    super.tearDown();
  }
}