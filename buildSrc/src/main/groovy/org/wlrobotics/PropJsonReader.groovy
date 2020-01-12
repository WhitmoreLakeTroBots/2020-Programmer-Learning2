#!/usr/bin/groovy
import java.nio.file.Paths
import groovy.json.JsonSlurper

class PropJsonReader {
//    This class is designed to read Json property files use content as groovy objects

    def defaultLocations (String propFileName) {
        // This method will retruns default locations of file
        String [] propfileLocations = [
        Paths.get(propFileName),
        Paths.get(System.getProperty("HOME") + "/${propFileName}"),
        Paths.get(System.getProperty("user.home") + "/Gina//${propFileName}"),
        Paths.get(System.getProperty("user.home") + "/gina//${propFileName}"),
        Paths.get(System.getProperty("USERPROFILE") + "/${propFileName}"),
        Paths.get(System.getProperty("USERPROFILE") + "/Gina/${propFileName}"),
        Paths.get(System.getProperty("USERPROFILE") + "/gina/${propFileName}")
        ]
        return propfileLocations
    }

    def findFileLocation(String fileName) {
        // this method will searches file in default location return file path
        String [] locations = defaultLocations(fileName)
        for (fileLoc in locations) {
            def fileObj = new File(fileLoc)
            if (fileObj.exists()) {
                return fileObj
            }
        }
        println(fileName+"File not fould")
    return ("")
    }
  def props (String [] locations){
    def p = null
    for (fileName in locations) {
      try {
        // println "looking for ${fileName}"
        def mFile = new File(fileName)
        if (mFile.exists()) {
          println ("FOUND: Propery file ${fileName}")
          p = new JsonSlurper().parse(mFile)
          break
            }
      }
      catch (FileNotFoundException e) {
        println "FileNotFound: ${fileName}"
      }
    }
    String locString = "\n"
    if ( p == null ) {
      locations.each {  fileName ->
        locString = locString + "\t FileNotFound: ${fileName}\n"
      }
      throw new FileNotFoundException ("ERROR: Property file not found in any of the listed locations. " + locString)
    }
    return p
  }
}
