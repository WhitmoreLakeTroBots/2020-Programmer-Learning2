import java.nio.file.Paths
import java.util.regex.*
import groovy.json.JsonSlurper


class Prop2Cli {

  String convert (obj){
    String retValue = ""
    obj.each{ k, v ->
      try{
        retValue = retValue + "-${k}=\"" + subEnvironVars(v) + "\" "
      }
      catch (Exception e) {
        def msg = "Property ${k} has invalid syntax for environment variable subsitution."
        println msg
        throw e
      }
    }
    retValue = retValue.replaceAll(/\"true\"/, "true")
    retValue = retValue.replaceAll(/\"false\"/, "false")
    return retValue
  }

  String subEnvironVars (String inStr){
    // looking for things like this that we can read from
    // environment vars
    // ${System.getenv('USERNAME')}
    String retValue = inStr
    def regexSystem = /\$\{System.getenv *\( *["'][a-z,A-Z,0-9()]*["'] *\) *\}/
    def finderSystem = (inStr =~ regexSystem)
    def regexVariable =/["'].*["']/
    finderSystem.each {fs ->
      def finderVariable = (fs =~ regexVariable)
      if (finderVariable.count > 0){
        def var = System.getenv(finderVariable[0][1..-2])
        retValue = retValue.replace(fs, var)
      }
      else{
        def msg = "${insStr} has invalid System.getenv syntax for subsitution."
        throw new Exception(msg)
      }
    }

    return retValue
  }

  static void main(String[] args) {
    def reader = new PropJsonReader()
    def props = reader.props("D:\\work\\ProjectAutomation\\TAZ\\src\\taz.json")
    def p2c = new Prop2Cli()
    println ()
    println p2c.convert(props.sctm)
  }
}
