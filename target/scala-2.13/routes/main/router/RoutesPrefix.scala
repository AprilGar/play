// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/april.garlington/Documents/AGA/Training/play-template/conf/routes
// @DATE:Fri Dec 02 10:43:23 GMT 2022


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
