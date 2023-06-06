import sttp.capabilities
import sttp.capabilities.zio.ZioStreams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.*
import sttp.client3.*
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import zio.direct.*

object Z2 extends ZIOAppDefault:
  protected type Backend = SttpBackend[Task, ZioStreams with capabilities.WebSockets]

  case class TODO(userId: Long, id: Long, title: String, completed: Boolean)

  def fetchJson()(using backend: Backend): Task[String] =
    basicRequest.get(uri"https://jsonplaceholder.typicode.com/todos/1")
      .send(backend)
      .map(_.body.left.map(RuntimeException(_)))
      .absolve

  def extractTODO(jsonText: String): Either[RuntimeException, TODO] =
    decode[TODO](jsonText).left.map(e => RuntimeException("Fail to parse JSON", e))

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    HttpClientZioBackend().flatMap: backend =>
      given Backend = backend
      defer:
        val jsonText = fetchJson().run
        extractTODO(jsonText) match
          case Left(err)    => ZIO.fail(err).run
          case Right(value) => Console.printLine(value).run
