#!/usr/bin/groovy

class Run{
  //
  //Run returns an exception or error code Zero
  //
  def command (cmd_line){
    //runs generic python script
    def out = new StringBuffer()
    def err = new StringBuffer()
    println ("\t" + cmd_line + "\n")
    Process p = cmd_line.execute()
    p.consumeProcessOutput( out, err )
    p.waitFor()

    if( out.size() > 0 ) {print ("\t" + out + "\n")}
    if( err.size() > 0 ) {print ("\t" + err + "\n")}

    if (p.exitValue() != 0 ){
        def msg = "command " + cmd_line + "\n" +
                  "returned an error code " + p.exitValue() + "\n"
        throw new Exception(msg)
    }
  }
}
//
//Run2 returns the text so that it can be searched for results
//
class Run2{

  def command (cmd_line){
    //runs generic python script
    def out = new StringBuffer()
    def err = new StringBuffer()

    Process p = cmd_line.execute()
    p.consumeProcessOutput( out, err )
    p.waitFor()

    if (p.exitValue() != 0 ){
        def msg = "command " + cmd_line + "\n" +
                  "returned an error code " + p.exitValue() + "\n"
        throw new Exception(msg)
    }
    return [out.toString(), err.toString()]
  }
}
