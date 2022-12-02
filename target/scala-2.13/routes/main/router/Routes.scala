// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/april.garlington/Documents/AGA/Training/play-template/conf/routes
// @DATE:Fri Dec 02 10:43:23 GMT 2022

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:1
  ApplicationController_0: controllers.ApplicationController,
  // @LINE:8
  HomeController_2: controllers.HomeController,
  // @LINE:11
  Assets_1: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:1
    ApplicationController_0: controllers.ApplicationController,
    // @LINE:8
    HomeController_2: controllers.HomeController,
    // @LINE:11
    Assets_1: controllers.Assets
  ) = this(errorHandler, ApplicationController_0, HomeController_2, Assets_1, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, ApplicationController_0, HomeController_2, Assets_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api""", """controllers.ApplicationController.index"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """create""", """controllers.ApplicationController.create"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """read/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.read(id:String)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """update/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.update(id:String)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """delete/""" + "$" + """id<[^/]+>""", """controllers.ApplicationController.delete(id:String)"""),
    ("""GET""", this.prefix, """controllers.HomeController.index()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:1
  private[this] lazy val controllers_ApplicationController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api")))
  )
  private[this] lazy val controllers_ApplicationController_index0_invoker = createInvoker(
    ApplicationController_0.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "index",
      Nil,
      "GET",
      this.prefix + """api""",
      """""",
      Seq()
    )
  )

  // @LINE:2
  private[this] lazy val controllers_ApplicationController_create1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("create")))
  )
  private[this] lazy val controllers_ApplicationController_create1_invoker = createInvoker(
    ApplicationController_0.create,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "create",
      Nil,
      "POST",
      this.prefix + """create""",
      """""",
      Seq()
    )
  )

  // @LINE:3
  private[this] lazy val controllers_ApplicationController_read2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("read/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_read2_invoker = createInvoker(
    ApplicationController_0.read(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "read",
      Seq(classOf[String]),
      "GET",
      this.prefix + """read/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:4
  private[this] lazy val controllers_ApplicationController_update3_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("update/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_update3_invoker = createInvoker(
    ApplicationController_0.update(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "update",
      Seq(classOf[String]),
      "PUT",
      this.prefix + """update/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:5
  private[this] lazy val controllers_ApplicationController_delete4_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("delete/"), DynamicPart("id", """[^/]+""",true)))
  )
  private[this] lazy val controllers_ApplicationController_delete4_invoker = createInvoker(
    ApplicationController_0.delete(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApplicationController",
      "delete",
      Seq(classOf[String]),
      "DELETE",
      this.prefix + """delete/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_HomeController_index5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index5_invoker = createInvoker(
    HomeController_2.index(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_Assets_versioned6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned6_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:1
    case controllers_ApplicationController_index0_route(params@_) =>
      call { 
        controllers_ApplicationController_index0_invoker.call(ApplicationController_0.index)
      }
  
    // @LINE:2
    case controllers_ApplicationController_create1_route(params@_) =>
      call { 
        controllers_ApplicationController_create1_invoker.call(ApplicationController_0.create)
      }
  
    // @LINE:3
    case controllers_ApplicationController_read2_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_read2_invoker.call(ApplicationController_0.read(id))
      }
  
    // @LINE:4
    case controllers_ApplicationController_update3_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_update3_invoker.call(ApplicationController_0.update(id))
      }
  
    // @LINE:5
    case controllers_ApplicationController_delete4_route(params@_) =>
      call(params.fromPath[String]("id", None)) { (id) =>
        controllers_ApplicationController_delete4_invoker.call(ApplicationController_0.delete(id))
      }
  
    // @LINE:8
    case controllers_HomeController_index5_route(params@_) =>
      call { 
        controllers_HomeController_index5_invoker.call(HomeController_2.index())
      }
  
    // @LINE:11
    case controllers_Assets_versioned6_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned6_invoker.call(Assets_1.versioned(path, file))
      }
  }
}
