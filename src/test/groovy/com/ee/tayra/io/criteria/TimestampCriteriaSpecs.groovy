package com.ee.tayra.io.criteria

import java.text.SimpleDateFormat

import com.ee.tayra.io.criteria.TimestampCriteria;

import spock.lang.Specification

class TimestampCriteriaSpecs extends Specification {

	def returnsTrueIfDocumentIsEarlierThanTimestamp () {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '{ts:{$ts:1357537752,$inc:2}}'
			def document = '{ts:{$ts:1357537752,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, false)
								
		then: 'it returns true if the document is older than the timestamp'
			criteria.isSatisfiedBy(document)
		
	}

	def returnsFalseIfDocumentIsLaterThanTimestamp () {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '{ts:{$ts:1300000000,$inc:1}}'
			def document = '{ts:{$ts:1357537752,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, false)
								
		then: 'it returns true if the document is older than the timestamp'
			! criteria.isSatisfiedBy(document)
		
	}

	def returnsTrueIfDocumentIsEarlierThanISOTime() {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '2012-12-26T15:19:40Z'
			def document = '{ts:{$ts:1356515303,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, false)
								
		then: 'it returns true if the document is older than the timestamp'
			criteria.isSatisfiedBy(document)
		
	}

	def returnsFalseIfDocumentIsLaterThanISOTime() {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '2012-12-26T15:19:40Z'
			def document = '{ts:{$ts:1357801207,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, false)
								
		then: 'it returns true if the document is older than the timestamp'
			!criteria.isSatisfiedBy(document)
	}

	def returnsFalseIfDocumentIsEarlierThanTimestampWithSExcludeOption () {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '{ts:{$ts:1357537752,$inc:2}}'
			def document = '{ts:{$ts:1357537752,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
			
		when: 'timestamp criteria is applied to the document with sExclude as true'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, true)
	
		then: 'document is excluded if it is older than the timestamp'
			!criteria.isSatisfiedBy(document)
		
	}

	def returnsTrueIfDocumentIsLaterThanTimestampWithSExcludeOption() {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '{ts:{$ts:1300000000,$inc:1}}'
			def document = '{ts:{$ts:1357537752,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document with sExclude as true'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, true)
								
		then: 'document is not excluded if it is later than the timestamp'
			criteria.isSatisfiedBy(document)
		
	}
	
	def returnsFalseIfDocumentIsEarlierThanISOTimeWithSExcludeOption() {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '2012-12-26T15:19:40Z'
			def document = '{ts:{$ts:1356515303,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'
		
		when: 'timestamp criteria is applied to the document with sExclude as true'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, true)
								
		then: 'document is excluded if it is older than the timestamp'
			!criteria.isSatisfiedBy(document)
		
	}

	def returnsTrueIfDocumentIsLaterThanISOTimeWithSExcludeOption() {
		
		given: 'A timestamp and an oplog document'
			def timeStamp = '2012-12-26T15:19:40Z'
			def document = '{ts:{$ts:1357801207,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'

		when: 'timestamp criteria is applied to the document with sExclude as true'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, true)

		then: 'document is not excluded if it is later than the timestamp'
			criteria.isSatisfiedBy(document)
	}

	def shoutsWhenInvalidJSONTimestampFormatIsGiven() {
		given:'an invalid JSON timestamp'
			def invalidTimeStamp = '{ts:{$s:1357801207,$inc:1}}'

		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(invalidTimeStamp, true)

		then: 'it shouts'
			thrown Exception
	}

	def shoutsWhenInvalidISOTimestampFormatIsGiven() {
		given:'an invalid ISO timestamp'
			def invalidTimeStamp = '2012-12-2615:19:40Z'

		when: 'timestamp criteria is applied to the document'
			TimestampCriteria criteria = new TimestampCriteria(invalidTimeStamp, true)

		then: 'it shouts'
			thrown Exception
	}

	def shoutsWhenDocumentHasInvalidTimestampFormat() {
		given:'A timestamp'
			def timeStamp = '{ts:{$ts:1357801207,$inc:1}}'
			
		and: 'an oplog document with an invalid timestamp'
			def document = '{ts:{$s:1357801207,$inc:1} , "h" : -2719432537158937612 , "v" : 2 , "op" : "i" , "ns" : "test.things" , "o" : { "_id" : { "$oid" : "50ea61d85bdcefd43e2994ae"} , "roll" : 0.0}}'

		and: 'a timestamp criteria'
			TimestampCriteria criteria = new TimestampCriteria(timeStamp, true)

		when: 'timestamp criteria is applied to the document'
			criteria.isSatisfiedBy(document)

		then: 'it shouts'
			thrown Exception
	}
}
