#!/bin/groovy
class DriveMapper {
  def reader = null
  def props = null

  String driveLetter = null
  String remoteServer = null
  String userName = null
  String passWord = null
  String persistence = null

////////////////////////////////////////////////////////////////////////////////

  DriveMapper (String configName) {
    // mapped based on a configuration name in the default prop file name
    reader = new PropJsonReader()
    props = reader.props(reader.defaultLocations("gina.json"))

    driveLetter = props.mappedDrives."${configName}".driveletter.toUpperCase()
    remoteServer = props.mappedDrives."${configName}".remoteserver
    userName = props.mappedDrives."${configName}".username
    passWord = props.mappedDrives."${configName}".password
    persistence = props.mappedDrives."${configName}".persistence

  }

  DriveMapper (String propFileName, String configName) {
    // mapped based on a configuration name in the named prop file
    reader = new PropJsonReader()
    props = reader.props(reader.defaultLocations(propFileName))
    driveLetter = props.mappedDrives."${configName}".driveletter.toUpperCase()
    remoteServer = props.mappedDrives."${configName}".remoteserver
    userName = props.mappedDrives."${configName}".username
    passWord = props.mappedDrives."${configName}".password
    persistence = props.mappedDrives."${configName}".persistence

  }


  DriveMapper (String letter, String server, String user, String pass, String persist) {
    // mapped based on all parameters
    driveLetter = letter.toUpperCase()
    remoteServer = server
    userName = user
    passWord = pass
    persistence = persist

  }

////////////////////////////////////////////////////////////////////////////////

String escapeSlash (String str){
  // converts the server name slashes to slashes that can be used with regEx
  return str.replaceAll ('\\\\', '\\\\\\\\')
}

////////////////////////////////////////////////////////////////////////////////

  Boolean isConnected () {
    // Look for this regex from net use
    // 'P:        \\\\ebgroup.elektrobit.com\\EBProjects\\FS01'
    def retValue = false
    def retText = [:]
    def r = new Run2()
    retText = r.command ("net use")
    def regEx = driveLetter + $/:\s*/$ + escapeSlash(remoteServer.toUpperCase())
    if (retText[0].toUpperCase() =~ regEx){
      retValue = true
    }
    return retValue
  }
////////////////////////////////////////////////////////////////////////////////
  def doDisconnect() {

    def retText = [:]
    def r = new Run2()
    String cmd = ""
    try {
      cmd = "net use ${driveLetter}: /DELETE"
      retText = r.command (cmd)
    }
    catch (java.lang.Exception e) {
      println ("INFO: ${driveLetter}: was not mapped")
    }
  }
////////////////////////////////////////////////////////////////////////////////
  def doConnect() {
    //only connect if we are not already connected to the correct drive and server
    def retText = [:]
    def r = new Run2()
    String cmd = ""

    if (! isConnected() ) {
      doDisconnect()
      retText = [:]
      println "INFO: Mapping ${driveLetter}: to \"${remoteServer}\" as /user:\"${userName}\""
      cmd = "net use ${driveLetter}: \"${remoteServer}\" /user:${userName} ${passWord} /persistent:${persistence}"
      try {
        retText = r.command (cmd)
      }
      catch (java.lang.Exception e) {
        //protect the password
        throw new Exception(e.toString().replaceAll ("${passWord}","*******"))
      }
    }
  }
}
