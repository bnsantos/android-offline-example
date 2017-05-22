'use strict'
require('dotenv').config({path: './test/.env'})
let db = null
const assert = require('assert');
const sinon = require('sinon')

describe('DB Test', function() {
  before('start db', function(done){
    //Delete wipe DB data
    db = require('../db')(function(){
      db.models.Comment.destroy({cascade : true, truncate: true}).then(function(){
        return db.models.User.destroy({cascade : true, truncate: true})
      }).then(function(){
        done()
      }).catch(err => {
        assert.equal(err, null)
      })
    })
  })

  describe('insert users', function(){
    it('insert user 1', function(done){
      db.models.User.create({
        id: 'user-test-1',
        name: 'John Doe',
        email: 'johndoe@email.com'
      }).then(user => {
        done()
      }).catch(err => {
        assert.equal(err, null)
      })
    })
  })

  describe('insert comment', function(){
    it('insert comment 1', function(done){
      db.models.Comment.create({
        id: 'comment-test-1',
        text: 'Comment text 1',
        userId: 'user-test-1'
      }).then(comment => {
        done()
      }).catch(err => {
        assert.equal(err, null)
      })
    })
  })
})
