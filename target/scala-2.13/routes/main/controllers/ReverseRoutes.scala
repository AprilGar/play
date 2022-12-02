// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/april.garlington/Documents/AGA/Training/play-template/conf/routes
// @DATE:Fri Dec 02 10:43:23 GMT 2022

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:1
package controllers {

  // @LINE:1
  class ReverseApplicationController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:3
    def read(id:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "read/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:2
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "create")
    }
  
    // @LINE:5
    def delete(id:String): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "delete/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:4
    def update(id:String): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "update/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:1
    def index(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api")
    }
  
  }

  // @LINE:8
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:8
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:11
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
