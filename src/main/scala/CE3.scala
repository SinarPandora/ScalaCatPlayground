import cats.*
import cats.data.*
import cats.syntax.all.*
import cats.effect.cps.*
import cats.effect.{IO, IOApp}
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.*
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*
import scala.util.chaining.*

object CE3 extends IOApp.Simple:
  protected type Backend = SttpBackend[IO, Fs2Streams[IO] with capabilities.WebSockets]

  case class TODO(userId: Long, id: Long, title: String, completed: Boolean)

  def fetchJson()(using backend: Backend): IO[String] =
    basicRequest.get(uri"https://jsonplaceholder.typicode.com/todos/1")
      .send(backend)
      .map(_.body)
      .pipe(EitherT.apply)
      .valueOrF(_ => IO.raiseError(RuntimeException("Can not get item from placeholder site")))

  def extractTODO(jsonText: String): Either[RuntimeException, TODO] =
    decode[TODO](jsonText).leftMap(e => RuntimeException("Fail to parse JSON", e))

  override def run: IO[Unit] =
    HttpClientFs2Backend.resource[IO]().use: backend =>
      given Backend = backend
      async[IO]:
        val jsonText = fetchJson().await
        extractTODO(jsonText) match
          case Left(err)    => IO.raiseError(err).await
          case Right(value) => IO.println(value).await
