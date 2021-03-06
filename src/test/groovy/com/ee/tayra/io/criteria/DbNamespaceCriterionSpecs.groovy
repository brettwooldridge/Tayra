package com.ee.tayra.io.criteria

import spock.lang.Specification

class DbNamespaceCriterionSpecs extends Specification {

  static eelabsCreateCollectionEntry = ''' "op" : "c", "ns" : "eelabs.$cmd",
        "o" : { "create" : "countries", "capped" : null, "size" : null } }'''
  static eelabsCreateCollectionWithInsert= '''"ns" : "eelabs.countries", "o" :
        { "_id" : ObjectId("511499dd9365898be4b00b0d"), "name" : "Test1" } }'''
  static eelabsUpdateDoc = ''' "op" : "u", "ns" : "eelabs.countries",
        "o2" :{ "_id" : ObjectId("511499dd9365898be4b00b0d") },
        "o" : { "$set" : { "name" : "Test2" } } }'''
  static eelabsInsertDoc = '''
          "op" : "i" , "ns" : "eelabs.countries" , "o" :
        { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'''
  static eelabsDeleteDoc = ''' "op" : "d" , "ns" : "eelabs.countries",
        "b" : true, "o" : { "_id" : ObjectId("51149b949365898be4b00b0e") } }'''
  static eelabsDropDatabase = ''' "op" : "c" , "ns" : "eelabs.$cmd",
        "o" : { "dropDatabase" : 1 } }'''
  static eelabsInsertDocFail = ''' "op" : "i" , "ns" : "eelabs.friends" , "o" :
        { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'''
  static eeDropIndexFail = ''' "op" : "c" , "ns" : "ee.$cmd",
        "o" : { "deleteIndexes" : "countries", "index" : { "roll" : 1 } } }'''
  static eelabsCreateCollection = ''' "op" : "c" , "ns" : "eelabs.$cmd" ,
        "o" : { "create" : "countries"}}'''
  static eelabsDropCollection = ''' "op" : "c" , "ns" : "eelabs.$cmd" ,
        "o" : { "drop" : "countries"}}'''
  static eelabsCreateIndex = ''' "op" : "i" , "ns" : "eelabs.system.indexes" ,
        "o" : { "_id" : { "$oid" : "511b232461ad583bb301e9ec"} , "ns" : "eelabs.countries" , "key" : { "name" : 1.0} , "name" : "name_1"}}'''
  static eelabsDropIndex = ''' "op" : "c" , "ns" : "eelabs.$cmd" ,
        "o" : { "deleteIndexes" : "countries" , "index" : { "name" : 1.0}}}'''
  static eelabsCappedCreateCollection=''' "op" : "c" , "ns" : "eelabs.$cmd" ,
        "o" : { "create" : "countries" , "capped" : true , "size" : 10000.0}}'''
  static initiatingSet = ''' "op" : "n" , "ns" : "",
        "o" : { "msg" : "initiating set" } }'''

  static tayraInsertOne = ''' "op" : "i" , "ns" : "tayra.people" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdb"} , "name" : 29.0}}'''
  static tayraInsertTwo = ''' "op" : "i" , "ns" : "tayra.people" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdc"} , "name" : 30.0}}'''
  static tayraInsertThree = ''' "op" : "i" , "ns" : "tayra.project" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdd"} , "name" : 31.0}}'''

  static eePrefixedInsertOne = ''' "op" : "i" , "ns" : "ee.people.addresses" ,
        "o" : { "_id" : { "$oid" : "512cab35696006eb3408bfdb"} , "name" : 29.0}}'''
  static eePrefixedInsertTwo = ''' "op" : "i" , "ns" : "ee.people.addresses" ,
        "o" : { "_id" : { "$oid" : "512cab35696006eb3408bfdc"} , "name" : 30.0}}'''
  static eeInsertThree = ''' "op" : "i" , "ns" : "ee.thing" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdd"} , "name" : 31.0}}'''

  static DLInsertOne = ''' "op" : "i" , "ns" : "DL.people" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdb"} , "name" : 29.0}}'''
  static DLInsertTwo = ''' "op" : "i" , "ns" : "DL.people" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdc"} , "name" : 30.0}}'''
  static DLInsertThree = ''' "op" : "i" , "ns" : "DL.people" , "o" :
        { "_id" : { "$oid" : "512cab35696006eb3408bfdd"} , "name" : 31.0}}'''

  def namespace ='eelabs'
  def multipleDBnamespace ='ee,tayra,DL'

  def criteria
  def document

  def satisfiesDatabaseCriteria(){
      criteria = new NamespaceCriterion (namespace)

    expect: 'criteria is satisfied for documents belonging to eelabs db'
      outcome == criteria.isSatisfiedBy(document)

    where:
      document                       | outcome
    eelabsCreateCollectionEntry      | true
    eelabsCreateCollectionWithInsert | true
    eelabsInsertDoc                  | true
    eelabsUpdateDoc                  | true
    eelabsDeleteDoc                  | true
    eelabsDropCollection             | true
    eelabsDropDatabase               | true
    eelabsCreateIndex                | true
    eelabsDropIndex                  | true
    eelabsInsertDocFail              | true
  }

  def doesNotSatisfyDatabaseCriteria() {
    criteria = new NamespaceCriterion(namespace)

    expect: 'criteria is not satisfied for documents not belonging to eelabs db'
    outcome == criteria.isSatisfiedBy(document)

    where:
      document                       | outcome
    eeDropIndexFail                  | false
    initiatingSet                    | false
  }

  def satisfiesMultipleDatabaseCriteria(){
    criteria = new NamespaceCriterion (multipleDBnamespace)

    expect: '''criteria is satisfied for documents belonging to ee, tayra, DL
           dbs and not eelabs'''
      outcome == criteria.isSatisfiedBy(document)

    where:
      document                       | outcome
    eelabsCreateCollectionEntry      | false
    eelabsCreateCollectionWithInsert | false
    eelabsInsertDoc                  | false
    eelabsUpdateDoc                  | false
    eelabsDeleteDoc                  | false
    eelabsDropCollection             | false
    eelabsDropDatabase               | false
    eelabsCreateIndex                | false
    eelabsDropIndex                  | false
    eelabsInsertDocFail              | false
    initiatingSet                    | false
    eeDropIndexFail                  | true
    tayraInsertOne                   | true
    tayraInsertTwo                   | true
    tayraInsertThree                 | true
    DLInsertOne                      | true
    DLInsertTwo                      | true
    DLInsertThree                    | true
    eePrefixedInsertOne              | true
    eePrefixedInsertTwo              | true
    eeInsertThree                    | true
  }
}
