package tv.codely.scala_http_api.module.shared.marshaller.infrastructure

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat, SerializationException, _}
import tv.codely.scala_http_api.module.course.domain.CourseCreated
import tv.codely.scala_http_api.module.shared.bus.domain.Message
import tv.codely.scala_http_api.module.user.domain.UserRegistered
import tv.codely.scala_http_api.module.user.infrastructure.marshaller.UserRegisteredJsonFormatMarshaller._
import tv.codely.scala_http_api.module.video.domain.VideoCreated
import tv.codely.scala_http_api.module.video.infrastructure.marshaller.VideoCreatedJsonFormatMarshaller._
import tv.codely.scala_http_api.module.course.infrastructure.marshaller.CourseCreatedJsonFormatMarshaller._

object MessageJsonFormatMarshaller extends DefaultJsonProtocol {
  implicit object MessageMarshaller extends RootJsonFormat[Message] {
    override def write(m: Message): JsValue = m match {
      case vc: VideoCreated   => vc.toJson
      case cc: CourseCreated  => cc.toJson
      case ur: UserRegistered => ur.toJson
      case unknown            => throw new SerializationException(s"Unknown message type to write <${unknown.getClass}>")
    }

    override def read(jv: JsValue): Message = jv.asJsObject.getFields("type") match {
      case Seq(JsString("codelytv_scala_api.video_created"))   => jv.convertTo[VideoCreated]
      case Seq(JsString("codelytv_scala_api.course_created"))  => jv.convertTo[CourseCreated]
      case Seq(JsString("codelytv_scala_api.user_registered")) => jv.convertTo[UserRegistered]
      case Seq(JsString(unknown)) =>
        throw DeserializationException(s"Unknown message type to read <$unknown>")
    }
  }
}
