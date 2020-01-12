#!/usr/bin/groovy
//
// based on the unix command line tr that "translates characters this class
// was created to translate unicode chars into the basic non unicode equivilants
//
// create the object and do a translate
// Def tr = new TR()
// tr.translate ("mytextFile.xml", "utf8")

class TR{
  Map nonUTF8Tokens = [
    '“' : '"',
    '”' : '"',
    '–' : '-',
    '’' : "\'" ]

  Map tokens = null;
  AntBuilder ant = new AntBuilder()
  // Without input it will default to swapping out known unicode chars that
  // cause issues.   By default only replace the unicode issue chars.

  TR(){
    this.tokens = this.nonUTF8Tokens
  }

  // Give option of naming replacements at time of object creation.
    TR(Map newTokens){
    this.tokens = newTokens
  }

  void translate (String fName, String encoding="utf8"){
    this.tokens.each () { k, v ->
      this.ant.replace(file: fName, token:k, value:v, encoding:encoding )
    }
  }

  // give options of naming the replacements at time of translate.
  void translate (String fName, String encoding="utf8", Map tokens){
    tokens.each () { k, v ->
      this.ant.replace(file: fName, token:k, value:v, encoding:encoding )
    }
  }

  static void main(String[] args) {
      println "This script is intended to be used by other groovy scripts."
    }
}
