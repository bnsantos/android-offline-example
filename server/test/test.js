'use strict'
require('dotenv').config({path: './test/.env'})
let db = null
const assert = require('assert')
const fs = require('fs')
const expect = require('chai').expect
const request = require('supertest')

describe('Test', function() {
  before('start db', function(done){
    if(fs.existsSync(process.env.DB)){
      //Delete wipe DB data
      fs.unlinkSync(process.env.DB)
      console.log('Successfully deleted DB: ' + process.env.DB)
    }
    db = require('../db')(function(){
      console.log('Successfully created DB')
      done()
    })
  })

  describe('DB-User', userDB)

  describe('DB-Comment', commentDB)

  describe('Create comment offline', createCommentOffline)
})

function userDB(){
  var user1, user2, user3
  it('insert user 1', function(done){
    db.models.User.create({
          id: 'user-test-1',
          name: 'John Doe',
          email: 'johndoe@email.com'
        }).then(user => {
          expect(user).to.not.be.null
          user1 = user
          done()
        }).catch(err => {
          expect(err).to.be.null
        })
  })

  it('insert user 2', function(done){
    db.models.User.create({
          id: 'user-test-2',
          name: 'Mary Doe',
          email: 'marydoe@email.com'
        }).then(user => {
          expect(user).to.not.be.null
          user2 = user
          done()
        }).catch(err => {
          expect(err).to.be.null
        })
  })

  it('insert user 3', function(done){
    db.models.User.create({
          id: 'user-test-3',
          name: 'Dilma Doe',
          email: 'dilmadoe@email.com'
        }).then(user => {
          expect(user).to.not.be.null
          user3 = user
          done()
        }).catch(err => {
          expect(err).to.be.null
        })
  })

  // it('search user by name', function(done){
  //   db.models.User.findAll({ where : {name: 'Mary Doe'}})
  //     .then(user => {
  //       expect(user).to.not.be.null
  //       expect(user.length).to.equal(1)
  //       expect(user[0].name).to.equal('Mary Doe')
  //       done()
  //     }).catch(err => {
  //       expect(err).to.be.null
  //     })
  // })
  //
  // it('search user by name does not exists', function(done){
  //   db.models.User.findAll({ where : {name: 'Dilma'}})
  //     .then(user => {
  //       expect(user).to.not.be.null
  //       expect(user.length).to.equal(0)
  //       done()
  //     }).catch(err => {
  //       expect(err).to.be.null
  //     })
  // })
  //
  // it('update user', function(done){
  //   user2.name = 'New Mary Doe'
  //   user2.save()
  //   .then(updated => {
  //       expect(updated).to.not.be.null
  //       expect(updated.name).to.equal('New Mary Doe')
  //       done()
  //     }).catch(err => {
  //       expect(err).to.be.null
  //     })
  // })
  //
  // it('delete user', function(done){
  //   user3.destroy()
  //     .then(user => {
  //       return db.models.User.findById('user-test-3')
  //     })
  //     .then(result => {
  //       expect(result).to.be.null
  //       done()
  //     })
  //     .catch(err => {
  //       expect(err).to.be.null
  //     })
  // })
}

function commentDB(){
  var comment1, comment2, comment3

  it('insert comment 1', function(done){
    db.models.Comment.create({
      id: 'comment-test-1',
      text: 'Comment text 1',
      userId: 'user-test-1'
    }).then(comment => {
      expect(comment).to.not.be.null
      comment1 = comment
      done()
    }).catch(err => {
      expect(err).to.be.null
    })
  })

  it('insert comment 2', function(done){
    db.models.Comment.create({
      id: 'comment-test-2',
      text: 'Comment text 2',
      userId: 'user-test-1'
    }).then(comment => {
      expect(comment).to.not.be.null
      comment2 = comment
      done()
    }).catch(err => {
      expect(err).to.be.null
    })
  })

  it('insert comment 3', function(done){
    db.models.Comment.create({
      id: 'comment-test-3',
      text: 'Comment text 3',
      userId: 'user-test-2'
    }).then(comment => {
      expect(comment).to.not.be.null
      comment3 = comment
      done()
    }).catch(err => {
      expect(err).to.be.null
    })
  })

  it('insert comment error', function(done){
    db.models.Comment.create({
      id: 'comment-test-3',
      text: 'Comment text 3'
    }).then(comment => {
      expect(comment).to.be.null
    }).catch(err => {
      expect(err).to.not.be.null
      done()
    })
  })
}

function createCommentOffline(){
  var app = null
  before('start server', function(done){
    app = require('../server')
    app.listen(app.get('port'), function(){
      done()
    })
  })

  it('read all', function(done){
    request(app)
      .get('/comments')
      .expect(200)
      .then(response => {
        expect(response).to.not.be.null
        expect(response.body).to.not.be.null
        expect(response.body.length).to.equal(3)
        done()
      })
      .catch(err => {
        expect(err).to.be.null
      })
  })

  it('create comment', function(done){
    request(app)
      .post('/comments')
      .set('userId', 'user-test-1')
      .send({id : 'comment-offline-1-user-test-1', text: 'Text offline 1'})
      .expect(201)
      .then(response => {
        expect(response).to.not.be.null
        expect(response.body).to.not.be.null
        expect(response.body.userId).to.equal('user-test-1')
        expect(response.body.text).to.equal('Text offline 1')
        done()
      })
      .catch(err => {
        expect(err).to.be.null
      })
  })

  it('create comment repeated', function(done){
    request(app)
      .post('/comments')
      .set('userId', 'user-test-1')
      .send({id : 'comment-offline-1-user-test-1', text: 'Text offline 1'})
      .expect(200)
      .then(response => {
        expect(response).to.not.be.null
        expect(response.body).to.not.be.null
        expect(response.body.userId).to.equal('user-test-1')
        expect(response.body.text).to.equal('Text offline 1')
        done()
      })
      .catch(err => {
        expect(err).to.be.null
      })
  })

  it('create comment user does not exists', function(done){
    request(app)
      .post('/comments')
      .set('userId', 'user-test-x')
      .send({id : 'comment-offline-1-user-test-1', text: 'Text offline 1'})
      .expect(401)
      .then(response => {
        expect(response).to.not.be.null
        expect(response.body).to.not.be.null
        done()
      })
      .catch(err => {
        expect(err).to.be.null
      })
  })
}
